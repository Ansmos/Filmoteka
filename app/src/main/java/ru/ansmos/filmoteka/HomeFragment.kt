package ru.ansmos.filmoteka

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator
import ru.ansmos.filmoteka.rw.FilmAdapter
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var filmsAdapter: FilmAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRV()
        initSearchView()
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