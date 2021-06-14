package com.example.posts.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("userId")
    val userId: Int? = null,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("body")
    val body: String? = null,

    @SerializedName("comments")
    val comments: String? = null,

    var expand: Boolean = false

) : Serializable
