package com.prueba.kotlinrecycler.Login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.prueba.kotlinrecycler.Login.utils.toast
import com.prueba.kotlinrecycler.R
import kotlinx.android.synthetic.main.activity_login_activity.*
import kotlinx.android.synthetic.main.activity_password_reset.*

class PasswordReset : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)


        btnRestablerContra.setOnClickListener {


            val email = edtEmailRestablecer.text.toString().trim()

            if (email.isEmpty()){
                edtEmailRestablecer .error="Email Requiered"
                edtEmailRestablecer.requestFocus()
                return@setOnClickListener
            }

            if (! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edtEmailRestablecer .error="Valid Email Requiered"
                edtEmailRestablecer.requestFocus()
                return@setOnClickListener

            }

            progressbarRes.visibility=View.VISIBLE

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener {task->
                    progressbarRes.visibility=View.GONE


                    if (task.isSuccessful){

                        this.toast("Checa tu email")

                    }
                    else{

                        this.toast(task.exception?.message!!)
                    }

                }

        }
    }
}
