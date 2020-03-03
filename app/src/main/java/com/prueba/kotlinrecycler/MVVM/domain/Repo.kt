package com.prueba.kotlinrecycler.MVVM.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.prueba.kotlinrecycler.MVVM.Usuario

class Repo {


    fun getUserData():LiveData<MutableList<Usuario>>{

        val mutable= MutableLiveData<MutableList<Usuario>>()

        FirebaseFirestore.getInstance().collection("Usuarios").get().addOnSuccessListener { result->

            val listData= mutableListOf<Usuario>()
            for(document in result){

                val imageUrl=document.getString("imageUrl")
                val nombre=document.getString("nombre")
                val descripcion=document.getString("descripcion")
                val usuario=Usuario(imageUrl!!,nombre!!,descripcion!!)
                listData.add(usuario)

            }
            mutable.value=listData

        }

        return mutable

    }
}