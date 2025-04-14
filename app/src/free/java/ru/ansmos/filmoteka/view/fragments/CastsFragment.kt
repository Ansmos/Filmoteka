package ru.ansmos.filmoteka.view.fragments

import android.icu.text.CaseMap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentCastsBinding
import ru.ansmos.filmoteka.utils.AnimationHelper
import ru.ansmos.filmoteka.view.rw.FilmDiffCallback
import ru.ansmos.filmoteka.view.rw.FilmPaggingAdapter
import ru.ansmos.filmoteka.viewmodel.CastsFragmentViewModel
import ru.ansmos.filmoteka.viewmodel.LaterFragmentViewModel
import java.io.IOException
import kotlin.math.log

class CastsFragment : Fragment() {
    private lateinit var binding : FragmentCastsBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(CastsFragmentViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentCastsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.casts_fragment_root), requireActivity(), 4)
        val filmPagingAdapter = FilmPaggingAdapter(FilmDiffCallback.FILM_COMPARATOR)

        viewModel.filmPagedListRx.subscribe({
            filmPagingAdapter.submitList(it)
            Log.i("paging","records: ${it.positionOffset}")
        },{
            println(it.message)
        })
        //RecyclerView
        binding.castsRv.apply {
            adapter = filmPagingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        if (viewModel.getEvaluatePeriodState()){
            binding.castsRv.visibility = View.VISIBLE
        } else {
            binding.castsRv.visibility = View.GONE
            Toast.makeText(requireContext(),  EVAL_MESSAGE, Toast.LENGTH_LONG).show()
        }
    }

    companion object{
        const val EVAL_MESSAGE = "Пробный период окончился, Напоминания доступны в полнофункциональной версии программы."
    }
}