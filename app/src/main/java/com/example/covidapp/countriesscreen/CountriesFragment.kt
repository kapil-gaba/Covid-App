package com.example.covidapp.countriesscreen

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covidapp.R
import com.example.covidapp.databinding.FragmentCountriesBinding

class CountriesFragment : Fragment() {



    private val viewModel: CountriesViewModel by lazy {
        ViewModelProviders.of(this).get(CountriesViewModel::class.java)
    }
    private var countriesDataAdapter : CountriesDataAdapter?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

   val binding : FragmentCountriesBinding =
       DataBindingUtil.inflate(inflater,R.layout.fragment_countries,container,false)

        binding.also {
           it.lifecycleOwner = viewLifecycleOwner
           it.viewModel = viewModel
        }

        countriesDataAdapter = CountriesDataAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesDataAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Observing countries live data
        viewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.apply {
                countriesDataAdapter?.countriesData = countries
                //Log.i("CountriesFrag", countries.get(0).country)
            }
        })

    }

}