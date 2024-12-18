package ru.ansmos.filmoteka.rw

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.db.Film

//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
    //Привязываем View из layout к переменным
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val poster = itemView.findViewById<ImageView>(R.id.poster)
    private val desc = itemView.findViewById<TextView>(R.id.description)

    //В этом методе кладем данные из Film в наши View
    fun bund(film: Film){
        title.text = film.title
        poster.setImageResource(film.poster)
        desc.text = film.description
    }
}