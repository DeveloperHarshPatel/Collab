package com.developer_harshpatel.demo.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("first_name")
    val first_name: String = "",
    @SerializedName("last_name")
    val last_name: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("avatar")
    val avatar: String = ""
)