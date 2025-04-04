package ru.ansmos.filmoteka.domain

import android.util.Log
import androidx.paging.DataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.filmoteka.bll.*
import ru.ansmos.filmoteka.utils.ConverterRoom
import ru.ansmos.filmoteka.utils.ConverterTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import ru.dombuketa.net_tmdb.ApiKey
import java.util.concurrent.Executors

class InteractorTmdb(private val repo: ru.dombuketa.database_module.repositories.MainRepository, private val retrofitService: ru.dombuketa.net_tmdb.api.IThemoviedbApi, private val preferences: PreferenceProvider) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    var isProgressBarVisible = BehaviorSubject.create<Boolean>()
    var isNetworkError = BehaviorSubject.create<Boolean>()
    var pageNumber = 1

    fun getFilmsFromApiRx() {
        isProgressBarVisible.onNext(true)
        isNetworkError.onNext(false)
        retrofitService.getFilmListRx(getDefaultCategoryFromPreferences(), ApiKey.APIKEY_TMDB, LANGUAGE, pageNumber)
            .subscribeOn(Schedulers.io())
            .map {
                ConverterTmdb.convertApiListToDtoList(it.tmdbFilmList)
            }
            .subscribe(
                {
                    Log.i("PutToDB", "interactor - put to db success.")
                    repo.putFilms(ConverterRoom.convertFilmListToEntity(it))
                    preferences.saveLastUploadSucsessDateTime(System.currentTimeMillis())
                    isProgressBarVisible.onNext(false)
                    isNetworkError.onNext(false)
                    ++pageNumber
                },
                {
                    Log.d("interactor", "Error get page $pageNumber from INET - Get from DB")
                    it.printStackTrace()
                    isProgressBarVisible.onNext(false)
                    isNetworkError.onNext(true)
                }
            )
    }

    fun getFilmFromAPI(id: Int) : Observable<Film> {
        isProgressBarVisible.onNext(true)
        return retrofitService.getFilm(id, ApiKey.APIKEY_TMDB, "ru-RU")
            .subscribeOn(Schedulers.io())
            .map {
                isProgressBarVisible.onNext(false)
                ConverterTmdb.convertApiToDto(it)
            }
            .doOnError { isProgressBarVisible.onNext(false) }
    }

    fun getFilmsSearchFromApi(searchString: String): Observable<List<Film>> {
        return retrofitService.getFilmsFromSearch(ru.dombuketa.net_tmdb.ApiKey.APIKEY_TMDB, LANGUAGE, searchString, pageNumber)
            .map {
                ++pageNumber
                ConverterTmdb.convertApiListToDtoList(it.tmdbFilmList)
            }
    }

    fun getFilmsFromDB(pageIndex: Int, pageSize: Int): Observable<List<Film>> {
        // Page в Api начинается с 1, в БД с 0
        // Берем все записи пока не сделали пагинацию.
        val data = repo.getFilms(0, Int.MAX_VALUE)
        return ConverterRoom.convertRxEntityToFilmList(data)
    }

    fun getFilmsFromDB_Paging(): DataSource.Factory<Int, Film> {
        val data = repo.getFilmsPaging()
        return ConverterRoom.convertPagingEntityToFilmList(data)
    }

    fun clearFilmsInDB() : Int  = repo.clearAllFilms()

    fun getDefaultCategoryFromPreferences() = preferences.getDefCategory()

    fun saveDefaultCategoryToPreferences(category: String) = preferences.saveDefCategory(category)
    //После очистке кеша инициируем обновление списка на домашнем экране, сначала попытаясь достать данные из сети 39*
    fun gotoDefaultCategory() = preferences.currentCategory.postValue(preferences.getDefCategory())


// Нотификации **************************************************

    fun getNotifications(): Observable<List<Notification>> {
        return ConverterRoom.convertRxEntityToNotifications(repo.getAllNotifications())
    }

    fun getNotificationById(id: Int) : Single<Notification>? {
        return repo.getNotificationById(id)
            ?.subscribeOn(Schedulers.io())
            ?.map {
                ConverterRoom.convertEntityToNotification(it)
            }
    }

    fun updateNotification(notification: Notification) {
        Single.just(notification)
            .observeOn(Schedulers.io())
            .map {
                ConverterRoom.convertNotificationToEntity(notification)
            }
            .subscribe( {
                repo.updateNotification(it)
                println("!!! Нотификация Обновлена в БД")
            },{
                println("!!! ОШИБКА: Нотификация не обновлена в БД" + it.message)
            })
    }

    fun cancelNotification(film_id: Int){
        Single.just(true)
            .observeOn(Schedulers.io())
            .subscribe( {
                repo.cancelNotification(film_id)
                println("!!! Нотификация отмененав в БД")
            },{
                println("!!! ОШИБКА: Нотификация не отменена БД" + it.message)
            })
    }

    companion object{
        const val LANGUAGE = "ru-RU"
    }

}