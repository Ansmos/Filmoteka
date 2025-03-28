package ru.ansmos.filmoteka.view.rw

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ansmos.filmoteka.databinding.FilmItemBinding
import ru.ansmos.filmoteka.bll.Film
import ru.ansmos.filmoteka.bll.Notification
import ru.ansmos.filmoteka.databinding.FilmNotificationItemBinding
import ru.dombuketa.net_tmdb.ApiConstants
import java.time.format.DateTimeFormatter

//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmNotificationViewHolder(var binding: FilmNotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
    //В этом методе кладем данные из Film в наши View
    fun bund(notification: Notification){
        binding.title.text = notification.title
        //poster.setImageResource(film.poster) Оставил на память
        Glide.with(itemView)
            //Загружаем сам ресурс
            .load(ApiConstants.IMAGES_URL_TMDB + "w780" + notification.poster)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(binding.poster)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy в HH:mm")
        binding.notification.text = notification.notificationTime.format(formatter)
    }
}