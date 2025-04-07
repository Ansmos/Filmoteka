package ru.ansmos.filmoteka.bll

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.Date

@Parcelize
class Notification (
    var id: Int = 0,
    val filmId: Int,
    val title: String,
    val poster: String,
    var notificationTime: LocalDateTime,
    var isActive: Boolean = true
) : Parcelable {
    // ДЛя нотификаций
    fun toFilm() : Film{
        return Film(
            id = this.filmId.toString(),
            title = this.title,
            poster = this.poster,
            description = "",
            releaseDate = "",
            rating = 0.0,
            isInFavorites = false

        )
    }
}
