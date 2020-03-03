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
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mauth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        mauth = FirebaseAuth.getInstance()

        text_view_register.setOnClickListener {

            startActivity(Intent(this@RegisterActivity, LoginACtivity::class.java))

        }


        button_register.setOnClickListener {

            val email = text_email_r.text.toString().trim()
            val password = edit_text_password_R.text.toString().trim()

            if (email.isEmpty()) {
                text_email_r.error = "Email Requiered"
                text_email_r.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                text_email_r.error = "Valid Email Requiered"
                text_email_r.requestFocus()
                return@setOnClickListener

            }

            if (password.isEmpty() || password.length < 6) {

                edit_text_password_R.error = "6 chard Password Requiered"
                edit_text_password_R.requestFocus()
                return@setOnClickListener

            }

            registerUser(email, password)

        }
    }

    private fun registerUser(email: String, password: String) {

        progressbar.visibility=View.VISIBLE
        mauth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressbar.visibility=View.GONE
                if (task.isSuccessful) {

                    login()

                } else {
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
