package ru.ansmos.filmoteka.bll

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.Date

@Parcelize
class Notification (
    val id: Int = 0,
    val filmId: Int,
    val title: String,
    val poster: String,
    var notificationTime: LocalDateTime,
    var isActive: Boolean = true
) : Parcelable
