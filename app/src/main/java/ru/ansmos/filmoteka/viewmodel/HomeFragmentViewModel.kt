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
        val films = interactor.getFilmsFromApi(1, object : IApiCallback{
            override fun onSuccess(films: List<Film>) {
                filmListLiveData.postValue(films)
            }

            override fun onFailure() {
            }
        })
    }

    interface IApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}