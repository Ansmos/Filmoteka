package ru.ansmos.filmoteka

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
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

        initFabs()
    }

    private fun initFabs() {
        val toolbarlayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout)
        for (i in 0..toolbarlayout.childCount - 1)
        {
            val  v = toolbarlayout.get(i)
            if (v is FloatingActionButton){

                val snackbar = Snackbar.make(findViewById<CoordinatorLayout>(R.id.coordinator_layout),
                    v.accessibilityPaneTitle.toString(), Snackbar.LENGTH_LONG)
                snackbar.setAction("Click"){
                    Toast.makeText(this, v.accessibilityPaneTitle.toString(), Toast.LENGTH_SHORT).show()
                }
                snackbar.setActionTextColor(ContextCompat.getColor(this,R.color.purple_500))
                v.setOnClickListener {
                    snackbar.show()
                }
            }
        }

    }
}