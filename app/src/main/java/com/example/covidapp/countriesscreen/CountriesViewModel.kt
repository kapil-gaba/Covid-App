package com.example.covidapp.countriesscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidapp.ApiStatus
import com.example.covidapp.network.CountriesData
import com.example.covidapp.network.countriesDataService
import com.google.gson.JsonArray
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class CountriesViewModel : ViewModel() {


    private val disposable = CompositeDisposable()
    private val publishSubject =
        PublishSubject.create<String>()
    private val observer: DisposableObserver<List<CountriesData>> = getSearchObserver()
    private val _countries = MutableLiveData<List<CountriesData>>()
            val countries : LiveData<List<CountriesData>>
                    get() = _countries
    // For api status
    private val _countriesApiStatus = MutableLiveData<ApiStatus>()
    val countriesApiStatus: LiveData<ApiStatus>
        get() = _countriesApiStatus

    init {

        getCountriesList()
        publishSubject.onNext("")
    }

    private fun getSearchObserver(): DisposableObserver<List<CountriesData>> {
        return object : DisposableObserver<List<CountriesData>>() {
            override fun onNext(countries: List<CountriesData>) {
                _countries.value = countries
                _countriesApiStatus.value = ApiStatus.DONE
                Log.i("CountriesViewModel", countries[0].country)
            }

            override fun onError(e: Throwable) {
                _countries.value = ArrayList()
                _countriesApiStatus.value = ApiStatus.ERROR
                Log.e("CountriesViewModel", "onError: " + e.message)
            }

            override fun onComplete() {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun getCountriesList() {
        _countriesApiStatus.value = ApiStatus.LOADING
        disposable.add(
            publishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle(object :
                    Function<String, Single<List<CountriesData>>> {
                    @Throws(Exception::class)
                    override fun apply(t: String): Single<List<CountriesData>> {

                        if (t == "") {
                            return countriesDataService.getCountries()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                        } else {
                            return countriesDataService.getCountry(t)
                                .flatMap { Single.just(listOf(it)) }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                        }
                    }
                })
                .subscribeWith(observer)
        )


        disposable.add(observer)

    }

}