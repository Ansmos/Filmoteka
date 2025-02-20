package ru.ansmos.filmoteka.view.rw
import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import ru.ansmos.filmoteka.data.FakeRepo
import ru.ansmos.filmoteka.db.Film

class FilmPositionalDataSource(private val fakeRepo: FakeRepo) : PositionalDataSource<Film>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Film>) {
        Log.i("Pagging", "loadInitial, requestedStartPosition = " + params.requestedStartPosition +
                ", requestedLoadSize = " + params.requestedLoadSize)
        val result = fakeRepo.getData(params.requestedStartPosition, params.requestedLoadSize)
        if (params.placeholdersEnabled){
            callback.onResult(result, 0 , result.count())
        } else {
            callback.onResult(result, 0)
        }

    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Film>) {
        Log.i("Pagging", "loadRange, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize)
        val result = fakeRepo.getData(params.startPosition, params.loadSize)
        callback.onResult(result)
    }
}

