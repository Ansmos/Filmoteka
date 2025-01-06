package ru.ansmos.filmoteka.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.domain.Interactor
import javax.inject.Inject
import javax.inject.Named

class SettingsFragmentViewModel : ViewModel() {
    //Инжектим интерактор
    @Inject lateinit var interactor: Interactor
    val categoryPropertyLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        App.instance.dagger.injSettingsFragment(this)
        //Получаем категорию при инициализации, чтобы у нас сразу подтягивалась категория
        getCategoryProperty()
    }

    private fun getCategoryProperty() {
        //Кладем категорию в LiveData
        categoryPropertyLiveData.value = interactor.getDefaultCategoryFromPreferences()
    }

    fun putCategoryProperty(category: String){
        //Сохраняем в настройки
        interactor.saveDefaultCategoryToPreferences(category)
        //И сразу забираем, чтобы сохранить состояние в модели
        getCategoryProperty()
    }
}