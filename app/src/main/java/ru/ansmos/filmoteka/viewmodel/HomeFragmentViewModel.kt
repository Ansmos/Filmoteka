package ru.ansmos.filmoteka.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.domain.Interactor

class HomeFragmentViewModel: ViewModel() {
    val filmListLiveData = MutableLiveData<List<Film>>()
    private val interactor: Interactor = App.instance.interactor

    init{
        val films = interactor.getFilmsDB()
        filmListLiveData.postValue(films)
    }
}