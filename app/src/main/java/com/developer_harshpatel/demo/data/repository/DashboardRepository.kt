package com.developer_harshpatel.demo.data.repository

import com.developer_harshpatel.demo.data.api.ApiHelper
import com.developer_harshpatel.demo.data.model.User
import com.developer_harshpatel.demo.data.model.UserTable
import io.reactivex.Single

class DashboardRepository(private val apiHelper: ApiHelper) {

    fun getUsers(): Single<UserTable> {
        return apiHelper.getUsers()
    }

}