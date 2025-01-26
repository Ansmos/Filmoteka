package ru.ansmos.filmoteka.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentHomeBinding
import ru.ansmos.filmoteka.databinding.FragmentSettingsBinding
import ru.ansmos.filmoteka.utils.AnimationHelper
import ru.ansmos.filmoteka.viewmodel.SettingsFragmentViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val  viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SettingsFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Подключаем анимации и передаем номер позиции у кнопки в нижнем меню
        AnimationHelper.performFragmentCircularRevealAnimation(binding.settingsFragmentRoot, requireActivity(), 5)
        //Слушаем, какой у нас сейчас выбран вариант в настройках
        viewModel.categoryPropertyLiveData.observe(viewLifecycleOwner, Observer<String>{
            when (it){
                POPULAR_CATEGORY -> binding.radioGroup.check(R.id.radio_popular)
                TOP_RATED_CATEGORY -> binding.radioGroup.check(R.id.radio_top_rated)
                UPCOMING_CATEGORY -> binding.radioGroup.check(R.id.radio_upcoming)
                NOW_PLAYING_CATEGORY -> binding.radioGroup.check(R.id.radio_now_playing)
            }
        })
        //Слушатель для отправки нового состояния в настройки
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId){
                R.id.radio_popular -> viewModel.putCategoryProperty(POPULAR_CATEGORY)
                R.id.radio_top_rated -> viewModel.putCategoryProperty(TOP_RATED_CATEGORY)
                R.id.radio_upcoming -> viewModel.putCategoryProperty(UPCOMING_CATEGORY)
                R.id.radio_now_playing -> viewModel.putCategoryProperty(NOW_PLAYING_CATEGORY)
            }
        }
        //Слушатель на кнопку удаления кеша 39*
        binding.buttonClearCache.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Очистим кеш в БД

                val deletedRecords = viewModel.interactor.clearFilmsInDB()
                Toast.makeText( requireContext(), "Удалено записей - $deletedRecords", Toast.LENGTH_SHORT).show()
                //После очистке кеша инициируем обновление списка на домашнем экране, сначала попытаясь достать данные из сети 39*
                viewModel.interactor.gotoDefaultCategory()
            }
        })
    }

    companion object {
        private const val POPULAR_CATEGORY = "popular"
        private const val TOP_RATED_CATEGORY = "top_rated"
        private const val UPCOMING_CATEGORY = "upcoming"
        private const val NOW_PLAYING_CATEGORY = "now_playing"

    }
}