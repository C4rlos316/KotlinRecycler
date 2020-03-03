package com.prueba.kotlinrecycler.Login.Fragments


import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.prueba.kotlinrecycler.Login.utils.toast

import com.prueba.kotlinrecycler.R
import kotlinx.android.synthetic.main.activity_login_activity.*
import kotlinx.android.synthetic.main.fragment_update_email.*

/**
 * A simple [Fragment] subclass.
 */
class UpdateEmail : Fragment() {

    val currentUser=FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_email, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearCorreo.visibility=View.VISIBLE
        layoutUpdate.visibility=View.GONE

        btnActualizarCorreo.setOnClickListener {

            val password=edtNuevoCorreo.text.toString().trim()

            if (password.isEmpty()){

                edtNuevoCorreo.error="Password requeried"
                edtNuevoCorreo.requestFocus()
                return@setOnClickListener

            }

            currentUser?.let {

                val credential=EmailAuthProvider.getCredential(it.email!!,password)
                progressEmail.visibility=View.VISIBLE


                it.reauthenticate(credential)
                    .addOnCompleteListener {task->
                        progressEmail.visibility=View.GONE

                        when {
                            task.isSuccessful -> {

                                linearCorreo.visibility=View.GONE
                                layoutUpdate.visibility=View.VISIBLE


                            }
                            task.exception is FirebaseAuthInvalidCredentialsException -> {

                                edtNuevoCorreo.error="Invalid password"
                                edtNuevoCorreo.requestFocus()

                            }
                            else -> {
                                context?.toast(task.exception?.message!!)

                            }
                        }


                    }


            }



        }


        btnOtro.setOnClickListener {view->

            val email=edtOtro.text.toString().trim()

            if (email.isEmpty()){
                edtOtro .error="Email Requiered"
                edtOtro.requestFocus()
                return@setOnClickListener
            }

            if (! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edtOtro .error="Valid Email Requiered"
                edtOtro.requestFocus()
                return@setOnClickListener

            }

            progressEmail.visibility=View.VISIBLE

            currentUser?.let {user->


                user.updateEmail(email)
                    .addOnCompleteListener {task->

                        progressEmail.visibility=View.GONE

                        if (task.isSuccessful){

                            val action=UpdateEmailDirections.actionBackEmail()
                            Navigation.findNavController(view).navigate(action)

                        }
                        else{

                            context?.toast(task.exception?.message!!)
                        }


                    }


            }


        }


    }


}
