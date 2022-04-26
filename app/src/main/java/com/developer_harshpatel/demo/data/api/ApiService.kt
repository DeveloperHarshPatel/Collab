package com.developer_harshpatel.demo.data.api

import com.developer_harshpatel.demo.data.model.User
import com.developer_harshpatel.demo.data.model.UserTable
import io.reactivex.Single

interface ApiService {

    fun getUsers(): Single<UserTable>

}