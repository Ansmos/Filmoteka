package ru.dombuketa.database_module.dagger

import ru.dombuketa.database_module.dao.ITmdbFilmDao
import ru.dombuketa.database_module.dao.INotificationDao

interface IDatabaseProvider {
    fun provideDatabase() : ITmdbFilmDao
    fun provideNotifications() : INotificationDao
}