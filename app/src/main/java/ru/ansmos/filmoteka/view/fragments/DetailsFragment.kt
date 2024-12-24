package ru.ansmos.filmoteka.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.transition.Fade
import androidx.transition.Slide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentDetailsBinding
import ru.ansmos.filmoteka.db.Film

class DetailsFragment : Fragment() {
    private lateinit var binding : FragmentDetailsBinding
    lateinit var film: Film

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
        requireActivity().findViewById<Toolbar>(R.id.details_toolbar).title = film.title
        requireActivity().findViewById<AppCompatImageView>(R.id.details_poster).setImageResource(film.poster)
        requireActivity().findViewById<TextView>(R.id.details_description).text = film.description
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


    }
}