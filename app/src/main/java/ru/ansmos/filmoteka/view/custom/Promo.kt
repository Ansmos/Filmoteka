package ru.ansmos.filmoteka.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.ansmos.filmoteka.databinding.MergePromoBinding
import ru.dombuketa.net_tmdb.ApiConstants

class Promo(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {
    val binding = MergePromoBinding.inflate(LayoutInflater.from(context), this)
    val btn = binding.watchButton

    fun setLinkForPoster(link: String){
        Glide.with(binding.root)
            .load(ApiConstants.IMAGES_URL_TMDB + "w500" + link)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(55)))
            .into(binding.poster)
    }
}