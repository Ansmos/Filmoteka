package ru.ansmos.filmoteka.view.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentCastsBinding
import ru.ansmos.filmoteka.db.FilmOMDB
import ru.ansmos.filmoteka.db.IFilmOMDB
import ru.ansmos.filmoteka.utils.AnimationHelper
import java.io.IOException
import kotlin.math.log

class CastsFragment : Fragment() {
    private lateinit var binding : FragmentCastsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentCastsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.casts_fragment_root), requireActivity(), 4)

        binding.btn.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(IFilmOMDB::class.java)
            service.getFilm("tt3896198","49e95b95").enqueue(object : retrofit2.Callback<FilmOMDB>{
                override fun onResponse(call: Call<FilmOMDB>, response: Response<FilmOMDB>) {
                     binding.title.text = response.body()?.Title ?: "нет данных"
                    context?.let { it1 ->
                        Glide.with(it1)
                            .load(response.body()?.Poster)
                            .centerCrop()
                            .into(binding.img)
                    }
                }

                override fun onFailure(call: Call<FilmOMDB>, t: Throwable) {
                     t.printStackTrace()
                }
            })
        }
    }
}