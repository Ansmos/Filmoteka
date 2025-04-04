package ru.dombuketa.database_module.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notifications", indices = [Index(
    value = ["film_id"],
    unique = true
)])
data class NotificationEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "film_id") val filmId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "start_year") val startYear: Int,
    @ColumnInfo(name = "start_month") val startMonth: Int,
    @ColumnInfo(name = "start_day") val startDay: Int,
    @ColumnInfo(name = "start_hour") val startHour: Int,
    @ColumnInfo(name = "start_minute") val startMinute: Int,
    @ColumnInfo(name = "poster_path") val poster: String,
    @ColumnInfo(name = "is_active", defaultValue = "1") var isActive: Boolean = true
) : Parcelable
