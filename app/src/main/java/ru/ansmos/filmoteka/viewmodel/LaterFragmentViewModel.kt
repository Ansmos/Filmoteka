package ru.ansmos.filmoteka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.data.FakeRepo
import ru.ansmos.filmoteka.data.MainRepository
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
    //lateinit var filmPagedList : LiveData<PagedList<Film>>
    lateinit var filmPagedListRx : Observable<PagedList<Film>>
    lateinit var filmDataSourceFactory : DataSource.Factory<Int, Film>

    //PageListConfig
    val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(20)
        .setEnablePlaceholders(false)
        .setPageSize(PAGE_SIZE_FROM_DB)
        .build()
    val filmDataSourceFactoryFake = FilmDataSourceFactory(FakeRepo())
    //val filmDataSourceFactory = interactor. MainRepository().getDataPDS()



    val infoDataSource = InfoDataSource(InfoStorage())

    init{
        App.instance.dagger.injLaterFragment(this)
        showProgressBar = interactor.isProgressBarVisible
        showNetworkErrorSnack = interactor.isNetworkError
        page = interactor.pageNumber

        filmDataSourceFactory = interactor.getFilmsFromDB_Paging()
        //filmPagedList = filmDataSourceFactory.toLiveData(config)
        //filmPagedListRx = filmDataSourceFactory.toObservable(config)

        //filmPagedListRx = RxPagedListBuilder(filmDataSourceFactory, config).buildObservable()
        filmPagedListRx = RxPagedListBuilder(interactor.getFilmsFromDB_Paging(), config)
            .buildObservable()

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