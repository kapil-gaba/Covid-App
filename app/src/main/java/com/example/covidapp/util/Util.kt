package com.example.covidapp.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covidapp.R


enum class ApiStatus {
    LOADING,
    DONE,
    ERROR
}

@BindingAdapter("ApiStatus")
fun getApiStatus(imgView: ImageView, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            Log.i("LoadingStatus", "ok")
            imgView.visibility = View.VISIBLE
            imgView.setImageResource(R.drawable.loading_animation)
        }
        ApiStatus.ERROR -> {
            Log.i("ErrorStatus", "ok")
            imgView.visibility = View.VISIBLE
            imgView.setImageResource(R.drawable.ic_connection_error)
        }
        ApiStatus.DONE -> {
            imgView.visibility = View.GONE
            Log.i("DoneStatus", "ok")
        }
    }
}

@BindingAdapter("VisibilityControl")
fun controlViewVisibility(constraintLayout: ConstraintLayout, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            Log.i("LoadingStatus2", "ok")
            constraintLayout.visibility = View.GONE

        }
        ApiStatus.ERROR -> {
            Log.i("ErrorStatus2", "ok")
            constraintLayout.visibility = View.GONE

        }
        ApiStatus.DONE -> {
            constraintLayout.visibility = View.VISIBLE
            Log.i("DoneStatus2", "ok")
        }
    }
}

// Extension function for viewModelFactory to return viewModel class
fun <T : ViewModel> T.createFactory(): ViewModelProvider.Factory {
    val viewModel = this
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel as T
    }
}