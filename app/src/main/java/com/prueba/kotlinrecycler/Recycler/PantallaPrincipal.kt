package com.prueba.kotlinrecycler.Recycler

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import com.prueba.kotlinrecycler.R
import com.prueba.kotlinrecycler.Recycler.Adapter.MyAdapter
import com.squareup.picasso.Picasso

class PantallaPrincipal : AppCompatActivity() {


    val ITEM_COUNT = 21
    var total_item = 0
    var last_visible_item = 0

    lateinit var adapter: MyAdapter

    var isLoading = false
    var isMaxData = false

    var last_node: String? = ""
    var last_key: String? = ""

    lateinit var recyclerView: RecyclerView

    lateinit var databaseReference: DatabaseReference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // setSupportActionBar(toolvar)
        //toolvar.setTitle("hola")


        getLastKey()

        val layoutManager = LinearLayoutManager(this)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager


        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        adapter = MyAdapter(this)
        recyclerView.adapter = adapter


        getUsers()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                total_item=layoutManager.itemCount

                last_visible_item= layoutManager.findLastVisibleItemPosition()

                if (!!isLoading &&total_item<=last_visible_item+ITEM_COUNT){

                    getUsers()
                    isLoading=true

                }
            }

        })

    }

    private fun getUsers() {

        if (!!isMaxData) {

            val query: Query

            if (TextUtils.isEmpty(last_node)) {
                query = FirebaseDatabase.getInstance().reference.child("Users")
                    .orderByKey().limitToFirst(ITEM_COUNT)
            } else {
                query = FirebaseDatabase.getInstance().reference.child("Users")
                    .orderByKey().startAt(last_node).limitToFirst(ITEM_COUNT)}

                query.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0.hasChildren()) {

                            val userList = ArrayList<Users>()

                            for (snapShot in p0.children) {

                                userList.add(snapShot.getValue(Users::class.java)!!)

                            }
                            last_node = userList[userList.size - 1].id


                            if (!last_node.equals(last_key)) {
                                userList.removeAt(userList.size - 1)
                            } else {
                                last_node = "end"

                                adapter.addAll(userList)
                                isLoading = false
                            }


                        } else {

                            isLoading=false
                            isMaxData=true

                        }
                    }

                })


        }

    }

    private fun getLastKey() {

        val get_last = FirebaseDatabase.getInstance().getReference().child("Users")
            .orderByKey()
            .limitToLast(1)

        get_last.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                for (userSnap in p0.children) {

                    last_key = userSnap.key

                }
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId


        if (id == R.id.refresh) {

            isMaxData=false
            last_node=adapter.lastItemId
            adapter.removeLasItem()
            adapter.notifyDataSetChanged()
            getLastKey()
            getUsers()

        }

        return true
    }


}
