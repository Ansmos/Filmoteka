package ru.ansmos.filmoteka.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named

class HomeFragmentViewModel: ViewModel() {
    var isOneRequest_afterClearDB : Boolean = false
    val filmListLiveData = MutableLiveData<List<Film>>()
    var page: Int = 1
    @Inject lateinit var preference: PreferenceProvider  //Для онлайн смены контента при смене настройки
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
                isOneRequest_afterClearDB = false
            }

            override fun onFailure() {
                Log.e("interactor","Error get page $page from INET - Het from DB")
                Executors.newSingleThreadExecutor().execute {
                    if (System.currentTimeMillis() - preference.getLastUploadSucsessDateTime() < TIME_TO_PURGE_CACH) {
                        filmListLiveData.postValue(interactor.getFilmsFromDB())
                    } else {
                        interactor.clearFilmsInDB()
                        isOneRequest_afterClearDB = true
                    }
                    //Чтобы не бивать процессор запросами в сеть
                    if (!isOneRequest_afterClearDB){
                        getFilmsPage(1)
                    }
                }
            }
        })
    }


    interface IApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }

    companion object{
        const val TIME_TO_PURGE_CACH = 600_000L //Время существование кеша, после удаление из БД в мс
    }
}