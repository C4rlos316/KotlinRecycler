package com.prueba.kotlinrecycler.Login.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.prueba.kotlinrecycler.Login.ui.HomeActivity
import com.prueba.kotlinrecycler.Login.ui.LoginACtivity

fun Context.toast(message:String)=Toast.makeText(this,message,Toast.LENGTH_SHORT).show()


fun Context.login(){

    val intent = Intent(this, HomeActivity::class.java).apply {

        flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    }
    startActivity(intent)
}



fun Context.logout(){

    val intent = Intent(this, LoginACtivity::class.java).apply {
        flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    }
    startActivity(intent)
}