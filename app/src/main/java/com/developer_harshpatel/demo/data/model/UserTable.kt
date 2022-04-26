package com.developer_harshpatel.demo.data.model

import com.google.gson.annotations.SerializedName

data class UserTable(
    @SerializedName("data")
    val data: List<User> = arrayListOf()
)