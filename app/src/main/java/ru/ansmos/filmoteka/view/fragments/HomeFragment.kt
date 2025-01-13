package ru.ansmos.filmoteka.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.*
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentHomeBinding
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator
import ru.ansmos.filmoteka.utils.AnimationHelper
import ru.ansmos.filmoteka.view.MainActivity
import ru.ansmos.filmoteka.view.rw.FilmAdapter
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }
    private lateinit var filmsAdapter: FilmAdapter
    private var lastVisibleItem = 0 // Для прокрутки и пагинации
    private var pageNumber = 1

    init {
        exitTransition = Fade()
        reenterTransition = Slide(Gravity.START).apply { duration = 500; }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnimationEnter()
        initSearchView()
        initRV()
        initPullToRefresh()
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.home_fragment_root), requireActivity(), 1)
//        viewModel.filmListLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer<List<Film>>{
//            filmsDataBase = it
//        })
        //Кладем нашу БД в RV
        viewModel.filmListLiveData.observe(viewLifecycleOwner, {
            filmsAdapter.addItems(it)
            Toast.makeText(requireContext(),"isNetworkOK = ${viewModel.isNetworkOK}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun initPullToRefresh(){
        //Вешаем слушатель, чтобы вызвался pull to refresh
        val pullToRefresh = requireActivity().findViewById<SwipeRefreshLayout>(R.id.pull_to_refresh)
        pullToRefresh.setOnRefreshListener {
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            filmsAdapter.clearItems()
            //Делаем новый запрос фильмов на сервер
            viewModel.page = 1
            viewModel.getFilmsPage()
            //Убираем крутящееся колечко
            pullToRefresh.isRefreshing = false
        }
    }

    private fun initAnimationEnter() {
        val scene = Scene.getSceneForLayout(requireActivity().findViewById(R.id.home_fragment_root),
            R.layout.merge_home_screen_content, requireContext())
        //Создаем анимацию выезда поля поиска сверху
        val searchSlide = Slide(Gravity.TOP).addTarget(R.id.search_view)
        //Создаем анимацию выезда RV снизу
        val recyclerSlide = Slide(Gravity.BOTTOM).addTarget(R.id.main_recycler)
        //Создаем экземпляр TransitionSet, который объединит все наши анимации
        val customTransition = TransitionSet().apply {
            //Устанавливаем время, за которое будет проходить анимация
            duration = 500
            //Добавляем сами анимации
            addTransition(recyclerSlide)
            addTransition(searchSlide)
        }
        //Также запускаем через TransitionManager, но вторым параметром передаем нашу кастомную анимацию
        //если это первый запуск
        if ((requireActivity() as MainActivity).firstStart) {
            TransitionManager.go(scene, customTransition)
            (requireActivity() as MainActivity).firstStart = false
        } else{
            TransitionManager.go(scene)
        }
    }

    private fun initSearchView() {
        // Поисковик сломался, но чинить его в задании не было. Еще тут API глючное, если просто задаешь выбрать страницу всего подряд, пишет Too mack results
        // Обязательно нужно что-то в поиск добавлять, я добавил "One"
        // TODO Пока оставлю, потом починю.
/*
        requireActivity().findViewById<SearchView>(R.id.search_view).apply {
            setOnClickListener {
                (it as SearchView).isIconified = false
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                //Этот метод отрабатывает на каждое изменения текста
                override fun onQueryTextChange(newText: String): Boolean {
                    //Если ввод пуст то вставляем в адаптер всю БД
                    if (newText.isEmpty()){
                        filmsAdapter.addItems(filmsDataBase)
                        return true
                    }
                    //Фильтруем список на поискк подходящих сочетаний
                    val result = filmsDataBase.filter {
                        //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                        it.title.lowercase(Locale.getDefault()).contains(
                            newText.lowercase(
                                Locale.getDefault()
                            )
                        )
                    }
                    //Добавляем в адаптер
                    filmsAdapter.addItems(result)
                    return true
                }
            })

        }
*/
    }

    private fun initRV() {
        val rv = requireActivity().findViewById<RecyclerView>(R.id.main_recycler)
        rv.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            //оставим его пока пустым, он нам понадобится во второй части задания
            filmsAdapter = FilmAdapter(object : FilmAdapter.IOnItemClixkListener {
                override fun click(film: Film) {
                    (requireActivity() as MainActivity).launchDetailsFragment(film)
                }
            })
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decor = FilmsRVItemDecorator(8)
            addItemDecoration(decor)
        }
        // Реализуем пагинацию на NestedScrollView, т.к. RecyclerView внутри не отвечает на это событие
        // TODO Запоминать позицию при переходе с бругого фрагмена
        // TODO При переходе с Details грузит одну текущую страницу. Нужно или все грузить или скролл наверх обработать
        requireActivity().findViewById<NestedScrollView>(R.id.main_container).apply {
            var heightSV = 0
            var heightRV = 0
            var heightRVprev = 0 //для плавной пагинации высота предыдущего RV
            var swIsSendQuery = false  //т.к. данные из сети грузятся с задержкой, для исключения повторного срабатывания
            setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
                override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                    heightSV = v.getMeasuredHeight()
                    heightRV = v.getChildAt(v.getChildCount() - 1).getMeasuredHeight()
                    heightRVprev = if (heightRVprev == 0) heightRV else heightRVprev
                    Log.i("SV","scrollY=$scrollY,  h_SV=$heightSV, h_RV=$heightRV, diff=${heightRV - heightSV}, h_RVPrev=$heightRVprev")
                    // Вся эта заморочка и-за предварительной загрузки до достижения конца списка (плавности)
                    if ((scrollY >= (heightRV - heightSV) - RV_LOADING_SHIFH) && scrollY > oldScrollY) {
                        if (!swIsSendQuery){  //Если запрос  еще не отправлен
                            viewModel.page = ++pageNumber
                            viewModel.getFilmsPage()
                            swIsSendQuery = true
                        }
                    }
                    //если данные уже подгрузились и RV удлиннилось, выставляем переключатель в готовность
                    if (heightRVprev != heightRV){
                        heightRVprev = heightRV
                        swIsSendQuery = false
                    }
                }

            })
        }
    }

    companion object{
        const val RV_LOADING_SHIFH: Int = 200 //Количество dp до конца списка для подгрузки новой страницы
        const val RV_PAGE_SIZE: Int = 10 //Количество Item На странице
    }
}