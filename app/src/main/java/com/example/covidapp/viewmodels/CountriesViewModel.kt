package com.example.covidapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidapp.domain.CountriesData
import com.example.covidapp.network.NetworkCountriesData
import com.example.covidapp.network.countriesDataService
import com.example.covidapp.network.convertToCountriesDataFromNetwork
import com.example.covidapp.repository.CoronaCasesRepository
import com.example.covidapp.repository.CoronaRepository
import com.example.covidapp.util.ApiStatus
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class CountriesViewModel(private val coronaRepository : CoronaRepository) : ViewModel() {


    private var disposable = CompositeDisposable()
    private val publishSubject =
        PublishSubject.create<String>()
    private val dataObserver: DisposableObserver<List<NetworkCountriesData>> = getDataObserver()
    private val searchObserver: DisposableObserver<List<NetworkCountriesData>> = getSearchObserver()
    private val _countries = MutableLiveData<List<CountriesData>>()
    val countries: LiveData<List<CountriesData>>
        get() = _countries
    private var databaseNotContainData = true

    // For api status
    private val _countriesApiStatus = MutableLiveData<ApiStatus>()
    val countriesApiStatus: LiveData<ApiStatus>
        get() = _countriesApiStatus

    init {
        getCountriesList()
    }

    private fun singleObservable() = countriesDataService.getCountries()
        .toObservable()
        .doOnError { throwable ->
            Log.i("CountriesViewModel", throwable.message)
        }.onErrorReturn { throwable ->
            ArrayList()
        }
        .map { countryDataList ->
            if (countryDataList.isNotEmpty()) {
                coronaRepository.refreshCountryData(countryDataList)
            }
            Log.i("CountriesViewModel", "map")
            return@map countryDataList
        }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun setUserSearchQuery(query: String) {
        Log.i("CountriesViewModel", query)
        publishSubject.onNext(query)
    }

    private fun getDataObserver(): DisposableObserver<List<NetworkCountriesData>> {
        return object : DisposableObserver<List<NetworkCountriesData>>() {

            override fun onNext(countriesList: List<NetworkCountriesData>) {

                if (countriesList.isEmpty()) {

                    if (databaseNotContainData) {
                        _countriesApiStatus.value = ApiStatus.ERROR
                        Log.i("CountriesViewModel", "error")
                    }


                } else {
                    Log.i("CountriesViewModel", "not empty")
                    _countriesApiStatus.value = ApiStatus.DONE
                    _countries.value = countriesList.convertToCountriesDataFromNetwork()
                }
            }

            override fun onError(e: Throwable) {
                //_countries.value = ArrayList()
                _countriesApiStatus.value = ApiStatus.ERROR
                Log.e("CountriesViewModel", "onError: " + e.message)
            }

            override fun onComplete() {}
        }
    }

    private fun getSearchObserver(): DisposableObserver<List<NetworkCountriesData>> {
        return object : DisposableObserver<List<NetworkCountriesData>>() {

            override fun onNext(countriesList: List<NetworkCountriesData>) {

                if (countriesList.isEmpty()) {
                    _countries.value = ArrayList()
                    Log.i("ViewModel Search", "Not found")
                } else {
                    Log.i("ViewModel Search", "Found")
                    _countries.value = countriesList.convertToCountriesDataFromNetwork()
                }
            }

            override fun onError(e: Throwable) {
                _countriesApiStatus.value = ApiStatus.ERROR
                Log.e("ViewModel Search", "onError: " + e.message)
            }

            override fun onComplete() {}
        }
    }

    private fun getCountriesList() {

        _countriesApiStatus.value = ApiStatus.LOADING

        //check if database have data already
        disposable.add(coronaRepository.globalCasesFromDataBase()
            .subscribeWith(object : DisposableObserver<List<NetworkCountriesData>>() {
                override fun onComplete() {
                }
                override fun onNext(t: List<NetworkCountriesData>) {
                    if (t.isNotEmpty()) {
                        databaseNotContainData = false
                        Log.i("CountriesViewModel", "Not Empty")
                    } else {
                        Log.i("CountriesViewModel", "Empty")
                    }
                }
                override fun onError(e: Throwable) {
                }
            })
        )
        //save to database from network and then fetch back to UI
        disposable.add(
            Observable.concat(
                singleObservable()
                , coronaRepository.globalCasesFromDataBase()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(dataObserver)
        )

        disposable.add(
            publishSubject
                .debounce(600, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle { t -> coronaRepository.countryCasesDataBase(t) }
                .subscribeWith(searchObserver)
        )
        disposable.add(searchObserver)
        disposable.add(dataObserver)

    }

    override fun onCleared() {
        super.onCleared()
        Log.i("CountriesViewModel", "onClearCalled")
        disposable.clear()
    }

}