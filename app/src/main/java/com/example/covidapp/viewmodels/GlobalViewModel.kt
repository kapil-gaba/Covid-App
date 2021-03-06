package com.example.covidapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidapp.domain.GlobalData
import com.example.covidapp.repository.CoronaCasesRepository
import com.example.covidapp.repository.CoronaRepository
import com.example.covidapp.util.ApiStatus
import kotlinx.coroutines.*


class GlobalViewModel ( private val coronaRepository : CoronaRepository) : ViewModel() {

    // Coroutines
    val job = SupervisorJob()
    val viewModelScope = CoroutineScope(job + Dispatchers.Main)

    // For api status
    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    //  For Global data
    private val _globalCases = coronaRepository.globalCasesDataBase
    val globalCases: LiveData<GlobalData>
        get() = _globalCases


    init {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                coronaRepository.refreshGlobalData()
                _apiStatus.value = ApiStatus.DONE

            } catch (t: Throwable) {
                if (globalCases.value != null) {
                    _apiStatus.value = ApiStatus.DONE
                } else {
                    _apiStatus.value = ApiStatus.ERROR
                }
                //  _apiStatus.value = ApiStatus.ERROR
                Log.i("GlobalViewModel", t.message)
            }
        }

    }

    // Cancelling job on clearing viewModel
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}