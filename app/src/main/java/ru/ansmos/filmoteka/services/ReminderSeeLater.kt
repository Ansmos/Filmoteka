package ru.ansmos.filmoteka.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.ansmos.filmoteka.bll.Film

class ReminderSeeLater : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.getBundleExtra(FILM_BUNDLE)
        val film : Film = bundle?.get(FILM) as Film
        NotificationHelper.createNotification(context!!, film)
    }

    companion object{
        const val FILM = "FILM"
        const val FILM_BUNDLE = "FILM_BUNDLE"
    }
}