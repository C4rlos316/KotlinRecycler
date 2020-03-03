package com.prueba.kotlinrecycler.Login.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.prueba.kotlinrecycler.Login.utils.toast

import com.prueba.kotlinrecycler.R
import kotlinx.android.synthetic.main.fragment_verify_phone.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class VerifyPhoneFragment : Fragment() {

    private var verificationID:String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_phone, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPhone.visibility=View.VISIBLE
        layoutVerificar.visibility=View.GONE


        btnEnviar.setOnClickListener {


            val phone=edtNumero.text.toString().trim()


            if (phone.isEmpty()||phone.length!=10){

                edtNumero.error="Enter valid phone"
                edtNumero.requestFocus()
                return@setOnClickListener

            }

            val phoneNumber='+'+ccp.selectedCountryCode + phone

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,60,TimeUnit.SECONDS,requireActivity(),phoneAuthCallbacks
            )

            layoutPhone.visibility=View.GONE
            layoutVerificar.visibility=View.VISIBLE


        }

        btnVerficar.setOnClickListener {

            val code = edtCodigo.text.toString().trim()

            if (code.isEmpty()){

                edtCodigo.error="Codigo erroneo"
                edtCodigo.requestFocus()
                return@setOnClickListener

            }

            verificationID?.let {

                val credential=PhoneAuthProvider.getCredential(it,code)
                addPhoneNumber(credential)

            }

        }

    }


 private val phoneAuthCallbacks = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

        override fun onVerificationCompleted(phoneAuth: PhoneAuthCredential?) {

            phoneAuth?.let {

                addPhoneNumber(it)
            }

        }

        override fun onVerificationFailed(exception: FirebaseException?) {

            context?.toast(exception?.message!!)
            Log.e("Error",exception?.message!!)

        }

     override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
         super.onCodeSent(verificationId, token)
         this@VerifyPhoneFragment.verificationID=verificationId


     }

    }

    private fun addPhoneNumber(it: PhoneAuthCredential) {


        FirebaseAuth.getInstance().currentUser?.updatePhoneNumber(it)?.addOnCompleteListener {task->

            if (task.isSuccessful){

                context?.toast("Phone Added.")
                val action = VerifyPhoneFragmentDirections.actionVerifyPhoneFragmentToActionProfile()
                Navigation.findNavController(btnVerficar).navigate(action)

            }
            else{

                context?.toast(task.exception?.message!!)
                Log.e("Mal",task.exception?.message!!)

            }



        }
    }
}
