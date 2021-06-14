package com.example.posts.api

import com.example.posts.models.User
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    fun getUsers(): Call<MutableList<User>>
}