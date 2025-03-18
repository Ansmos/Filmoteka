package ru.ansmos.filmoteka.view.rw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import ru.ansmos.filmoteka.databinding.FilmItemBinding
import ru.ansmos.filmoteka.bll.Film


class FilmPaggingAdapter(val diffUtil: DiffUtil.ItemCallback<Film>): PagedListAdapter<Film, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmPaggingViewHolder {
        val binding = FilmItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return FilmPaggingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        getItem(position)?.also {
            (holder as FilmPaggingViewHolder).bind(it)
        }
    }


}




