package com.example.covidapp.countriesscreen

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.covidapp.R
import com.example.covidapp.databinding.CountryItemBinding
import com.example.covidapp.network.CountriesData

// Recyclerview Adapter for attaching data
class CountriesDataAdapter : RecyclerView.Adapter<CountryInfoViewHolder>() {


    var countriesData: List<CountriesData> = emptyList()
        set(value) {
            field = value

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryInfoViewHolder {
        val withDataBinding: CountryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CountryInfoViewHolder.LAYOUT,
            parent,
            false
        )
        return CountryInfoViewHolder(withDataBinding)
    }

    override fun getItemCount() = countriesData.size


    override fun onBindViewHolder(holder: CountryInfoViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.countriesData = countriesData[position]
            Log.i("DataAdapter", countriesData[position].country)
        }
    }

}


//ViewHolder for countrydataitem
class CountryInfoViewHolder(val viewDataBinding: CountryItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.country_item
    }
}