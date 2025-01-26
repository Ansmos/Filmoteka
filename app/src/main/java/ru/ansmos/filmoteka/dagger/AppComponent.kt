package ru.ansmos.filmoteka.dagger

import dagger.Component
import dagger.Module
import ru.ansmos.filmoteka.dagger.modules.DatabaseModule
import ru.ansmos.filmoteka.dagger.modules.DomainModule
import ru.ansmos.filmoteka.dagger.modules.RemoteModuleTmdb
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel
import ru.ansmos.filmoteka.viewmodel.SettingsFragmentViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [RemoteModuleTmdb::class, DatabaseModule::class, DomainModule::class])
interface AppComponent {

    fun injHomeFragment(vm: HomeFragmentViewModel)
    fun injSettingsFragment(vm: SettingsFragmentViewModel)
}