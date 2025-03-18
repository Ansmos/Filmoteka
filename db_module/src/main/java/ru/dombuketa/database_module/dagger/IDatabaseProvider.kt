package ru.dombuketa.database_module.dagger

import ru.dombuketa.database_module.dao.ITmdbFilmDao

interface IDatabaseProvider {
    fun provideDatabase() : ITmdbFilmDao
}