package ru.ansmos.filmoteka

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
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
        initAnimationPosters()


    }

    private fun initAnimationPosters() {
        val gridLayout = findViewById<GridLayout>(R.id.grid)
        for (i in 1..gridLayout.childCount - 1) {  //Первый пропускаем
            val v = gridLayout.get(i)
            // Если сюда вставят не CardView, i собъется, усложнять не буду
            if (v is CardView) {
                v.setOnClickListener {
                    when (i){
                        1 -> {
                            val anim1 = ObjectAnimator.ofFloat(it, View.ROTATION_X, 0F, 180F)
                            val anim2 = ObjectAnimator.ofFloat(it, View.ROTATION_X, 180F, 0F)
                            anim1.duration = 500
                            anim2.duration = 500
                            anim1.start()
                            anim2.start()
                        }
                        2 -> {
                            val anim1 = ObjectAnimator.ofFloat(it, View.ROTATION_Y, 180F, 0F)
                            val anim2 = ObjectAnimator.ofFloat(it, View.ROTATION_Y, 180F, 0F)
                            anim1.duration = 500
                            anim2.duration = 500
                            anim1.start()
                            anim2.start()
                        }
                        3 -> {
                            val anim1 = ObjectAnimator.ofFloat(it, View.ALPHA, 1F, 0.5F)
                            val anim2 = ObjectAnimator.ofFloat(it, View.ALPHA, 0.5F, 1F)
                            anim1.duration = 500
                            anim2.duration = 500
                            anim1.start()
                            anim2.start()
                        }
                    }
                }
            }
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
}
