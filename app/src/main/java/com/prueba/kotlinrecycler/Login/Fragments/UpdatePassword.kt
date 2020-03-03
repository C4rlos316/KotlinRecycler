package com.prueba.kotlinrecycler.Login.Fragments


import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_update_password.*

/**
 * A simple [Fragment] subclass.
 */
class UpdatePassword : Fragment() {


    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_password, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearCorreo.visibility = View.VISIBLE
        layoutPass.visibility = View.GONE


        btnActualizarCorreo.setOnClickListener {

            val password = edtNuevoCorreo.text.toString().trim()

            if (password.isEmpty()) {

                edtNuevoCorreo.error = "Password requeried"
                edtNuevoCorreo.requestFocus()
                return@setOnClickListener

            }

            currentUser?.let {

                val credential = EmailAuthProvider.getCredential(it.email!!, password)
                progressPass.visibility = View.VISIBLE

                it.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        progressPass.visibility = View.GONE

                        when {
                            task.isSuccessful -> {

                                linearCorreo.visibility = View.GONE
                                layoutPass.visibility = View.VISIBLE

                            }
                            task.exception is FirebaseAuthInvalidCredentialsException -> {

                                edtNuevoCorreo.error = "Invalid password"
                                edtNuevoCorreo.requestFocus()

                            }
                            else -> {
                                context?.toast(task.exception?.message!!)
                            }
                        }

                    }

            }


        }

        btnNuevaContrasena.setOnClickListener {

            val password=edtNewPass.text.toString().trim()


            if (password.isEmpty()|| password.length<6){

                edtNewPass.error="Tiene que tener 6 caracteres"
                edtNewPass.requestFocus()
                return@setOnClickListener
            }


            if (password!=edtNewPassCon.text.toString().trim()){

                edtNewPassCon.error="La contraseña no es la misma"
                edtNewPassCon.requestFocus()
                return@setOnClickListener

            }

            currentUser?.let {user->
                progressPass.visibility = View.VISIBLE
                user.updatePassword(password)
                    .addOnCompleteListener {task->

                        if (task.isSuccessful){
                            val action=UpdatePasswordDirections.actionBackPassword()
                            Navigation.findNavController(it).navigate(action)
                            context?.toast("Se actualizo bien.")


                        }
                        else{

                            context?.toast(task.exception?.message!!)
                        }

                    }
            }
        }



    }

}
