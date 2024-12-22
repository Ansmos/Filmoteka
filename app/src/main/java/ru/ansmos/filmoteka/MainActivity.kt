package ru.ansmos.filmoteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.request.transition.ViewPropertyTransition.Animator
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator
import ru.ansmos.filmoteka.rw.FilmAdapter
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    var darkMode = AppCompatDelegate.getDefaultNightMode()
    private var backPressed = 0L
    lateinit var filmsDataBase : List<Film>
    var firstStart: Boolean = true
    var defaultFragmentTag: String = ""
    var previoustFragmentTag: String = ""  //Для фракмента с деталями, неизвестно, из какого фрагмента он вызван

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB()
        initBottomNavigationView()
        //Запускаем анимацию при старте
        val lottieAnimationView: LottieAnimationView = findViewById(R.id.lottie_anim)
        lottieAnimationView.addAnimatorListener(object : android.animation.Animator.AnimatorListener{
            override fun onAnimationStart(animation: android.animation.Animator?) {
            }
            override fun onAnimationEnd(animation: android.animation.Animator?) {
                lottieAnimationView.visibility = View.GONE
                // запускаем фрагмент при окончании анимации
                changeFragment(HomeFragment(), "home")
            }
            override fun onAnimationCancel(animation: android.animation.Animator?) {
            }
            override fun onAnimationRepeat(animation: android.animation.Animator?) {
            }
        })
        lottieAnimationView.playAnimation()
    }

    private fun checkFragmentExistance(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(null).commit()
        previoustFragmentTag = defaultFragmentTag
        defaultFragmentTag = tag
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
        previoustFragmentTag = defaultFragmentTag
        defaultFragmentTag = "details"
    }

    override fun onBackPressed() {
        Log.i("back", supportFragmentManager.backStackEntryCount.toString() + " - " + defaultFragmentTag + " : " + previoustFragmentTag)

        if (defaultFragmentTag == "home"){
            //if (supportFragmentManager.backStackEntryCount >= 1) // Оставил на память, не ругаться.
                // Если уложильсь в TIME_INTERVAL_DBL_CLICK, то покажем диалог
                if (backPressed + TIME_INTERVAL_DBL_CLICK > System.currentTimeMillis()) {
                    AlertDialog.Builder(this)
                        .setTitle("Вы хотите выйти?")
                        .setIcon(R.drawable.ic_baseline_question_24)
                        .setPositiveButton("Да") { _, _ ->
                            finishAndRemoveTask()
                        }
                        .setNegativeButton("Нет") { _, _ ->
                        }
                        .show()
                } else {
                    Toast.makeText(applicationContext, R.string.m25_exit_dbl_click, Toast.LENGTH_SHORT).show()
                }
                //Запомним время предудущего клика
                backPressed = System.currentTimeMillis()
        }
        if (defaultFragmentTag == "details") {
            super.onBackPressed()
            // Возвращаем тот фрагмент, из которого был вызван details
            defaultFragmentTag = previoustFragmentTag
        }

    }

    fun initBottomNavigationView() {
        val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home ->{
                    val tag = "home"
                    val fragment = checkFragmentExistance(tag)
                    changeFragment(fragment?: HomeFragment(), tag)
                    true
                }
                R.id.fav -> {
                    val tag = "fav"
                    val fragment = checkFragmentExistance(tag)
                    changeFragment(fragment?: FavoriteFragment(), tag)
                    true
                }
                R.id.later -> {
                    val tag = "later"
                    val fragment = checkFragmentExistance(tag)
                    changeFragment(fragment?: LaterFragment(), tag)
                    true
                }
                R.id.casts -> {
                    val tag = "casts"
                    val fragment = checkFragmentExistance(tag)
                    changeFragment(fragment?: CastsFragment(), tag)
                    true
                }
                else -> false
            }
        }
    }

    private fun initDB() {
        filmsDataBase = listOf<Film>(
            Film(1,"Первый", R.drawable.poster_1,"В целом все должно быть понятно, за исключением, может, поля poster. Там мы будем хранить id картинки в ресурсах, а как вы помните, они у нас в Int. Теперь наступает «творческая» часть: нужно подготовить 7–10 фильмов"),
            Film(2,"Второй", R.drawable.poster_2,"Смысл такой: когда проект только начинается, вы сразу делаете новую ветку от master → ветку develop и от неё уже делаете ветки с новым функционалом (feature) или исправлением неработающего кода (bugfix).", 1.5f),
            Film(3,"Третий", R.drawable.poster_3,"После того как вы сделали свою feature , вы это мержите в develop (после ревью начальника, когда будете работать, в нашем случае, желательно, после проверки ментора).", 9f, true),
            Film(4,"Четвертый", R.drawable.poster_4,"И вот таким образом вы создаёте приложение, пока не будет понимания, что можно его показать людям, и вы тогда создаёте release ветку (ставится ещё соответствующий тэг, но на этом сейчас останавливаться не будем), мержите изменения в неё (в случае с коммерческой разработкой в этом моменте она передаётся на тестирование) и потом уже мержите в master."),
            Film(5,"Пятый", R.drawable.poster_5,"Осталось все наши предыдущие ветки с проектными заданиями смержить в develop (если у вас их проверили, конечно), придерживайтесь очередности их создания. "),
            Film(6,"Шестой", R.drawable.poster_6,"Нам нужно создать RecyclerView для главной страницы с фильмами (пока у нас нет данных по фильмам из сети, будем использовать mock-данные, то есть вручную создадим нашу БД с фильмами (7 - 10 штук))."),
            Film(7,"Седьмой", R.drawable.poster_7,"Начнём мы не с этого. А с темы системы контроля версий. Когда вы успешно пройдете курс, успешно пройдете собеседование и получите работу, вы столкнетесь с тем, что в разработке, к сожалению, не все так просто, как хотелось бы, и там недостаточно просто делать ветки, коммиты и мержи."),
            Film(8,"Восьмой", R.drawable.poster_8,"Мера это вынужденная, ведь над проектом может работать сколько угодно человек, и надо все это как-то структурировать. Не будем углубляться в глубокие глубины, у вас и так сейчас голова в режиме терминатора работает."),
            Film(9,"Девятый", R.drawable.poster_9,"Всё должно быть понятно, возможно, будет в новинку атрибут tools: он нужен для того, чтобы были какие-то представления на момент верстки, чтобы визуально легче было ориентироваться. ")
        )
    }

    companion object consts{
        const val TIME_INTERVAL_DBL_CLICK = 2000  //В этот интервал нужно дважды щелкнуть для выхлда. Можно сократить)))
    }
}
