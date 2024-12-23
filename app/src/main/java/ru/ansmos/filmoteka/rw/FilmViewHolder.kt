package ru.ansmos.filmoteka.rw

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FilmItemBinding
import ru.ansmos.filmoteka.db.Film

//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(var binding: FilmItemBinding) : RecyclerView.ViewHolder(binding.root) {
    //В этом методе кладем данные из Film в наши View
    fun bund(film: Film){
        binding.title.text = film.title
        //poster.setImageResource(film.poster) Оставил на память
        Glide.with(itemView)
            //Загружаем сам ресурс
            .load(film.poster)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(binding.poster)
        binding.description.text = film.description
    }
}