package ru.ansmos.filmoteka.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.domain.Interactor
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import javax.inject.Inject
import javax.inject.Named

class HomeFragmentViewModel: ViewModel() {

    val filmListLiveData = MutableLiveData<List<Film>>()
    var page: Int = 1
    @Inject lateinit var preference: PreferenceProvider  //Для онлайн смены контента при смене настройки
    //@Inject lateinit var interactor: Interactor
    @Inject lateinit var interactor: InteractorTmdb

    init{
        App.instance.dagger.injHomeFragment(this)
        getFilmsPage(page)
        // Слушаем смену категории в настройках 38*
        preference.currentCategory.observeForever {
            Toast.makeText(App.instance.applicationContext,it,Toast.LENGTH_SHORT).show()
            getFilmsPage(page)
        }


    }


    fun changePage(pageNew: Int) {
        page = pageNew
        getFilmsPage(page)
    }

    fun getFilmsPage(pageNew: Int){
        interactor.getFilmsFromApi(page, object : IApiCallback{
            override fun onSuccess(films: List<Film>) {
                filmListLiveData.postValue(films)
            }

            override fun onFailure() {
                Log.e("interactor","Error get page $page from INET - Het from DB")
                filmListLiveData.postValue(interactor.getFilmsFromDB())
            }
        })
    }


    interface IApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}