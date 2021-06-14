package com.example.posts.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.posts.repositories.UserRepository
import com.example.posts.models.User
import com.example.posts.utils.Utility.isInternetAvailable

class UserViewModel(private val context: Context) : ViewModel() {

    private var listData = MutableLiveData<ArrayList<User>>()

    init {
        val userRepository: UserRepository by lazy {
            UserRepository
        }
        if (context.isInternetAvailable()) {
            listData = userRepository.getMutableLiveData(context)
        }
    }

    fun getData(): MutableLiveData<ArrayList<User>> {
        return listData
    }
}