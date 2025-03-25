package ru.ansmos.filmoteka.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.bll.Film
import ru.ansmos.filmoteka.view.MainActivity
import ru.dombuketa.net_tmdb.ApiConstants

class Notification() {



    companion object {
        const val CHANNEL_ID = "FilmotekaChannel"

        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel_name = "SeeLaterChannel"
                val channel_desc = "Filmoteka channel"
                val channel_impt = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, channel_name, channel_impt)
                channel.description = channel_desc
                NotificationManagerCompat.from(context).createNotificationChannel(channel)
            }
        }

        fun createNotification(context: Context, film: Film) {
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT )
            val notificationManager = NotificationManagerCompat.from(context)
            val notifBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_btn_later)
                setContentText(film.title)
                setContentTitle("Не забыть посомтреть.")
                priority = NotificationCompat.PRIORITY_DEFAULT
                setContentIntent((pendingIntent))
                setAutoCancel(true)
            }
            //Загружаем картинку
            Glide.with(context)
                .asBitmap()
                .load(ApiConstants.IMAGES_URL_TMDB + "w500" + film.poster)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        //Создаем нотификацию
                        notifBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                        notificationManager.notify(film.id.toInt(), notifBuilder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                })
            notificationManager.notify(film.id.toInt(), notifBuilder.build())
        }
    }
}