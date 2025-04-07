package ru.dombuketa.database_module.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dombuketa.database_module.dao.INotificationDao
import ru.dombuketa.database_module.dao.ITmdbFilmDao
import ru.dombuketa.database_module.entity.FilmEntity
import ru.dombuketa.database_module.entity.NotificationEntity

@Database(entities = [FilmEntity::class, NotificationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tmdbFilmDao(): ITmdbFilmDao
    abstract fun notificationDao(): INotificationDao
}