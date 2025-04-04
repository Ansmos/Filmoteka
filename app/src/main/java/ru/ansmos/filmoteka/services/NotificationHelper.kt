package ru.ansmos.filmoteka.services

import android.app.*
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
import java.time.ZoneId
import java.util.*

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
                    //Обновляем нотификацию
                    notificationManager.notify(film.id.toInt(), notifBuilder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        //Отправляем изначальную нотификацию в стандартном исполнении
        notificationManager.notify(film.id.toInt(), notifBuilder.build())
    }

    fun notificationSet(context: Context, filmOrNotification: Any?){
        if (filmOrNotification == null) return
        val calendar = Calendar.getInstance()
        val curY = calendar.get(Calendar.YEAR)
        val curM = calendar.get(Calendar.MONTH)
        val curD = calendar.get(Calendar.DAY_OF_MONTH)
        val curH = calendar.get(Calendar.HOUR_OF_DAY)
        val curm = calendar.get(Calendar.MINUTE)
        // Оставлю для примера when с объектом
        // Вся эта тема связана с тем, что когда нет сети, мы не можем получить фильм, а в БД его может уже не быть
        // поэтому берем все его данные из нотификации, чтобы напоминание про него все равно пришло
        // Енсли прислали вообще другой обхект, то выходим из процедуры, ничего не делая.
        var notification: Notification? = null
        when (filmOrNotification) {
            is Film -> {
                notification = Notification(
                    id =  0,
                    filmId = filmOrNotification.id.toInt(),
                    title = filmOrNotification.title,
                    poster = filmOrNotification.poster,
                    notificationTime = LocalDateTime.of(curY, curM, curD, curH, curm),
                    isActive = true
                )
            }
            is Notification -> notification = filmOrNotification
            else -> null
        }
        if (notification == null) return

        DatePickerDialog(context,{
            _, dpdYear, dpdMonth, dayOfMonth ->
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, pickerMinute ->
                val pickerDateTime = Calendar.getInstance()
                pickerDateTime.set(dpdYear, dpdMonth, dayOfMonth, hourOfDay, pickerMinute, 0)
                val dateTimeInMillis = pickerDateTime.timeInMillis
                //interactor.insertNotification(notification)
                val newTime = LocalDateTime.ofInstant(pickerDateTime.toInstant(), pickerDateTime.timeZone.toZoneId())
                notification.notificationTime = newTime
                interactor.updateNotification(notification)
                createWatchLaterEvent(context, dateTimeInMillis, notification.toFilm())
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
