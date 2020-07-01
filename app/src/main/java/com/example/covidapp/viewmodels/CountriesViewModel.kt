package com.example.covidapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidapp.domain.CountriesData
import com.example.covidapp.network.countriesDataService
import com.example.covidapp.util.ApiStatus
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class CountriesViewModel : ViewModel() {


    private val disposable = CompositeDisposable()
    private val publishSubject =
        PublishSubject.create<String>()
    private val observer: DisposableObserver<List<CountriesData>> = getSearchObserver()
    private val _countries = MutableLiveData<List<CountriesData>>()
    val countries: LiveData<List<CountriesData>>
        get() = _countries

    // For api status
    private val _countriesApiStatus = MutableLiveData<ApiStatus>()
    val countriesApiStatus: LiveData<ApiStatus>
        get() = _countriesApiStatus

    init {
        getCountriesList()
        publishSubject.onNext("")
    }

    fun setUserSearchQuery(query: String) {
        Log.i("CountriesViewModel", query)
        publishSubject.onNext(query)
    }

    private fun getSearchObserver(): DisposableObserver<List<CountriesData>> {
        return object : DisposableObserver<List<CountriesData>>() {
            override fun onNext(countries: List<CountriesData>) {

                if (countries.isEmpty()) {
                    _countries.value = countries
                    _countriesApiStatus.value = ApiStatus.ERROR

                } else {
                    _countriesApiStatus.value = ApiStatus.DONE
                    if (countries[0].country == "Not Found") {
                        _countries.value = ArrayList()
                    } else {
                        _countries.value = countries
                    }
                }
                Log.i("CountriesViewModel", "observerOnNext")
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
        Log.i("CountriesViewModel", "onClearCalled")
        disposable.clear()
    }

    private fun getCountriesList() {
        _countriesApiStatus.value = ApiStatus.LOADING
        disposable.add(
            publishSubject
                .debounce(600, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle(object :
                    Function<String, Single<List<CountriesData>>> {
                    @Throws(Exception::class)
                    override fun apply(t: String): Single<List<CountriesData>> {

                        if (t == "") {
                            return countriesDataService.getCountries().doOnError { throwable ->

                                Log.i("CountriesViewModel", throwable.message)
                            }.onErrorReturn { throwable -> ArrayList() }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                        } else {
                            Log.i("CountriesViewModel", "queryNetworkCall")
                            return countriesDataService.getCountry(t)
                                .flatMap {
                                    Single.just(listOf(it))
                                }.doOnError { throwable ->

                                }.onErrorReturn { throwable ->
                                    throwable.message?.let {
                                        if (it.contains("HTTP 404")) {
                                            Log.i("CountriesViewModel", throwable.message)
                                            val countriesData =
                                                CountriesData(
                                                    "Not Found",
                                                    0,
                                                    0,
                                                    0,
                                                    0,
                                                    0,
                                                    0,
                                                    0
                                                )
                                            return@onErrorReturn listOf(countriesData)
                                        } else {
                                            Log.i("CountriesViewModel", throwable.message)
                                            return@onErrorReturn ArrayList()
                                        }
                                    }
                                }
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