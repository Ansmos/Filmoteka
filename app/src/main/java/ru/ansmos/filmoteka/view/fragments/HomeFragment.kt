package ru.ansmos.filmoteka.view.fragments

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import ru.ansmos.filmoteka.view.MainActivity
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentHomeBinding
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.utils.AnimationHelper
import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator
import ru.ansmos.filmoteka.view.rw.FilmAdapter
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }
    private lateinit var filmsAdapter: FilmAdapter
    private var filmsDataBase = listOf<Film>()
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) {
                return
            } else {
                //Если пришло другое значение, то кладем его в переменную
                field = value
                //Обновляем RV адаптер
                filmsAdapter.addItems(field)
            }
        }

    init {
        exitTransition = Fade() // Slide(Gravity.START).apply { duration = 500; mode = Slide.MODE_OUT }
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
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.home_fragment_root), requireActivity(), 1)
        viewModel.filmListLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer<List<Film>>{
            filmsDataBase = it
        })
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
        //Кладем нашу БД в RV
        filmsAdapter.addItems(filmsDataBase)
    }
}