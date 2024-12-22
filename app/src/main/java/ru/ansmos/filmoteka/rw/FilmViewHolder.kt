package ru.ansmos.filmoteka.rw

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.decor.RatingDonutView

//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
    //Привязываем View из layout к переменным
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val poster = itemView.findViewById<ImageView>(R.id.poster)
    private val desc = itemView.findViewById<TextView>(R.id.description)
    //Вот здесь мы находим в верстке наш прогресс бар для рейтинга
    private val ratingDonut = itemView.findViewById<RatingDonutView>(R.id.rating_donut)

    //В этом методе кладем данные из Film в наши View
    fun bund(film: Film){
        title.text = film.title
        //poster.setImageResource(film.poster) Оставил на память
        Glide.with(itemView)
            //Загружаем сам ресурс
            .load(film.poster)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(poster)
        desc.text = film.description
        //Устанавливаем рэйтинг
        ratingDonut.setProgress((film.rating * 10).toInt())
        if (film.rating != 0f) {
            ratingDonut.visibility = View.VISIBLE
        } else {
            ratingDonut.visibility = View.INVISIBLE
        }
    }
}