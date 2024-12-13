package ru.ansmos.filmoteka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var darkMode = AppCompatDelegate.getDefaultNightMode()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTopAppBar()
        initBottomNavigationView()
    }

    private fun initTopAppBar() {
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            Toast.makeText(this, "Когда-нибудь здесь будет навигация...", Toast.LENGTH_SHORT).show()
        }
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, resources.getString(R.string.m20_btn_menu_settings), Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.night -> {
                    darkMode =  if (darkMode != AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    AppCompatDelegate.setDefaultNightMode(darkMode)
                    true
                }
                else -> false
            }
        }
    }

    fun initBottomNavigationView(){
        val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fav -> {
                    Toast.makeText(this, resources.getString(R.string.m20_btn_menu_fav), Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.later -> {
                    Toast.makeText(this, resources.getString(R.string.m20_btn_menu_later), Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.casts -> {
                    Toast.makeText(this, resources.getString(R.string.m20_btn_menu_casts), Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }    }
}