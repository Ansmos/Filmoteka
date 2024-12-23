package ru.ansmos.filmoteka.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.databinding.FragmentCastsBinding
import ru.ansmos.filmoteka.utils.AnimationHelper

class CastsFragment : Fragment() {
    private lateinit var binding : FragmentCastsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentCastsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.casts_fragment_root), requireActivity(), 4)
    }
}