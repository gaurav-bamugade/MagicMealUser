package com.example.minipro3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Models.ModelOrders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_orders.*
import java.lang.Exception
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import com.example.minipro3.MyOrdersAdapter as MyOrdersAdapter1

class MyOrdersActivity : AppCompatActivity() {

    private val fStore = FirebaseFirestore.getInstance()
    lateinit var recylerView: RecyclerView
    lateinit var ordersAdapter: MyOrdersAdapter1
    private val mArrayList : ArrayList<ModelOrders> = ArrayList()
    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val adminID = "KKpL3gQiy8Ps7x5CdobOn5uxTGl1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        //supportActionBar!!.title = "My Orders"
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

      /*  view_my_orders_my_home_orders.setOnClickListener {
            startActivity(Intent(this, MyHomeOrdersActivity::class.java))
        }*/

        val retData = fStore.collection("MagicMeal").document(userid).collection("CateringOrders")
        retData.addSnapshotListener { snap, excep ->
            if (snap != null){
                for (i in snap.documentChanges){
                    if (i.type == DocumentChange.Type.ADDED){
                        if (i.document.exists()){
                            try {
                                val types : ModelOrders = ModelOrders(
                                    i.document.getString("city")!!,
                                    i.document.getString("deliverytime")!!,
                                    i.document.getString("home")!!,
                                    i.document.getString("landmark")!!,
                                    i.document.getString("mobno")!!,
                                    i.document.getString("name")!!,
                                    i.document.getString("ordered_at")!!,
                                    i.document.getString("pin")!!,
                                    i.document.getString("road")!!,
                                    i.document.getString("state")!!,
                                    i.document.getString("totalprice")!!,
                                    i.document.getString("uid")!!,
                                    i.document.getString("statusoforder")!!
                                )
                                types.set(i.document.id)
                                mArrayList.add(types)

                            }catch (e: Exception){
                                e.printStackTrace()
                                Log.d("ex", i.document.getString("city").toString().trim())
                            }
                        }
                    }
                }
                ordersAdapter.update(mArrayList)
            }
        }


        my_orders_recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        my_orders_recycler_view.layoutManager = layoutManager
        ordersAdapter = MyOrdersAdapter1(applicationContext, mArrayList)
        my_orders_recycler_view.adapter = ordersAdapter

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
