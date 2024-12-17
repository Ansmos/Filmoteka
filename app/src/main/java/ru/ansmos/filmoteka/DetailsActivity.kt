package ru.ansmos.filmoteka

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import ru.ansmos.filmoteka.db.Film

class DetailsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val film = intent.extras?.get("film") as Film
        setContentView(R.layout.activity_details)
        findViewById<Toolbar>(R.id.details_toolbar).title = film.title
        findViewById<AppCompatImageView>(R.id.details_poster).setImageResource(film.poster)
        findViewById<TextView>(R.id.details_description).text = film.description
    }
}