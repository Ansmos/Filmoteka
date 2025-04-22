package ru.ansmos.filmoteka.view

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.ActivityMainBinding
import ru.ansmos.filmoteka.bll.Film
import ru.ansmos.filmoteka.services.StateEventChecker
import ru.ansmos.filmoteka.view.fragments.*

class MainActivity : AppCompatActivity() {
    var darkMode = AppCompatDelegate.getDefaultNightMode()
    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L
    private lateinit var statteChecker : BroadcastReceiver
    var firstStart: Boolean = true
    var defaultFragmentTag: String = ""
    var previoustFragmentTag: String = ""  //Для фракмента с деталями, неизвестно, из какого фрагмента он вызван


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Передаем его в метод
        setContentView(binding.root)
        //Инициализируем проверщик состояний
        statteChecker = StateEventChecker()
        val intentFilters = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(statteChecker, intentFilters)


        initBottomNavigationView()
        //Запускаем анимацию при старте
        val lottieAnimationView: LottieAnimationView = findViewById(R.id.lottie_anim)
        lottieAnimationView.addAnimatorListener(object : android.animation.Animator.AnimatorListener{
            override fun onAnimationStart(animation: android.animation.Animator?) {
            }
            override fun onAnimationEnd(animation: android.animation.Animator?) {
                lottieAnimationView.visibility = View.GONE
                // Проверяем, если есть фильм в интенте от нотификации
                val filmFromNotification = intent.getParcelableExtra<Film>("film")
                // Если есть, запускаем детальный фрагмент, если нет, - основной
                if (filmFromNotification != null){
                    launchDetailsFragment(filmFromNotification)
                } else {
                    // запускаем фрагмент при окончании анимации
                    val tag = "home"
                    val fragment = checkFragmentExistance(tag)
                    changeFragment(fragment?: HomeFragment(), tag)
                }
            }
            override fun onAnimationCancel(animation: android.animation.Animator?) {
            }
            override fun onAnimationRepeat(animation: android.animation.Animator?) {
            }
        })
        lottieAnimationView.playAnimation()
        //Показ Промо
        showPromo()
    }

    private fun showPromo() {
        if (!App.instance.isPromoShow){
            val remoteConfig = FirebaseRemoteConfig.getInstance()
            val settings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0).build()
            remoteConfig.setConfigSettingsAsync(settings)
            // Метод получения данных с сервера
            remoteConfig.fetch().addOnCompleteListener {
                if (it.isSuccessful){
                    //активируем последний полученный конфиг с сервера
                    remoteConfig.activate()
                    //Получаем ссылку
                    val link = remoteConfig.getString("film_link")
                    if (link.isNotBlank()){
                        App.instance.isPromoShow = true
                        //Включаем верстку
                        binding.promoViewGroup?.apply {
                            visibility = View.VISIBLE
                            //анимируем
                            animate().setDuration(1500).alpha(1f).start()
                            //Загружаем постер
                            setLinkForPoster(link)
                            btn.setOnClickListener {
                                visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
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
        binding.bottomNavigation?.setOnNavigationItemSelectedListener {
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
                R.id.settings ->{
                    val tag = "settings"
                    val fragment = checkFragmentExistance(tag)
                    changeFragment(fragment?: SettingsFragment(), tag)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(statteChecker)
    }

    companion object consts{
        const val TIME_INTERVAL_DBL_CLICK = 2000  //В этот интервал нужно дважды щелкнуть для выхлда. Можно сократить)))
    }
}
