package ru.ansmos.filmoteka.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import java.util.Date

class PreferenceProvider(context: Context) {
    //Нам нужен контекст приложения
    private val appContext = context.applicationContext
    //Создаем экземпляр SharedPreferences
    private val preference: SharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private lateinit var listener: OnSharedPreferenceChangeListener  //Будем прикручивать листенер для обновления набора данных
    var currentCategory = MutableLiveData<String>()

    init {
        //Логика для первого запуска приложения, чтобы положить наши настройки,
        //Сюда потом можно добавить и другие настройки
        if (preference.getBoolean(KEY_FIRST_LAUNCH, false)){
            preference.edit {
                putString(KEY_DEF_CATEGORY, DEF_CATEGORY)
            }
            preference.edit {
                putBoolean(KEY_FIRST_LAUNCH, false)
            }
        }
        initSharedPreferecncesListener()
    }

    private fun initSharedPreferecncesListener() {
        listener = OnSharedPreferenceChangeListener{ sharedPreferences, key ->
            when (key) {
                // Если меняем текущую категорию
                KEY_DEF_CATEGORY -> currentCategory.setValue(getDefCategory())
            }
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
    }

    //Сохраняем категорию
    fun saveDefCategory(category: String){
        preference.edit {
            putString(KEY_DEF_CATEGORY, category)
        }
    }

    //Забираем категорию
    fun getDefCategory(): String {
        return preference.getString(KEY_DEF_CATEGORY, DEF_CATEGORY) ?: DEF_CATEGORY
    }
    //Сохраняем время последней удачной загрузки из сети
    fun saveLastUploadSucsessDateTime(datetime: Long){
        preference.edit {
            putLong(LAST_SUCCESS_UPLOAD, datetime)
        }
    }
    //Получаем время последней удачной загрузки из сети
    fun getLastUploadSucsessDateTime(): Long {
        return preference.getLong(LAST_SUCCESS_UPLOAD, System.currentTimeMillis())
    }


    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        const val KEY_DEF_CATEGORY = "default_category"
        private const val DEF_CATEGORY = "popular"

        private const val LAST_SUCCESS_UPLOAD = "last_success_upload"
    }
}