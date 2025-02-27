package ru.ansmos.filmoteka.view.fragments


import android.annotation.SuppressLint
//import android.arch.paging.PagedList
//import android.arch.paging.PagedList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.paging.PagedList
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.data.FakeRepo
import ru.ansmos.filmoteka.databinding.FragmentLaterBinding
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.utils.AnimationHelper
import ru.ansmos.filmoteka.view.rw.*
import ru.ansmos.filmoteka.viewmodel.HomeFragmentViewModel
import ru.ansmos.filmoteka.viewmodel.LaterFragmentViewModel
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class LaterFragment : Fragment() {
    private lateinit var binding : FragmentLaterBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(LaterFragmentViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaterBinding.inflate(inflater, container, false)
        return binding.root
    }
//    @SuppressLint("WrongThread")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.later_fragment_root), requireActivity(), 3)
        //DataSource
        //val positionalDataSource = FilmPositionalDataSource(FakeRepo())





        //val pagedListInfo: PagedList<Info> = PagedList.Builder<>(infoDataSource, config)


        //Adapter
        val filmPagingAdapter = FilmPaggingAdapter(FilmDiffCallback.FILM_COMPARATOR)
//        viewModel.filmPagedList.observe(viewLifecycleOwner, Observer<PagedList<Film>>{
//            filmPagingAdapter.submitList(it)
//        })
        viewModel.filmPagedListRx.subscribe({
            filmPagingAdapter.submitList(it)
            Log.i("paging","records: ${it.positionOffset}")
        },{
            println(it.message)
        })
//        viewModel.filmPagedList.observe(viewLifecycleOwner, Observer<PagedList<Film>>{
//            PagedList(filmPagingAdapter::submitList)
//        })
            //adapter.submitList(pagedList)
            val adapterInfo = InfoAdapter(InfoDiffer.COMPARATOR)
            //RecyclerView
            binding.laterRv.apply {
                adapter = filmPagingAdapter
                layoutManager = LinearLayoutManager(requireContext())

            }
        }


}

