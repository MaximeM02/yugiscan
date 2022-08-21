package com.improcorp.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.improcorp.myapplication.databinding.FragmentSlideshowBinding
import com.improcorp.myapplication.viewmodel.SlideshowViewModel

class SlideshowFragment : Fragment() {

    private lateinit var binding: FragmentSlideshowBinding
    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false)

        slideshowViewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)

        return binding.root
    }
}