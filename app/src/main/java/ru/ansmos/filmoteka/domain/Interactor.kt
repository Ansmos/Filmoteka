package ru.ansmos.filmoteka.domain

import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ansmos.filmoteka.data.MainRepository
import ru.ansmos.filmoteka.db.*
import ru.ansmos.filmoteka.utils.Converter
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel

class Interactor(private val repo: MainRepository, private val retrofitService: IOmdbApi) {
    fun getFilmsDB(): List<Film> = repo.filmsDataBase
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.IApiCallback) {
        retrofitService.getFilmList(searchString, page, ApiKey.APIKEY).enqueue(object: Callback<OmdbFilmListDTO> {
            override fun onResponse(call: Call<OmdbFilmListDTO>, response: Response<OmdbFilmListDTO>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                callback.onSuccess(Converter.ConverterToFilmList(response.body()?.omdbFilmList))
            }

            override fun onFailure(call: Call<OmdbFilmListDTO>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    companion object{
        private val searchString = "one"
        private val testId = "tt3896198"
    }
}