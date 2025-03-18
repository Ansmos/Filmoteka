package ru.ansmos.filmoteka.dagger

import dagger.Component
import ru.dombuketa.database_module.dagger.IDatabaseProvider
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel
import ru.ansmos.filmoteka.viewmodel.LaterFragmentViewModel
import ru.ansmos.filmoteka.viewmodel.SettingsFragmentViewModel
import ru.dombuketa.net_tmdb.dagger.ITmdbProvider
import javax.inject.Singleton

@Singleton
@Component(dependencies = [ITmdbProvider::class, IDatabaseProvider::class], modules = [DomainModule::class])
interface AppComponent {

    fun injHomeFragment(vm: HomeFragmentViewModel)
    fun injLaterFragment(vm: LaterFragmentViewModel)
    fun injSettingsFragment(vm: SettingsFragmentViewModel)
}