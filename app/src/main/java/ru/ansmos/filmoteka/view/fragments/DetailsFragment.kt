package ru.ansmos.filmoteka.view.fragments

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Fade
import androidx.transition.Slide
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentDetailsBinding
import ru.ansmos.filmoteka.bll.Film
import ru.ansmos.filmoteka.viewmodel.DetailsFragmentViewModel

class DetailsFragment : Fragment() {
    private lateinit var binding : FragmentDetailsBinding
    private val  viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailsFragmentViewModel::class.java)
    }
    lateinit var film: Film
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        enterTransition = Slide(Gravity.END).apply { duration = 500 }
        returnTransition = Fade() //Slide(Gravity.END).apply { duration = 500; mode = Slide.MODE_OUT }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        film = arguments?.get("film") as Film
        initFabs()
        binding.detailsToolbar.title = film.title
        Glide.with(this)
            .load(film.poster)
            .centerCrop()
            .into(binding.detailsPoster)
        binding.detailsDescription.text = film.description
    }

    private fun performAsyncLoadOfPoster(){
        //Проверяем есть ли разрешение
        if (!checkPermission()){
            //Если нет, то запрашиваем и выходим из метода
            requestPermission()
            return
        }
        //Создаем обработчик ошибки в Coroutine
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Snackbar.make(binding.root, R.string.m41_network_error, Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setAction("OK"){}
                .show()
        }
        //Создаем родительский скоуп с диспатчером Main потока, так как будем взаимодействовать с UI
        MainScope().launch(exceptionHandler) {
            //Включаем Прогресс-бар
            binding.progressBar.isVisible = true
            //Создаем через async, так как нам нужен результат от работы, то есть Bitmap
            val job = scope.async {
                viewModel.loadWallpaper(ru.dombuketa.net_tmdb.ApiConstants.IMAGES_URL_TMDB + "original" + film.poster)
            }
            //Сохраняем в галерею, как только файл загрузится
            if (job.await() != null) {
                saveToGallery(job.await()!!)
                //Выводим снекбар с кнопкой перейти в галерею
                Snackbar.make(
                    binding.root,
                    R.string.m42_downloaded_to_gallery,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.m42_open) {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.type = "image/*"
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity((intent))
                    }
                    .show()
            }
            //Отключаем прогресс-бар
            binding.progressBar.isVisible = false
        }
    }

    //Узнаем, было ли получено разрешение ранее
    private fun checkPermission(): Boolean{
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }
    //Запрашиваем разрешение
    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf((android.Manifest.permission.WRITE_EXTERNAL_STORAGE)),
            1
        )
    }
    private fun saveToGallery(bitmap: Bitmap){
        //Проверяем версию системы
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            //Создаем объект для передачи данных
            val contentValues = ContentValues().apply {
                //Составляем информацию для файла (имя, тип, дата создания, куда сохранять и т.д.)
                put(MediaStore.Images.Media.TITLE, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.DISPLAY_NAME, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Filmoteka")
            }
            //Получаем ссылку на объект Content resolver, который помогает передавать информацию из приложения вовне
            val contentResolver = requireActivity().contentResolver
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val outputStream = contentResolver.openOutputStream(uri!!)
            //Передаем нашу картинку, может сделать компрессию
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            //Закрываем поток
            outputStream?.close()
        } else{
            //То же, но для более старых версий ОС
            @Suppress("DEPRICATED")
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap,
                film.title.handleSingleQuote(),
                film.description.handleSingleQuote()
            )

        }
    }

    private fun initFabs() {
        val toolbarlayout = requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_layout)
        for (i in 0..toolbarlayout.childCount - 1)
        {
            val  v = toolbarlayout.get(i)
            if (v is FloatingActionButton){

                val snackbar = Snackbar.make(requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_layout),
                    v.accessibilityPaneTitle.toString(), Snackbar.LENGTH_LONG)
                snackbar.setAction("Click"){
                    Toast.makeText(requireContext(), v.accessibilityPaneTitle.toString(), Toast.LENGTH_SHORT).show()
                }
                snackbar.setActionTextColor(ContextCompat.getColor(requireContext(),
                    R.color.purple_500
                ))
                v.setOnClickListener {
                    snackbar.show()
                }
            }
        }

        requireActivity().findViewById<FloatingActionButton>(R.id.fav_fab).apply {
            setImageResource(
                if (film.isInFavorites)
                    R.drawable.ic_favorite_24
                else
                    R.drawable.ic_favorite_border_24
            )
            setOnClickListener {
                if (!film.isInFavorites){
                    (it as FloatingActionButton).setImageResource(R.drawable.ic_favorite_24)
                    film.isInFavorites = true
                } else {
                    (it as FloatingActionButton).setImageResource(R.drawable.ic_favorite_border_24)
                    film.isInFavorites = false
                }
            }
        }
        requireActivity().findViewById<FloatingActionButton>(R.id.share_fab).setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(Intent.EXTRA_TEXT,"Посмотри это: ${film.title} \n\n ${film.description}")
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Отправить к:"))
        }
        requireActivity().findViewById<FloatingActionButton>(R.id.details_fab_download_wp).setOnClickListener {
            performAsyncLoadOfPoster()
        }
    }
}

private fun String.handleSingleQuote(): String = this.replace("'", "")



