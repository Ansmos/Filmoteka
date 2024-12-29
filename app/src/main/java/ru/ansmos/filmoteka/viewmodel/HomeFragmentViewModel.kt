package ru.ansmos.filmoteka.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.domain.Interactor

class HomeFragmentViewModel: ViewModel() {

    val filmListLiveData = MutableLiveData<List<Film>>()
    var page: Int = 1
    private val interactor: Interactor = App.instance.interactor

    init{
        getFilmsPage(page)
    }

    fun changePage(pageNew: Int) {
        page = pageNew
        getFilmsPage(page)
    }

    private fun getFilmsPage(pageNew: Int){
        interactor.getFilmsFromApi(page, object : IApiCallback{
            override fun onSuccess(films: List<Film>) {
                filmListLiveData.postValue(films)
            }

            override fun onFailure() {
                Log.e("interactor","Error get page $page")
            }
        })
    }


    interface IApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}