package ru.ansmos.filmoteka.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceProvider(context: Context) {
    //Нам нужен контекст приложения
    private val appContext = context.applicationContext
    //Создаем экземпляр SharedPreferences
    private val preference: SharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

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

    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEF_CATEGORY = "default_category"
        private const val DEF_CATEGORY = "popular"
    }
}