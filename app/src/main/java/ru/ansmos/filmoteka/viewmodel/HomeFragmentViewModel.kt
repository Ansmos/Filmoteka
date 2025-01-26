package ru.ansmos.filmoteka.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import ru.ansmos.filmoteka.utils.SingleLiveEvent
import java.text.SimpleDateFormat
import java.util.concurrent.Executors
import javax.inject.Inject

class HomeFragmentViewModel: ViewModel() {
    val isNetworkError = SingleLiveEvent<Boolean>()
    val filmListLiveData : LiveData<List<Film>>

    val showProgressBar : MutableLiveData<Boolean> = MutableLiveData()
    var page: Int = 1
    @Inject lateinit var preference: PreferenceProvider  //Для онлайн смены контента при смене настройки
    @Inject lateinit var interactor: InteractorTmdb

    init{
        App.instance.dagger.injHomeFragment(this)
        //Если прошло времени больше, чем настроено с последней загрузки, удаляем кеш.
        if (System.currentTimeMillis() - preference.getLastUploadSucsessDateTime() > TIME_TO_PURGE_CACH) {
            Executors.newSingleThreadExecutor().execute {
                interactor.clearFilmsInDB()
            }
        }
        // Берем из БД то, что есть.
         filmListLiveData = interactor.getFilmsFromDB(page , PAGE_SIZE_FROM_DB)
        // Слушаем смену категории в настройках 38*
        interactor.gotoDefaultCategory()
        preference.currentCategory.observeForever {
            getFilmsPage()
        }
    }

    fun getFilmsPage() {    //Вернем статус запроса из сети для потребителей View
        showProgressBar.postValue(true)
        interactor.getFilmsFromApi(page, object : IApiCallback {
            override fun onSuccess() {
                page++
                showProgressBar.postValue(false)
            }
            override fun onFailure() {
                isNetworkError.postValue(true)
                showProgressBar.postValue(false)
                Log.d("interactor", "Error get page $page from INET - Get from DB")
            }
        })
    }

    interface IApiCallback {
        fun onSuccess()
        fun onFailure()
    }

    companion object{
        const val TIME_TO_PURGE_CACH = 600000_000L //Время существование кеша, после удаление из БД в мс
        const val PAGE_SIZE_FROM_DB = 10
    }
}