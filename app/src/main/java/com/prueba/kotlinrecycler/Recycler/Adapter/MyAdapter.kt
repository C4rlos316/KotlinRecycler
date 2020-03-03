package com.prueba.kotlinrecycler.Recycler.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prueba.kotlinrecycler.R
import com.prueba.kotlinrecycler.Recycler.Users
import kotlinx.android.synthetic.main.item_recycler.view.*

class MyAdapter(internal var context: Context): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    internal var userList: MutableList<Users>


    val lastItemId:String?
        get() =userList[userList.size-1].id


    fun addAll(newUsers: List<Users>){

        val init = userList.size
        userList.addAll(newUsers)
        notifyItemRangeChanged(init,newUsers.size)

    }

    fun removeLasItem(){

        userList.removeAt(userList.size-1)

    }


    init {
        this.userList=ArrayList()
    }


    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {


        internal var txtName:TextView
        internal var txtEmail:TextView


        init {

            txtName=itemView.findViewById(R.id.txtNombre)
            txtEmail=itemView.findViewById(R.id.txtEmail)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.item_recycler,parent,false)

        return MyViewHolder(itemView)


    }

    override fun getItemCount(): Int {

        return userList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.txtName.text=userList[position].name

        holder.txtEmail.text=userList[position].email


    }
}