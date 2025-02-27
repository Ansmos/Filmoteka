package ru.ansmos.filmoteka.view.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentLaterBinding
import ru.ansmos.filmoteka.utils.AnimationHelper
import ru.ansmos.filmoteka.view.rw.*
import ru.ansmos.filmoteka.viewmodel.LaterFragmentViewModel

class LaterFragment : Fragment() {
    private lateinit var binding : FragmentLaterBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(LaterFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.later_fragment_root), requireActivity(), 3)
        val filmPagingAdapter = FilmPaggingAdapter(FilmDiffCallback.FILM_COMPARATOR)

        viewModel.filmPagedListRx.subscribe({
            filmPagingAdapter.submitList(it)
            Log.i("paging","records: ${it.positionOffset}")
        },{
            println(it.message)
        })
        //RecyclerView
        binding.laterRv.apply {
            adapter = filmPagingAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }
    }
}

