package ru.ansmos.filmoteka.domain

import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ansmos.filmoteka.data.MainRepository
import ru.ansmos.filmoteka.db.*
import ru.ansmos.filmoteka.utils.Converter
import ru.ansmos.filmoteka.utils.ConverterTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel

class InteractorTmdb(private val repo: MainRepository, private val retrofitService: IThemoviedbApi, private val preferences: PreferenceProvider) {
    fun getFilmsDB(): List<Film> = repo.filmsDataBase
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.IApiCallback) {
        retrofitService.getFilmList(getDefaultCategoryFromPreferences(), ApiKey.APIKEY_TMDB, LANGUAGE, page).enqueue(object: Callback<TmdbFilmListDTO> {
            override fun onResponse(call: Call<TmdbFilmListDTO>, response: Response<TmdbFilmListDTO>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                callback.onSuccess(ConverterTmdb.convertApiListToDtoList(response.body()?.tmdbFilmList))
            }

            override fun onFailure(call: Call<TmdbFilmListDTO>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getDefaultCategoryFromPreferences() = preferences.getDefCategory()

    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefCategory(category)
    }

    companion object{
        const val LANGUAGE = "ru-RU"
    }

}