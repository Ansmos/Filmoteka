package ru.ansmos.filmoteka.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.dombuketa.database_module.entity.FilmEntity
import ru.ansmos.filmoteka.bll.Film
import ru.ansmos.filmoteka.bll.Notification
import ru.dombuketa.database_module.entity.NotificationEntity
import java.time.LocalDateTime

object ConverterRoom {

    fun convertliveEntityToFilms(list: LiveData<List<ru.dombuketa.database_module.entity.FilmEntity>>): LiveData<List<Film>> {
        val result = Transformations.map(list){ filmEntityList ->
            val filmList = arrayListOf<Film>()
            filmEntityList.forEach {
                convertEntityToFilm(it)?.let { it1 -> filmList.add(it1) }
            }
            return@map filmList.toList()
        }
        return result
    }

    fun convertFlowEntityToFilms(list: Flow<List<ru.dombuketa.database_module.entity.FilmEntity>>): Flow<List<Film>> {
        val result = list.map {
             filmEntityList ->
                val filmList = arrayListOf<Film>()
                filmEntityList.forEach {
                    convertEntityToFilm(it)?.let { it1 -> filmList.add(it1) }
                }
                return@map filmList.toList()
            }
        return result
    }

    fun convertPagingEntityToFilms(list: DataSource.Factory<Int, FilmEntity>): DataSource.Factory<Int, Film> {
        return list.map {
                convertEntityToFilm(it)
        }
    }


    fun convertRxEntityToFilms(list: Observable<List<FilmEntity>>): Observable<List<Film>> {
        val result = list.map {
                filmEntityList ->
            val filmList = arrayListOf<Film>()
            filmEntityList.forEach {
                convertEntityToFilm(it)?.let { it1 -> filmList.add(it1) }
            }
            return@map filmList.toList()
        }
        return result
    }

    fun convertEntityToFilms(list: List<FilmEntity>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(convertEntityToFilm(it))
        }
        return result
    }

    private fun convertEntityToFilm(filmEntity: FilmEntity): Film {
        return Film(
            id = filmEntity.id_tmdb.toString(),
            title = filmEntity.title,
            poster = filmEntity.poster,
            description = filmEntity.description,
            rating = filmEntity.rating,
            releaseDate = filmEntity.release_date  //LocalDate.parse(it.release_date).year,
        )
    }

    fun convertFilmsToEntity(list: List<Film>?): List<FilmEntity> {
        val result = mutableListOf<FilmEntity>()
        list?.forEach {
            result.add( convertFilmToEntity(it))
        }
        return result
    }

    fun convertFilmToEntity(film: Film): FilmEntity {
        return FilmEntity(
            id = 0,
            id_tmdb = film.id.toInt(),
            title = film.title,
            poster = film.poster,
            description = film.description,
            rating = film.rating,
            release_date = film.releaseDate
        )
    }

// Конвертеры для Notifications

    fun convertNotificationsToEntity(list: Observable<List<Notification>>): Observable<List<NotificationEntity>> {
        val result = list.map { notificationEntityList ->
            val notificationList = arrayListOf<NotificationEntity>()
            notificationEntityList.forEach {
                convertNotificationToEntity(it)?.let { it1 -> notificationList.add(it1) }
            }
            return@map notificationList.toList()
        }
        return result
    }

    fun convertNotificationToEntity(notification: Notification): NotificationEntity {
        return NotificationEntity(
            id = notification.id,
            filmId = notification.filmId,
            title = notification.title,
            poster = notification.poster,
            startYear = notification.notificationTime.year,
            startMonth = notification.notificationTime.monthValue,
            startDay = notification.notificationTime.dayOfMonth,
            startHour = notification.notificationTime.hour,
            startMinute = notification.notificationTime.minute,
            isActive = notification.isActive,
        )
    }

    fun convertRxEntityToNotifications(list: Observable<List<NotificationEntity>>?): Observable<List<Notification>> {
        if (list != null) {
            val result = list.map { notificationsEntityList ->
                val notificationsList = arrayListOf<Notification>()
                notificationsEntityList.forEach {
                    convertEntityToNotification(it)?.let { it1 -> notificationsList.add(it1) }
                }
                return@map notificationsList.toList()
            }
            return result
        } else return Observable.just(null)
    }

    private fun convertEntityToNotification(notificationEntity: NotificationEntity): Notification {
        return Notification(
            id = notificationEntity.id,
            filmId = notificationEntity.filmId,
            title = notificationEntity.title,
            poster = notificationEntity.poster,
            isActive = notificationEntity.isActive,
            notificationTime = LocalDateTime.of(notificationEntity.startYear, notificationEntity.startMonth,
                notificationEntity.startDay, notificationEntity.startHour, notificationEntity.startMinute)
        )
    }

}