package ru.ansmos.filmoteka.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.*
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentHomeBinding
import ru.ansmos.filmoteka.bll.Film
import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator
import ru.ansmos.filmoteka.utils.AnimationHelper
import ru.ansmos.filmoteka.utils.AutoDisposable
import ru.ansmos.filmoteka.utils.addTo
import ru.ansmos.filmoteka.view.MainActivity
import ru.ansmos.filmoteka.view.rw.FilmAdapter
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }
    private val autoDisposable = AutoDisposable()
    private lateinit var filmsAdapter: FilmAdapter

    init {
        exitTransition = Fade()
        reenterTransition = Slide(Gravity.START).apply { duration = 500; }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnimationEnter()
        initSearchView()
        initRV()
        initPullToRefresh()
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.home_fragment_root), requireActivity(), 1)
        //Подписываемся на сообщение о сетевой ошибке
        viewModel.showNetworkErrorSnack
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val snack = Snackbar.make(view, R.string.m41_network_error, Snackbar.LENGTH_INDEFINITE)
                if (it) {
                    snack.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .setAction("OK") {
                            Log.i("HF", "snack/ page=  ${viewModel.showProgressBar}")
                            snack.dismiss()
                        }
                        .show()
                } else {
                    if (snack.isShown) snack.dismiss()
                }
            },{

            })
            .addTo(autoDisposable)

        //Подписываемся на progressBar
        viewModel.showProgressBar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.root.findViewById<ProgressBar>(R.id.progress_bar).isVisible = it
            },{
                it.printStackTrace()
            })
            .addTo(autoDisposable)

        //Кладем нашу БД в RV
        viewModel.filmListRxData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val adapterSize = filmsAdapter.itemCount
                //val diff = FilmDiff(filmsAdapter.getItems(), it)
                //val diffResult = DiffUtil.calculateDiff(diff)
                filmsAdapter.addItems(it)
                //diffResult.dispatchUpdatesTo(filmsAdapter)
                if (it.size > 0) Log.i("HF","Список был ${adapterSize} -> ${filmsAdapter.itemCount} : (${it[0].title} - ${it[9].title}")
            }, {
                Log.i("FH", "error ${it.message}")
            }, {
                Log.i("FH", "onCompleted")
            })
            .addTo(autoDisposable)
    }

    private fun initPullToRefresh(){
        //Вешаем слушатель, чтобы вызвался pull to refresh
        val pullToRefresh = requireActivity().findViewById<SwipeRefreshLayout>(R.id.pull_to_refresh)
        pullToRefresh.setOnRefreshListener {
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            filmsAdapter.clearItems()
            //Делаем новый запрос фильмов на сервер
            viewModel.showNetworkErrorSnack.onNext(false)
            viewModel.page = 1
            viewModel.getFilmsPageRx(true)
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
        val searchView = requireActivity().findViewById<SearchView>(R.id.search_view)
        searchView.setOnClickListener {
            (it as SearchView).isIconified = false
        }

        Observable.create(ObservableOnSubscribe<String> {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
                override fun onQueryTextSubmit(query: String?): Boolean {
                    it.onNext(query)
                    return false
                }

                //Этот метод отрабатывает на каждое изменения текста
                override fun onQueryTextChange(newText: String): Boolean {
                    filmsAdapter.clearItems()
                    it.onNext(newText)
                    return false
                }
            })
        })
        .observeOn(Schedulers.io())
            .map {
                it.lowercase(Locale.getDefault()).trim()
            }
            .debounce(1, TimeUnit.SECONDS)
            .filter {
                viewModel.page = 1
                viewModel.getFilmsSearchRx(it)
                Log.i("Interactor:Search","query: $it")
                it.isNotBlank()
            }
            .flatMap {
                viewModel.getFilmsSearchRx(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                filmsAdapter.addItems(it)
            },{
                Log.i("Interactor:Search","error - ${it.message}")
            },{
                Log.i("Interactor:Search","success")
            })
            .addTo(autoDisposable)
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
                            //viewModel.page = ++pageNumber
                            val searchString = requireActivity().findViewById<SearchView>(R.id.search_view).query
                            if (searchString.isNotBlank()) {
                                viewModel.getFilmsSearchRx(searchString.toString()).subscribe({
                                    filmsAdapter.addItems(it)
                                },
                                    {
                                        Log.i("initRv", it.message.toString())
                                    })
                            } else {
                                viewModel.getFilmsPageRx(true) //На следущую страницу
                            }
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