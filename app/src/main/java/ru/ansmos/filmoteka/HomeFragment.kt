package ru.ansmos.filmoteka

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.google.android.material.appbar.MaterialToolbar
import ru.ansmos.filmoteka.databinding.FragmentCastsBinding
import ru.ansmos.filmoteka.databinding.FragmentHomeBinding
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.decor.AnimationHelper
import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator
import ru.ansmos.filmoteka.rw.FilmAdapter
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var filmsAdapter: FilmAdapter

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
                        filmsAdapter.addItems((requireActivity() as MainActivity).filmsDataBase)
                        return true
                    }
                    //Фильтруем список на поискк подходящих сочетаний
                    val result = (requireActivity() as MainActivity).filmsDataBase.filter {
                        //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                        it.title.toLowerCase(Locale.getDefault()).contains(newText.toLowerCase(
                            Locale.getDefault()))
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
        filmsAdapter.addItems((requireActivity() as MainActivity).filmsDataBase)
    }
}