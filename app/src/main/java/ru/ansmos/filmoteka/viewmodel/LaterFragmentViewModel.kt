package ru.ansmos.filmoteka.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import io.reactivex.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.data.FakeRepo
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import ru.ansmos.filmoteka.view.rw.FilmDataSourceFactory
import ru.ansmos.filmoteka.view.rw.InfoDataSource
import ru.ansmos.filmoteka.view.rw.InfoStorage
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class LaterFragmentViewModel: ViewModel() {

    //val filmListRxData : Observable<List<Film>>
    val showProgressBar : BehaviorSubject<Boolean> //Channel<Boolean> //MutableLiveData<Boolean> = MutableLiveData()
    val showNetworkErrorSnack : BehaviorSubject<Boolean> //MutableLiveData<Boolean> = MutableLiveData()
    var page : Int
    @Inject lateinit var preference: PreferenceProvider  //Для онлайн смены контента при смене настройки
    @Inject lateinit var interactor: InteractorTmdb
    lateinit var filmPagedList : LiveData<PagedList<Film>>
    lateinit var filmPagedListRx : Observable<PagedList<Film>>

    //PageListConfig
    val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(10)
        .build()
    val filmDataSourceFactory = FilmDataSourceFactory(FakeRepo())

    val infoDataSource = InfoDataSource(InfoStorage())
    //PagedList




/*
    val dao = ITmdbFilmDao.getFilmsByPage()

    val dao : LiveData<PagedList<Film>> = ITmdbFilmDao.getFilmsByPage_Paging().
    val filmList: LiveData<PagedList<Film>> = FakeRepo().getDataDSFactory().toLiveData(page)
    val filmListDAO: LiveData<PagedList<Film>> = FakeRepo().getDataDSFactory().toLiveData(page)
    val filmListDAORx: Observable<PagedList<Film>> = FakeRepo().getDataDSFactory().toLiveData(page)
*/

    init{
        App.instance.dagger.injLaterFragment(this)
        showProgressBar = interactor.isProgressBarVisible
        showNetworkErrorSnack = interactor.isNetworkError
        page = interactor.pageNumber

        filmPagedList = filmDataSourceFactory.toLiveData(config)
        //filmPagedListRx = filmDataSourceFactory.toObservable(config)

        //Если прошло времени больше, чем настроено с последней загрузки, удаляем кеш.
        if (System.currentTimeMillis() - preference.getLastUploadSucsessDateTime() > TIME_TO_PURGE_CACH) {
            CoroutineScope(EmptyCoroutineContext).launch {
                interactor.clearFilmsInDB()
            }
        }
        // Берем из БД то, что есть.
        //filmListRxData = interactor.getFilmsFromDB(0 , PAGE_SIZE_FROM_DB)
        // Слушаем смену категории в настройках 38*
        interactor.gotoDefaultCategory()


//TODO        preference.currentCategory.observeForever {
            getFilmsPage(true)
//        }
    }

    fun getFilmsPage(toNextPage :Boolean) {    //Вернем статус запроса из сети для потребителей View
        interactor.getFilmsFromApi()
    }

    companion object{
        const val TIME_TO_PURGE_CACH = 600000_000L //Время существование кеша, после удаление из БД в мс
        const val PAGE_SIZE_FROM_DB = 10
    }
}