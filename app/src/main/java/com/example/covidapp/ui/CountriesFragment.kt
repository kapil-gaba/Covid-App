package com.example.covidapp.ui

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covidapp.R
import com.example.covidapp.util.CountriesDataAdapter
import com.example.covidapp.viewmodels.CountriesViewModel
import com.example.covidapp.databinding.FragmentCountriesBinding

class CountriesFragment : Fragment() {


    private val viewModel: CountriesViewModel by lazy {
        ViewModelProviders.of(this).get(CountriesViewModel::class.java)
    }
    private var countriesDataAdapter: CountriesDataAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentCountriesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_countries, container, false)

        binding.also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        countriesDataAdapter =
            CountriesDataAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesDataAdapter
        }

        binding.countrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.countrySearch.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.setUserSearchQuery(it)
                }

                return false
            }

        })

        //customise colors of searchview
        val searchIcon = binding.countrySearch.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.WHITE)

        val cancelIcon = binding.countrySearch.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.WHITE)

        val textView = binding.countrySearch.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(Color.WHITE)
        textView.setHintTextColor(Color.WHITE)



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