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
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.IApiCallback) {
        retrofitService.getFilmList(getDefaultCategoryFromPreferences(), ApiKey.APIKEY_TMDB, LANGUAGE, page).enqueue(object: Callback<TmdbFilmListDTO> {
            override fun onResponse(call: Call<TmdbFilmListDTO>, response: Response<TmdbFilmListDTO>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                val listFilms = ConverterTmdb.convertApiListToDtoList(response.body()?.tmdbFilmList)
                //Кладем фильмы в бд
                listFilms.forEach{
                    repo.putToDB(it)
                }
                callback.onSuccess(listFilms)
            }

            override fun onFailure(call: Call<TmdbFilmListDTO>, t: Throwable) {
                t.printStackTrace()
                callback.onFailure()
            }
        })
    }

    fun getFilmsFromDB(): List<Film> = repo.getAllFromDB()
    fun clearFilmsInDB()  = repo.clearAll()

    fun getDefaultCategoryFromPreferences() = preferences.getDefCategory()

    fun saveDefaultCategoryToPreferences(category: String) = preferences.saveDefCategory(category)
    //После очистке кеша инициируем обновление списка на домашнем экране, сначала попытаясь достать данные из сети 39*
    fun gotoDefaultCategory() = preferences.currentCategory.postValue(preferences.getDefCategory())

    companion object{
        const val LANGUAGE = "ru-RU"
    }

}