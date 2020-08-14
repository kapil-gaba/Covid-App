package com.example.covidapp.ui

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.telephony.gsm.GsmCellLocation
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.covidapp.CovidApplication
import com.example.covidapp.R
import com.example.covidapp.ServiceLocator
import com.example.covidapp.databinding.FragmentGlobalBinding
import com.example.covidapp.repository.CoronaCasesRepository
import com.example.covidapp.util.createFactory
import com.example.covidapp.viewmodels.GlobalViewModel

class GlobalFragment : Fragment() {


    private val viewModel: GlobalViewModel by lazy {
         val application = requireNotNull(activity).application
        val viewModelFactory = GlobalViewModel(ServiceLocator.provideCoronaRepository(application)).createFactory()
        ViewModelProviders.of(this,viewModelFactory).get(GlobalViewModel::class.java)
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