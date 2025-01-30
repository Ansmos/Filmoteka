package ru.ansmos.filmoteka.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ansmos.filmoteka.data.MainRepository
import ru.ansmos.filmoteka.db.*
import ru.ansmos.filmoteka.utils.ConverterRoom
import ru.ansmos.filmoteka.utils.ConverterTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel

class InteractorTmdb(private val repo: MainRepository, private val retrofitService: IThemoviedbApi, private val preferences: PreferenceProvider) {
    val scope = CoroutineScope(Dispatchers.IO)
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    var isProgressBarVisible = Channel<Boolean>(Channel.CONFLATED)
    var isNetworkError = Channel<Boolean>(Channel.CONFLATED)
    var pageNumber = Channel<Int>(Channel.CONFLATED)

    init {
        scope.launch { pageNumber.send(1) }
    }

    fun getFilmsFromApi(page: Int) {
        var pageNoCoroutine = 0
        scope.launch {
            pageNoCoroutine = pageNumber.receive()
            pageNumber.send(pageNoCoroutine)
            isProgressBarVisible.send(true)
            isNetworkError.send(false)
        }
        retrofitService.getFilmList(getDefaultCategoryFromPreferences(), ApiKey.APIKEY_TMDB, LANGUAGE, pageNoCoroutine).enqueue(object: Callback<TmdbFilmListDTO> {
            override fun onResponse(call: Call<TmdbFilmListDTO>, response: Response<TmdbFilmListDTO>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                val listFilms = ConverterTmdb.convertApiListToDtoList(response.body()?.tmdbFilmList)
                //Кладем фильмы в бд
                repo.putFilms(ConverterRoom.convertFilmsToEntity(listFilms))
                preferences.saveLastUploadSucsessDateTime(System.currentTimeMillis())
                //41 callback.onSuccess(listFilms)
                scope.launch {
                    isProgressBarVisible.send(false)
                    isNetworkError.send(false)
                    val n = ++pageNoCoroutine
                    pageNumber.send(n)
                }
            }

            override fun onFailure(call: Call<TmdbFilmListDTO>, t: Throwable) {
                t.printStackTrace()
                scope.launch {
                    isProgressBarVisible.send(false)
                    isNetworkError.send(true)
                    pageNumber.send(pageNoCoroutine)
                    Log.d("interactor", "Error get page $page from INET - Get from DB")

                }
            }
        })
    }


    //fun getFilmsFromDB(pageIndex: Int, pageSize: Int): List<Film> = ConverterRoom.convertEntityToFilms(repo.getFilmsFromDB(pageIndex, pageSize))
    fun getFilmsFromDB(pageIndex: Int, pageSize: Int): Flow<List<Film>> {
        // Page в Api начинается с 1, в БД с 0
        // Берем все записи пока не сделали пагинацию.
        val data = repo.getFilms(0, Int.MAX_VALUE)
        return ConverterRoom.convertFlowEntityToFilms(data)
    }
    fun clearFilmsInDB() : Int  = repo.clearAllFilms()

    fun getDefaultCategoryFromPreferences() = preferences.getDefCategory()

    fun saveDefaultCategoryToPreferences(category: String) = preferences.saveDefCategory(category)
    //После очистке кеша инициируем обновление списка на домашнем экране, сначала попытаясь достать данные из сети 39*
    fun gotoDefaultCategory() = preferences.currentCategory.postValue(preferences.getDefCategory())

    companion object{
        const val LANGUAGE = "ru-RU"
    }

}