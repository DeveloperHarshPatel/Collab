package com.developer_harshpatel.demo.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developer_harshpatel.demo.data.model.User
import com.developer_harshpatel.demo.data.model.UserTable
import com.developer_harshpatel.demo.data.repository.DashboardRepository
import com.developer_harshpatel.demo.ui.base.App
import com.developer_harshpatel.demo.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DashboardViewModel(private val repository: DashboardRepository) : ViewModel() {

    private val users = MutableLiveData<Resource<UserTable>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        users.postValue(Resource.loading(null))
        compositeDisposable.add(
            repository.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userList ->
                    users.postValue(Resource.success(userList))
                }, { throwable ->
                    App.log("" + throwable)
                    users.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getUsers(): LiveData<Resource<UserTable>> {
        return users
    }
}