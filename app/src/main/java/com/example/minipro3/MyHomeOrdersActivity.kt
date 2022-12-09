package com.example.minipro3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Adapters.MyHomeOrdersAdapter
import com.example.minipro3.Models.ModelHomeOrders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class MyHomeOrdersActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var myHomeOrdersAdapter: MyHomeOrdersAdapter
    private val mArrayList: ArrayList<ModelHomeOrders> = ArrayList()
    lateinit var fStore: FirebaseFirestore
    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_home_orders)
        fStore = FirebaseFirestore.getInstance()

        //supportActionBar!!.title = "My Home Orders"
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //supportActionBar!!.setDisplayShowHomeEnabled(true)

        recyclerView = findViewById(R.id.my_home_orders_recycler_view)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        myHomeOrdersAdapter = MyHomeOrdersAdapter(applicationContext, mArrayList, this)
        recyclerView.adapter = myHomeOrdersAdapter

        val retData = fStore.collection("MagicMeal").document(userid).collection("HomeOrders")
        retData.addSnapshotListener { snap, excep ->
            if (snap != null){
                for (i in snap.documentChanges){
                    if (i.type == DocumentChange.Type.ADDED){
                        if (i.document.exists()){
                            try {
                                val types : ModelHomeOrders = ModelHomeOrders(
                                    i.document.getString("deliveryaddress")!!,
                                    i.document.getString("deliverytime")!!,
                                    i.document.getString("mobileno")!!,
                                    i.document.getString("name")!!,
                                    i.document.getString("ordered_at")!!,
                                    i.document.getString("pickupaddress")!!,
                                    i.document.getString("subscription")!!,
                                    i.document.getString("statusoforder")!!,
                                    i.document.getString("uid")!!,
                                    i.document.getString("price")!!
                                )
                                types.set(i.document.id)
                                mArrayList.add(types)

                            }catch (e: Exception){
                                e.printStackTrace()
                                Log.d("ex", e.message.toString().trim())
                            }
                        }
                    }
                }
                myHomeOrdersAdapter.update(mArrayList)
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
