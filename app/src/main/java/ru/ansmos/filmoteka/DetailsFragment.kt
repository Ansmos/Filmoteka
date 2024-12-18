package ru.ansmos.filmoteka

import android.os.Bundle
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ru.ansmos.filmoteka.db.Film

class DetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFabs()
        val film = arguments?.get("film") as Film
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
                snackbar.setActionTextColor(ContextCompat.getColor(requireContext(),R.color.purple_500))
                v.setOnClickListener {
                    snackbar.show()
                }
            }
        }

    }
}