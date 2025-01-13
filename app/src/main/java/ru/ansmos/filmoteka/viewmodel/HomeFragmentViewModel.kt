package ru.ansmos.filmoteka.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import java.text.SimpleDateFormat
import java.util.concurrent.Executors
import javax.inject.Inject

class HomeFragmentViewModel: ViewModel() {
    var isNetworkOK : Boolean = true  //true-данные взяты из сети, false-из БД
    var isOneRequest_ifFailureNetwork : Boolean = false
    val filmListLiveData = MutableLiveData<List<Film>>()
    var page: Int = 1
    @Inject lateinit var preference: PreferenceProvider  //Для онлайн смены контента при смене настройки
    @Inject lateinit var interactor: InteractorTmdb

    init{
        App.instance.dagger.injHomeFragment(this)
        getFilmsPage()
        // Слушаем смену категории в настройках 38*
        preference.currentCategory.observeForever {
//            Toast.makeText(App.instance.applicationContext,it,Toast.LENGTH_SHORT).show()
            getFilmsPage()
        }
    }

    fun getFilmsPage(): Int{    //Вернем статус запроса из сети для потребителей View
        var recordsCount = -1
        interactor.getFilmsFromApi(page, object : IApiCallback{
            override fun onSuccess(films: List<Film>) {
                filmListLiveData.postValue(films)
                isOneRequest_ifFailureNetwork = false
                isNetworkOK = true
                recordsCount = films.size
            }

            override fun onFailure() {
                isNetworkOK = false
                val nowTime = System.currentTimeMillis()
                val lastTime = preference.getLastUploadSucsessDateTime()
                Log.d("interactor","Error get page $page from INET - Get from DB - Now ${SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(nowTime)} - Prev ${SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(lastTime)} = ${nowTime - lastTime}")
                Executors.newSingleThreadExecutor().execute {
                    //Если прошло времени больше, чем настроено с последней загрузки, удаляем кеш.
                    if (System.currentTimeMillis() - preference.getLastUploadSucsessDateTime() > TIME_TO_PURGE_CACH) {
                        interactor.clearFilmsInDB()
                    }
                    val filmsFromDB = interactor.getFilmsFromDB(page, PAGE_SIZE_FROM_DB)
                    recordsCount = filmsFromDB.size
                    if (recordsCount == PAGE_SIZE_FROM_DB){
                        page++
                    }
                    filmListLiveData.postValue(filmsFromDB)
                    isOneRequest_ifFailureNetwork = true
                }
            }
        })
        return recordsCount
    }


    interface IApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }

    companion object{
        const val TIME_TO_PURGE_CACH = 600000_000L //Время существование кеша, после удаление из БД в мс
        const val PAGE_SIZE_FROM_DB = 10
    }
}