package ru.ansmos.filmoteka.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.bll.Film
import ru.ansmos.filmoteka.bll.Notification
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class LaterFragmentViewModel: ViewModel() {
    lateinit var notificationsRx : Observable<List<Notification>>
    @Inject lateinit var interactor: InteractorTmdb
    val showProgressBar : BehaviorSubject<Boolean> //Channel<Boolean> //MutableLiveData<Boolean> = MutableLiveData()
    val showNetworkErrorSnack : BehaviorSubject<Boolean> //MutableLiveData<Boolean> = MutableLiveData()

    init{
        App.instance.dagger.injLaterFragment(this)
        showProgressBar = interactor.isProgressBarVisible
        showNetworkErrorSnack = interactor.isNetworkError
        notificationsRx = interactor.getNotifications()
    }

    fun getFilm(id: Int) : Observable<Film> {
        return interactor.getFilmFromAPI(id)
    }

    fun getNotification(id: Int) : Single<Notification>?{
            return interactor.getNotificationById(id)
    }
}