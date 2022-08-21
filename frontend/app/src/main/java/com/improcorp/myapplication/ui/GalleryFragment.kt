package com.improcorp.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.improcorp.myapplication.databinding.FragmentGalleryBinding
import com.improcorp.myapplication.viewmodel.GalleryViewModel

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentGalleryBinding.inflate(inflater, container, false)

        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        return binding.root
    }
}