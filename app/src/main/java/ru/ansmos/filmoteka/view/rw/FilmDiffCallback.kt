package ru.ansmos.filmoteka.view.rw
import androidx.recyclerview.widget.DiffUtil
import ru.ansmos.filmoteka.db.Film

class FilmDiffCallback() {

    companion object{
        val FILM_COMPARATOR = object : DiffUtil.ItemCallback<Film>() {
            override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.description == newItem.description &&
                        oldItem.poster == newItem.poster
            }

        }
    }

}