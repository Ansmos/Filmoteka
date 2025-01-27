package ru.ansmos.filmoteka.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.utils.SingleLiveEvent
import java.net.URL
import java.util.jar.Manifest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailsFragmentViewModel : ViewModel() {
    val isNetworkError = SingleLiveEvent<String>()

    suspend fun loadWallpaper(url: String): Bitmap?{
        return suspendCoroutine {
            val url = URL(url)
            var bitmap: Bitmap? = null
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            it.resume(bitmap)
        }
    }
}