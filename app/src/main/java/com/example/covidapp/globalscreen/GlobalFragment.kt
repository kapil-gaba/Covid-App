package com.example.covidapp.globalscreen

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.covidapp.R
import com.example.covidapp.databinding.FragmentGlobalBinding

class GlobalFragment : Fragment() {


    private val viewModel: GlobalViewModel by lazy {
        ViewModelProviders.of(this)
            .get(GlobalViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentGlobalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_global, container, false)

        // Setting lifecycleOwner so DataBinding can observe LiveData
        binding.also {
            it.lifecycleOwner = viewLifecycleOwner
            it.globalViewModel = viewModel
        }

        return binding.root
    }


}