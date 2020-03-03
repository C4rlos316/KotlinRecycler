package com.prueba.kotlinrecycler.MVVM.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.prueba.kotlinrecycler.MVVM.AdapterModelo
import com.prueba.kotlinrecycler.MVVM.viewModel.MainViewModel
import com.prueba.kotlinrecycler.R
import kotlinx.android.synthetic.main.activity_main_principal.*

class MainPrincipal : AppCompatActivity() {

    //se inicializa al momento
    private lateinit var adapter: AdapterModelo

    //se inicializa cuando se necesita
    private val viewModel by lazy {

        ViewModelProviders.of(this).get(MainViewModel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_principal)

        adapter= AdapterModelo(this)

        recyclerPrincipal.layoutManager=LinearLayoutManager(this)
        recyclerPrincipal.adapter=adapter
        obserData()


    }


    fun obserData(){

        shimmer_view_container.startShimmer()
        viewModel.fetchUserData().observe(this, Observer {

            shimmer_view_container.hideShimmer()
            shimmer_view_container.stopShimmer()
            shimmer_view_container.visibility=View.GONE

            adapter.setListData(it)
            adapter.notifyDataSetChanged()


        })

    }
}
