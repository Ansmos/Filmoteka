package ru.dombuketa.database_module.repositories

import android.app.Notification
import androidx.paging.DataSource
import io.reactivex.rxjava3.core.Observable
import ru.dombuketa.database_module.dao.INotificationDao
import ru.dombuketa.database_module.dao.ITmdbFilmDao
import ru.dombuketa.database_module.entity.FilmEntity
import ru.dombuketa.database_module.entity.NotificationEntity
import java.util.concurrent.Executors
import javax.inject.Inject

class MainRepository @Inject constructor(private val filmDao: ITmdbFilmDao, private val notificationDao: INotificationDao)  {

    fun putFilms(films: List<FilmEntity>) {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getFilms(pageIndex: Int, pageSize: Int): Observable<List<FilmEntity>> {
        return filmDao.getFilmsByPage(pageIndex, pageSize)
    }

    fun getFilmsPaging(): androidx.paging.DataSource.Factory<Int, FilmEntity> {
        return filmDao.getFilms_Paging()
    }


    fun clearAllFilms() : Int {
        var deletedItemsCount : Int = 0
        Executors.newSingleThreadExecutor().execute {
             deletedItemsCount = filmDao.clearAll()
        }
        //Омновной поток не ждет другого, поэтому возвращает 0, если через дебаг, правильно. Как сделать возврат?
        return deletedItemsCount
    }

    fun getDataPDS(startPosition: Int, loadSize: Int): DataSource.Factory<Int, FilmEntity> {
        return filmDao.getFilmsByPage_Paging(startPosition, loadSize)
    }

// Нотификации

    fun getAllNotifications(): Observable<List<NotificationEntity>> = notificationDao.getAllNotifications()

    fun insertNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    fun updateNotification(notification: NotificationEntity) {
        // Для упрощения деактивирую старый и вставляю новый
        notificationDao.cancelNotification(notification.filmId)
        notificationDao.insertNotification(notification)
    }

    fun cancelNotification(film_id: Int) = notificationDao.cancelNotification(film_id)

    fun cancelAllNotification() = notificationDao.cancelAllNotifications()

}