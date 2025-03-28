package ru.ansmos.filmoteka.dagger

import dagger.Component
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.services.NotificationHelper
import ru.ansmos.filmoteka.viewmodel.CastsFragmentViewModel
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
    fun injCastsFragment(vm: CastsFragmentViewModel)
    fun injSettingsFragment(vm: SettingsFragmentViewModel)

    fun getNotificationHelper() : NotificationHelper
    fun getInteractor() : InteractorTmdb
}