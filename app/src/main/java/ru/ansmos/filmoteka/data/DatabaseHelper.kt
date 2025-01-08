package ru.ansmos.filmoteka.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {


    override fun onCreate(db: SQLiteDatabase?) {
        //Создаем саму таблицу для фильмов
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COL_TITLE TEXT UNIQUE," +
                    "$COL_POSTER TEXT," +
                    "$COL_DESC TEXT," +
                    "$COL_RATING REAL);"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object{
        private const val DB_NAME = "themovie.db" //Название самой БД
        private const val DB_VER = 1

        const val TABLE_NAME = "films_table"
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_POSTER = "poster_path"
        const val COL_DESC = "overview"
        const val COL_RATING = "vote_average"
    }
}