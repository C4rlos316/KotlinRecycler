package com.prueba.kotlinrecycler.MVVM.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prueba.kotlinrecycler.MVVM.Usuario
import com.prueba.kotlinrecycler.MVVM.domain.Repo

class MainViewModel:ViewModel() {

   private val repo= Repo()
    fun fetchUserData():LiveData<MutableList<Usuario>>{
        val mutableData=MutableLiveData<MutableList<Usuario>>()

        repo.getUserData().observeForever{
            mutableData.value=it

        }

        return mutableData

    }

}