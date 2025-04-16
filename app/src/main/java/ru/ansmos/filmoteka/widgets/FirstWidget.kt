package ru.ansmos.filmoteka.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.view.MainActivity

class FirstWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        appWidgetIds?.forEach {
            val pendingIntent = Intent(context, MainActivity::class.java).let { intent ->
                PendingIntent.getActivity(context, 0, intent, 0)
            }
            val views = RemoteViews(context?.packageName, R.layout.widget_first).apply {
                setOnClickPendingIntent(R.id.widget_image_button, pendingIntent)
            }
            appWidgetManager?.updateAppWidget(it,  views)
        }
        //super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}