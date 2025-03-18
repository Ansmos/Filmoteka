package ru.ansmos.filmoteka.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class HomeFragmentViewModel: ViewModel() {
    val filmListRxData : Observable<List<Film>>
    val showProgressBar : BehaviorSubject<Boolean> //Channel<Boolean> //MutableLiveData<Boolean> = MutableLiveData()
    val showNetworkErrorSnack : BehaviorSubject<Boolean> //MutableLiveData<Boolean> = MutableLiveData()

    var page = 1
    get() = field
    set(value){
        interactor.pageNumber = value
        field = value
    }

    @Inject lateinit var preference: PreferenceProvider  //Для онлайн смены контента при смене настройки
    @Inject lateinit var interactor: InteractorTmdb

    init{
        App.instance.dagger.injHomeFragment(this)
        showProgressBar = interactor.isProgressBarVisible
        showNetworkErrorSnack = interactor.isNetworkError
        page = interactor.pageNumber
        //Если прошло времени больше, чем настроено с последней загрузки, удаляем кеш.
        if (System.currentTimeMillis() - preference.getLastUploadSucsessDateTime() > TIME_TO_PURGE_CACH) {
            CoroutineScope(EmptyCoroutineContext).launch {
                interactor.clearFilmsInDB()
            }
        }
        // Берем из БД то, что есть.
         filmListRxData = interactor.getFilmsFromDB(0 , PAGE_SIZE_FROM_DB)
        // Слушаем смену категории в настройках 38*
        interactor.gotoDefaultCategory()

//TODO        preference.currentCategory.observeForever {
            getFilmsPageRx(true)
//        }
    }

    fun getFilmsPageRx(toNextPage :Boolean) {    //Вернем статус запроса из сети для потребителей View
        interactor.getFilmsFromApiRx()
    }

    fun getFilmsSearchRx(searchString: String) : Observable<List<Film>> {
        return interactor.getFilmsSearchFromApi(searchString)
    }

    companion object{
        const val TIME_TO_PURGE_CACH = 600000_000L //Время существование кеша, после удаление из БД в мс
        const val PAGE_SIZE_FROM_DB = 10
    }
}