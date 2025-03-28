package ru.ansmos.filmoteka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.bll.Film
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class CastsFragmentViewModel : ViewModel() {

    val showProgressBar : BehaviorSubject<Boolean> //Channel<Boolean> //MutableLiveData<Boolean> = MutableLiveData()
    val showNetworkErrorSnack : BehaviorSubject<Boolean> //MutableLiveData<Boolean> = MutableLiveData()
    var page : Int
    @Inject
    lateinit var preference: PreferenceProvider  //Для онлайн смены контента при смене настройки
    @Inject
    lateinit var interactor: InteractorTmdb
    lateinit var filmPagedListRx : io.reactivex.Observable<PagedList<Film>>
    lateinit var filmDataSourceFactory : DataSource.Factory<Int, Film>

    val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(20)
        .setEnablePlaceholders(false)
        .setPageSize(PAGE_SIZE_FROM_DB)
        .build()

    init{
        App.instance.dagger.injCastsFragment(this)
        showProgressBar = interactor.isProgressBarVisible
        showNetworkErrorSnack = interactor.isNetworkError
        page = interactor.pageNumber

        filmDataSourceFactory = interactor.getFilmsFromDB_Paging()
        filmPagedListRx = RxPagedListBuilder(interactor.getFilmsFromDB_Paging(), config)
            .buildObservable()

        //Если прошло времени больше, чем настроено с последней загрузки, удаляем кеш.
        if (System.currentTimeMillis() - preference.getLastUploadSucsessDateTime() > TIME_TO_PURGE_CACH) {
            CoroutineScope(EmptyCoroutineContext).launch {
                interactor.clearFilmsInDB()
            }
        }
        // Берем из БД то, что есть.
        // Слушаем смену категории в настройках 38*
        interactor.gotoDefaultCategory()
        getFilmsPage(true)
    }

    fun getFilmsPage(toNextPage :Boolean) {    //Вернем статус запроса из сети для потребителей View
        interactor.getFilmsFromApiRx()
    }

    companion object{
        const val TIME_TO_PURGE_CACH = 600000_000L //Время существование кеша, после удаление из БД в мс
        const val PAGE_SIZE_FROM_DB = 10
    }
}
