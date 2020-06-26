package com.example.covidapp.globalscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidapp.ApiStatus
import com.example.covidapp.network.GlobalData
import com.example.covidapp.network.GlobalDataApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class GlobalViewModel : ViewModel() {

    // Coroutines
    val job = Job()
    val viewModelScope = CoroutineScope(job + Dispatchers.Main)

    // For api status
    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    //  For Global data
    private val _globalCases = MutableLiveData<GlobalData>()
    val globalCases: LiveData<GlobalData>
        get() = _globalCases

    init {
        getGlobalData()
    }

    fun getGlobalData() {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            val deferredData = GlobalDataApi.globalDataService.getGlobalData()
            try {
                val data = deferredData.await()
                if (data != null) {
                    Log.i("GlobalViewModel", "Success")
                    _apiStatus.value = ApiStatus.DONE
                    _globalCases.value = data
                }
            } catch (networkError: IOException) {
                Log.i("GlobalViewModel", networkError.toString())
                _apiStatus.value = ApiStatus.ERROR
            }
        }
    }

    // Cancelling job on clearing viewModel
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}