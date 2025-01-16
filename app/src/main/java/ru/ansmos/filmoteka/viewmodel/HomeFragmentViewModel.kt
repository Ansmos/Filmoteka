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
    var isNetworkOK : Boolean = true  //true-данные взяты из сети, false-из БД
    val isNetworkError = SingleLiveEvent<Boolean>()
    var filmListLiveData : LiveData<List<Film>>? = null
    //val filmMutableListLiveData = MutableLiveData<List<Film>>()
    val showProgressBar : MutableLiveData<Boolean> = MutableLiveData()
    var page: Int = 1
    @Inject lateinit var preference: PreferenceProvider  //Для онлайн смены контента при смене настройки
    @Inject lateinit var interactor: InteractorTmdb

    init{
        App.instance.dagger.injHomeFragment(this)
        // Берем из БД то, что есть.
 // in getPage То же делаю
 //        filmListLiveData = interactor.getFilmsFromDB(page , PAGE_SIZE_FROM_DB)
//        MediatorLiveData<Unit>().addSource(filmListLiveData, {
//            filmMutableListLiveData.postValue(it)})
        // Interactor кладет фильмы в БД
        getFilmsPage()
        // Слушаем смену категории в настройках 38*
        preference.currentCategory.observeForever {
            getFilmsPage()
        }
        interactor.gotoDefaultCategory()
    }

    fun getFilmsPage() {    //Вернем статус запроса из сети для потребителей View
        showProgressBar.postValue(true)
        interactor.getFilmsFromApi(page, object : IApiCallback{
            //41 override fun onSuccess(films: List<Film>) {
            override fun onSuccess() {
                showProgressBar.postValue(false)
                isNetworkError.postValue(true)
            }

            override fun onFailure() {
                isNetworkError.postValue(true)
                showProgressBar.postValue(false)
                isNetworkOK = false
                val nowTime = System.currentTimeMillis()
                val lastTime = preference.getLastUploadSucsessDateTime()
                Log.d("interactor","Error get page $page from INET - Get from DB - Now ${SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(nowTime)} - Prev ${SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(lastTime)} = ${nowTime - lastTime}")
                Executors.newSingleThreadExecutor().execute {
                    //Если прошло времени больше, чем настроено с последней загрузки, удаляем кеш.
                    if (System.currentTimeMillis() - preference.getLastUploadSucsessDateTime() > TIME_TO_PURGE_CACH) {
                        interactor.clearFilmsInDB()
                    }
                    page = page
                    Log.i("VM_getFilmsPage","page=$page")
                    filmListLiveData = interactor.getFilmsFromDB(page, PAGE_SIZE_FROM_DB)
                    page++
                }
            }
        })
    }


    interface IApiCallback {
        //41 fun onSuccess(films: List<Film>)
        fun onSuccess()
        fun onFailure()
    }

    companion object{
        const val TIME_TO_PURGE_CACH = 600000_000L //Время существование кеша, после удаление из БД в мс
        const val PAGE_SIZE_FROM_DB = 10
    }
}