package ru.ansmos.filmoteka.db

data class FilmOMDB(
    val Title: String,
    val Poster: String,
    @Transient
    val Actors: String,
    @Transient
    val Awards: String,
    @Transient
    val BoxOffice: String,
    @Transient
    val Country: String,
    @Transient
    val DVD: String,
    @Transient
    val Director: String,
    @Transient
    val Genre: String,
    @Transient
    val Language: String,
    @Transient
    val Metascore: String,
    @Transient
    val Plot: String,
    @Transient
    val Production: String,
    @Transient
    val Rated: String,
    @Transient
    val Ratings: List<Rating>,
    @Transient
    val Released: String,
    @Transient
    val Response: String,
    @Transient
    val Runtime: String,
    @Transient
    val Type: String,
    @Transient
    val Website: String,
    @Transient
    val Writer: String,
    @Transient
    val Year: String,
    @Transient
    val imdbID: String,
    @Transient
    val imdbRating: String,
    @Transient
    val imdbVotes: String
)