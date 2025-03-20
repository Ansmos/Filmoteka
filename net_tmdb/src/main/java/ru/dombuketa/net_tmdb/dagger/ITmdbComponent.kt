package ru.dombuketa.net_tmdb.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RemoteModuleTmdb::class])
interface ITmdbComponent : ITmdbProvider {
}