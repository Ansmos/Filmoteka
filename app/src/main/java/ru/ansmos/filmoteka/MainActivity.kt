package ru.ansmos.filmoteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator
import ru.ansmos.filmoteka.rw.FilmAdapter

class MainActivity : AppCompatActivity() {
    var darkMode = AppCompatDelegate.getDefaultNightMode()
    private var backPressed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTopAppBar()
        initBottomNavigationView()
        //Запускаем фрагмент при старте
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, HomeFragment())
            .addToBackStack(null).commit()
    }

    fun launchDetailsFragment(film: Film){
        //Создаем "посылку"
        val bundle = Bundle()
        bundle.putParcelable("film", film)
        val fragment = DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle
        //Запускаем фрагмент
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            // Если уложильсь в TIME_INTERVAL_DBL_CLICK, то покажем диалог
            if (backPressed + TIME_INTERVAL_DBL_CLICK > System.currentTimeMillis()) {
                AlertDialog.Builder(this)
                    .setTitle("Вы хотите выйти?")
                    .setIcon(R.drawable.ic_baseline_question_24)
                    .setPositiveButton("Да") { _, _ ->
                        finish()
                    }
                    .setNegativeButton("Нет") { _, _ ->
                    }
                    .show()
            } else {
                Toast.makeText(applicationContext, R.string.m25_exit_dbl_click, Toast.LENGTH_SHORT).show()
            }
            //Запомним время предудущего клика
            backPressed = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }

    }

    private fun initTopAppBar() {
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            Toast.makeText(this, "Когда-нибудь здесь будет навигация...", Toast.LENGTH_SHORT).show()
        }
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.m20_btn_menu_settings),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                R.id.night -> {
                    darkMode =
                        if (darkMode != AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    AppCompatDelegate.setDefaultNightMode(darkMode)
                    true
                }
                else -> false
            }
        }
    }

    fun initBottomNavigationView() {
        val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fav -> {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.m20_btn_menu_fav),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                R.id.later -> {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.m20_btn_menu_later),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                R.id.casts -> {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.m20_btn_menu_casts),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                else -> false
            }
        }
    }

    companion object consts{
        const val TIME_INTERVAL_DBL_CLICK = 2000  //В этот интервал нужно дважды щелкнуть для выхлда. Можно сократить)))
    }
}
