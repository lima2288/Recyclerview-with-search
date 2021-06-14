package com.example.posts.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.posts.api.ApiClient
import com.example.posts.models.User
import com.example.posts.utils.Utility.showProgressBar
import com.example.posts.utils.Utility.hideProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {

    fun getMutableLiveData(context: Context) : MutableLiveData<ArrayList<User>>{

        val mutableLiveData = MutableLiveData<ArrayList<User>>()

        context.showProgressBar()

        ApiClient.apiService.getUsers().enqueue(object : Callback<MutableList<User>> {
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                hideProgressBar()
                Log.e("error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                hideProgressBar()
                val usersResponse = response.body()
                usersResponse?.let { mutableLiveData.value = it as ArrayList<User> }
            }

        })

        return mutableLiveData
    }

}