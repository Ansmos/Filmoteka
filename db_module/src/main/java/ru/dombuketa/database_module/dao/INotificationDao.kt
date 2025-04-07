package ru.dombuketa.database_module.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.dombuketa.database_module.entity.NotificationEntity

@Dao
interface INotificationDao {
    // Notifications (Уведомления)
    @Query("SELECT * FROM notifications WHERE is_active = 1 ")
    fun getAllNotifications() : Observable<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE id = (:id) ")
    fun getNotificationById(id : Int) : Single<NotificationEntity>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET is_active = 0 WHERE film_id = :film_id")
    fun cancelNotification(film_id : Int)

    @Query("UPDATE notifications SET is_active = 0")
    fun cancelAllNotifications()
}