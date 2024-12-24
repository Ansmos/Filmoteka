package ru.ansmos.filmoteka.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.filmoteka.view.MainActivity
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentFavoriteBinding
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.utils.AnimationHelper
import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator
import ru.ansmos.filmoteka.view.rw.FilmAdapter

class FavoriteFragment : Fragment() {
    private lateinit var binding : FragmentFavoriteBinding
    private lateinit var filmsAdapter: FilmAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Получаем список при транзакции фрагмента
        val favoriteList: List<Film> = emptyList()

        requireActivity().findViewById<RecyclerView>(R.id.favorites_recycler).apply {
            filmsAdapter = FilmAdapter((object : FilmAdapter.IOnItemClixkListener{
                override fun click(film: Film) {
                    (requireActivity() as MainActivity).launchDetailsFragment(film)
                }
            }))
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decor = FilmsRVItemDecorator(8)
            addItemDecoration(decor)
        }
        //Кладем нашу БД в RV
        filmsAdapter.addItems((requireActivity() as MainActivity).filmsDataBase.filter{
            it.isInFavorites
        })
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.fav_fragment_root), requireActivity(), 2)

    }
}