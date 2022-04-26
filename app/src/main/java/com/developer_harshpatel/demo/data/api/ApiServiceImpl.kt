package com.developer_harshpatel.demo.data.api

import com.developer_harshpatel.demo.data.model.User
import com.developer_harshpatel.demo.data.model.UserTable
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single

class ApiServiceImpl : ApiService {

    override fun getUsers(): Single<UserTable> {

        // Rx2AndroidNetworking is compatible with RxJava2
        return Rx2AndroidNetworking.get("https://reqres.in/api/users?page=1&&per_page=12")
            .build()
            .getObjectSingle(UserTable::class.java)


    }

}