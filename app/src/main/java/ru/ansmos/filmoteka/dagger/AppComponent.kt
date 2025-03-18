package ru.ansmos.filmoteka.dagger

import dagger.Component
import ru.ansmos.filmoteka.dagger.modules.DatabaseModule
import ru.ansmos.filmoteka.dagger.modules.DomainModule
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel
import ru.ansmos.filmoteka.viewmodel.LaterFragmentViewModel
import ru.ansmos.filmoteka.viewmodel.SettingsFragmentViewModel
import ru.dombuketa.net_tmdb.dagger.ITmdbProvider
import javax.inject.Singleton
import kotlin.text.Typography.dagger

@Singleton
@Component(dependencies = [ITmdbProvider::class], modules = [DatabaseModule::class, DomainModule::class])
interface AppComponent {

    fun injHomeFragment(vm: HomeFragmentViewModel)
    fun injLaterFragment(vm: LaterFragmentViewModel)
    fun injSettingsFragment(vm: SettingsFragmentViewModel)
}