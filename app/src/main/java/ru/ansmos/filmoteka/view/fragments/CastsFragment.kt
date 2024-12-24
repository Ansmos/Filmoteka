package ru.ansmos.filmoteka.view.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.*
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentCastsBinding
import ru.ansmos.filmoteka.db.FilmOMDB
import ru.ansmos.filmoteka.utils.AnimationHelper
import java.io.IOException

class CastsFragment : Fragment() {
    private lateinit var binding : FragmentCastsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentCastsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.casts_fragment_root), requireActivity(), 4)

        binding.btn.setOnClickListener {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://www.omdbapi.com/?i=tt3896198&apikey=49e95b95")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val gson = Gson()
                        val filmOMDB = gson.fromJson(response.body()?.string(), FilmOMDB::class.java)
                        binding.title.text =  filmOMDB.Title
                        context?.let { it1 ->
                            Glide.with(it1.applicationContext)
                                //Загружаем сам ресурс
                                .load(filmOMDB.Poster)

                                //Центруем изображение
                                .centerCrop()
                                //Указываем ImageView, куда будем загружать изображение
                                .into(binding.img)
                        }
                    } catch (e: java.lang.Exception){
                        println(response)
                        e.printStackTrace()
                    }
                }

            })
        }
    }
}