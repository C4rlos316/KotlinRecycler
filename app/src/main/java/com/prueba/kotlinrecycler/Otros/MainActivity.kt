package com.prueba.kotlinrecycler.Otros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.prueba.kotlinrecycler.R
import kotlinx.android.synthetic.main.item_recycler.view.*

class MainActivity : AppCompatActivity() {


    lateinit var searchText:EditText
    lateinit var recyclerView: RecyclerView

    lateinit var database:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        searchText=findViewById(R.id.edtNombre)
        recyclerView=findViewById(R.id.recyclerView)


        database=FirebaseDatabase.getInstance().getReference("Users")


        loadFirebaseData()

    }

    private fun loadFirebaseData() {

        val firebaseRecycler=object : FirebaseRecyclerAdapter<User, UserViewHolder>(
            User::class.java,
            R.layout.item_recycler,
            UserViewHolder::class.java,
            database
        ){
            override fun populateViewHolder(viewHolder: UserViewHolder?, p1: User?, p2: Int) {

                viewHolder?.view?.txtNombre?.setText(p1?.name)
                viewHolder?.view?.txtEmail?.setText(p1?.status)

            }


        }

        recyclerView.adapter=firebaseRecycler


    }

    class UserViewHolder(var view: View):RecyclerView.ViewHolder(view){


    }

}
