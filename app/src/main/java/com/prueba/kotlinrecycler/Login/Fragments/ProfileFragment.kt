package com.prueba.kotlinrecycler.Login.Fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.prueba.kotlinrecycler.Login.utils.toast

import com.prueba.kotlinrecycler.R
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {


    private val REQUEST_IMAGE_CAPTURE = 100
    private val DEFAULT_IMAGE_URL="https://picsum.photos/200"
    private lateinit var imageUri:Uri

    private val currentUser=FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        currentUser?.let {user->


            Glide.with(this).load(user.photoUrl).into(userProfile)

            txtUserProfile.setText(user.displayName)
            edtCorreo.text=user.email
            edtPhone.text=if (user.phoneNumber.isNullOrEmpty())
                "Add number"
            else
                user.phoneNumber

            if (user.isEmailVerified){

                txtVerificado.visibility=View.GONE
            }
            else{
                txtVerificado.visibility=View.VISIBLE

            }


        }

        userProfile.setOnClickListener {

            takePicture()

        }

        btnGuardar.setOnClickListener {

            val photo = when{

                ::imageUri.isInitialized->imageUri
                currentUser?.photoUrl==null-> Uri.parse(DEFAULT_IMAGE_URL)
                else-> currentUser?.photoUrl

            }

            val name= txtUserProfile.text.toString().trim()


            if (name.isEmpty()){

                txtUserProfile.error="Name requeried"
                txtUserProfile.requestFocus()
                return@setOnClickListener
            }

            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(photo)
                .build()

            progressbarPerfil.visibility=View.VISIBLE


            currentUser?.updateProfile(updates)?.addOnCompleteListener{

                    if (it.isSuccessful){
                        progressbarPerfil.visibility=View.GONE
                        context?.toast("Profile Update")
                    }
                    else{

                        context?.toast(it.exception?.message!!)
                    }
                }
        }


        txtVerificado.setOnClickListener {

            currentUser?.sendEmailVerification()
                ?.addOnCompleteListener {

                    if (it.isSuccessful){

                        context?.toast("Hemos enviando una verificacion a tu correo")

                    }
                    else{

                        context?.toast(it.exception?.message!!)
                    }

                }

        }

        edtPhone.setOnClickListener {

            val action= ProfileFragmentDirections.actionActionProfileToVerifyPhoneFragment()
            Navigation.findNavController(it).navigate(action)



        }

        edtCorreo.setOnClickListener {

            val action= ProfileFragmentDirections.actionUpdateEmail()
            Navigation.findNavController(it).navigate(action)


        }

        edtPass.setOnClickListener {

            val action= ProfileFragmentDirections.actionUpdatePassword()
            Navigation.findNavController(it).navigate(action)


        }


    }

    private fun takePicture() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->

            pictureIntent.resolveActivity(activity?.packageManager!!)?.also {

                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==REQUEST_IMAGE_CAPTURE&&resultCode== RESULT_OK){

            val imageBitmap=data?.extras?.get("data") as Bitmap

            uploadImageAndSAveUri(imageBitmap)

        }

    }

    private fun uploadImageAndSAveUri(bitmap: Bitmap) {

        val baos = ByteArrayOutputStream()
        val storageRef=FirebaseStorage.getInstance().reference.
            child("pics/+${FirebaseAuth.getInstance().currentUser?.uid}")

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)

        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)


        progressbarPerfil.visibility=View.VISIBLE
        upload.addOnCompleteListener{uploadTask->
            progressbarPerfil.visibility=View.GONE

            if (uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener{urlTask->
                    urlTask.result?.let {

                        imageUri=it
                        activity?.toast(imageUri.toString())

                        userProfile.setImageBitmap(bitmap)

                    }


                }

            }

            else{
                uploadTask.exception?.let {

                    activity?.toast(it.message!!)
                }
            }

        }



    }

}
