package ru.dombuketa.database_module.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cached_films", indices = [Index(value = ["title"], unique = true)])
data class FilmEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "id_tmdb") val id_tmdb: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_path") val poster: String,
    @ColumnInfo(name = "overview") val description: String,
    @ColumnInfo(name = "release_date") val release_date: String = "",
    @ColumnInfo(name = "vote_average") var rating: Double = 0.0,
    var isInFavorites: Boolean = false
) : Parcelable
