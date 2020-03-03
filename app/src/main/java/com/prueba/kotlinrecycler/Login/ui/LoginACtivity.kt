package com.prueba.kotlinrecycler.Login.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.prueba.kotlinrecycler.Login.utils.login
import com.prueba.kotlinrecycler.Login.utils.toast
import com.prueba.kotlinrecycler.R
import kotlinx.android.synthetic.main.activity_login_activity.*

class LoginACtivity : AppCompatActivity() {

    private lateinit var mauth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activity)


        mauth=FirebaseAuth.getInstance()


        button_sign_in.setOnClickListener {


            val email = text_email.text.toString().trim()
            val password=edit_text_password.text.toString().trim()

            if (email.isEmpty()){
                text_email .error="Email Requiered"
                text_email.requestFocus()
                return@setOnClickListener
            }

            if (! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                text_email .error="Valid Email Requiered"
                text_email.requestFocus()
                return@setOnClickListener

            }

            if (password.isEmpty()||password.length<6){

                edit_text_password .error="6 chard Password Requiered"
                edit_text_password.requestFocus()
                return@setOnClickListener

            }

            loginUser(email,password)

        }

        text_view_login.setOnClickListener {

            startActivity(Intent(this@LoginACtivity,
                RegisterActivity::class.java))

        }

        text_view_forget_password.setOnClickListener {

            startActivity(Intent(this@LoginACtivity,
                PasswordReset::class.java))

        }
    }

    private fun loginUser(email: String, password: String) {

        progressbarL.visibility=View.VISIBLE


        mauth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task->
                progressbarL.visibility=View.GONE

                if (task.isSuccessful){

                  login()

                }
                else{

                    task.exception?.message?.let {

                        toast(it)
                    }
                }

            }


    }

    override fun onStart() {
        super.onStart()
        mauth.currentUser?.let {
            login()
        }
    }
}
