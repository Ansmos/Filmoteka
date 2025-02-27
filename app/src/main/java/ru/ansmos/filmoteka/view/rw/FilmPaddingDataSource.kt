package ru.ansmos.filmoteka.view.rw

//import android.arch.paging.PositionalDataSource
import android.util.Log
import androidx.paging.PositionalDataSource

import ru.ansmos.filmoteka.data.FakeRepo
import ru.ansmos.filmoteka.data.IRepo
import ru.ansmos.filmoteka.db.Film

class FilmPositionalDataSource1(private val repo: IRepo): PositionalDataSource<Film>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Film>) {
        Log.i("PosDataSource", "loadInitial, requestedStartPosition = ${params.requestedStartPosition}" +
                ", requestedLoadSize = ${params.requestedLoadSize}")
        val fakeRepo = FakeRepo()
        val result = fakeRepo.getData(params.requestedStartPosition, params.requestedLoadSize)
        callback.onResult(result, 0)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Film>) {
        Log.i("PosDataSource", "loadRange, startPosition = ${params.startPosition}" +
        ", loadsize = ${params.loadSize}")
        val fakeRepo = FakeRepo()
        val result = fakeRepo.getData(params.startPosition, params.loadSize)
        callback.onResult(result)
    }

}