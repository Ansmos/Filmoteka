package ru.ansmos.filmoteka.services

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.bll.Film
import ru.ansmos.filmoteka.bll.Notification
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.view.MainActivity
import ru.dombuketa.net_tmdb.ApiConstants
import java.time.LocalDateTime
import java.util.Calendar

object NotificationHelper {
    const val CHANNEL_ID = "FilmotekaChannel"
    const val CHANNEL_TILTLE = "Не забыть посомтреть."

    val interactor: InteractorTmdb = App.instance.dagger.getInteractor()

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
        intent.putExtra("film", film)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT )
        val notificationManager = NotificationManagerCompat.from(context)
        val notifBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_btn_later)
            setContentText(film.title)
            setContentTitle(CHANNEL_TILTLE)
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

    fun notificationSet(context: Context, film: Film){
        val calendar = Calendar.getInstance()
        val curY = calendar.get(Calendar.YEAR)
        val curM = calendar.get(Calendar.MONTH)
        val curD = calendar.get(Calendar.DAY_OF_MONTH)
        val curH = calendar.get(Calendar.HOUR_OF_DAY)
        val curm = calendar.get(Calendar.MINUTE)

        DatePickerDialog(context,{
            _, dpdYear, dpdMonth, dayOfMonth ->
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, pickerMinute ->
                val pickerDateTime = Calendar.getInstance()
                pickerDateTime.set(dpdYear, dpdMonth, dayOfMonth, hourOfDay, pickerMinute, 0)
                val dateTimeInMillis = pickerDateTime.timeInMillis
                interactor.insertNotification(Notification(
                    id =  0,
                    filmId = film.id.toInt(),
                    title = film.title,
                    poster = film.poster,
                    notificationTime = LocalDateTime.of(curY, curM, curD, curH, curm),
                    isActive = true
                ))
                createWatchLaterEvent(context, dateTimeInMillis, film)
            }
            TimePickerDialog(context, timeSetListener, curH, curm, true).show()
        }, curY, curM, curD).show()
    }

    private fun createWatchLaterEvent(context: Context, dateTimeInMillis: Long, film: Film) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(film.title, null, context, ReminderSeeLater():: class.java)
        val bundle = Bundle()
        bundle.putParcelable(ReminderSeeLater.FILM, film)
        intent.putExtra(ReminderSeeLater.FILM_BUNDLE, bundle)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Устанавливаем напоминалку
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTimeInMillis, pendingIntent)
    }
}
