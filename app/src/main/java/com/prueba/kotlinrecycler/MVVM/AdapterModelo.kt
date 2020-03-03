package com.prueba.kotlinrecycler.MVVM

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prueba.kotlinrecycler.R
import kotlinx.android.synthetic.main.item_recycler_mvvv.view.*

class AdapterModelo(private val context:Context): RecyclerView.Adapter<AdapterModelo.MainViewHolder>() {

    private var dataList = mutableListOf<Usuario>()

    fun setListData(data: MutableList<Usuario>) {

        dataList = data
    }


    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler_mvvv, parent, false)

        return MainViewHolder(view)

    }

    override fun getItemCount(): Int {

      return if (dataList.size>0) {
          dataList.size
      }
        else{
             0}

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val user = dataList[position]
        holder.bindView(user)
    }


    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(user: Usuario) {

            Glide.with(context).load(user.imageUrl).into(itemView.imgRecycler)
            itemView.txtTitulo.text = user.nombre
            itemView.txtDescripcion.text = user.descripcion

        }


    }

}