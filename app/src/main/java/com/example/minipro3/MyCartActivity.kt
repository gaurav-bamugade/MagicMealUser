package com.example.minipro3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Adapters.MyCartAdapter
import com.example.minipro3.Models.ModelCart
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_cart.*
import java.util.*
import java.util.concurrent.Executor
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MyCartActivity : AppCompatActivity() {

    private val fStore = FirebaseFirestore.getInstance()
    lateinit var recylerView: RecyclerView
    lateinit var cartAdapter: MyCartAdapter
    private val mArrayList: ArrayList<ModelCart> = ArrayList()
    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var totalPrice = ""

    val listt = mArrayList

    val mMessageReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            totalPrice = p1!!.getStringExtra("OverAllTotalPrice").toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        //from MyCartAdapter.kt
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter("totalprice"))

        setSupportActionBar(my_cart_tool_bar)
        val actionBar = supportActionBar
        actionBar!!.title = "My Cart"

        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        my_cart_checkout_button.setOnClickListener {
            val ref = fStore.collection("MagicMeal").document(userid).collection("CartList")
            ref.get().addOnSuccessListener {
                if (!it.isEmpty){
                    val i = Intent(this, CheckOutActivity::class.java)
                    i.putExtra("total", totalPrice)
                    startActivity(i)
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,"Cart is empty please select foods from Menu",Toast.LENGTH_SHORT).show()
                }
            }
        }

        val retData = fStore.collection("MagicMeal").document(userid).collection("CartList")
        retData.addSnapshotListener { snapshot, firestoreException ->
            if(snapshot != null){
                for(i in snapshot.documentChanges) {
                    if (i.type == DocumentChange.Type.ADDED) {
                        if (i.document.exists()) {
                            try {
                                val types: ModelCart = ModelCart(
                                    i.document.getString("imageoffood")!!,
                                    i.document.getString("nameoffood")!!,
                                    i.document.getString("priceoffood")!!,
                                    i.document.getString("offeroffood")!!,
                                    i.document.getString("qtyoffood")!!,
                                    i.document.getString("descoffood")!!,
                                    i.document.getString("categoryoffood")!!
                                )
                                types.set(i.document.id)
                                mArrayList.add(types)
                            } catch (e: Exception) {
                                Log.d("Main", e.message.toString().trim())
                            }
                        }
                    }
                }
                cartAdapter.update(mArrayList)
            }
        }

        recylerView = findViewById(R.id.my_cart_recycler_view)
        recylerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recylerView.layoutManager = layoutManager
        cartAdapter = MyCartAdapter(applicationContext, mArrayList)
        recylerView.adapter = cartAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.side_menu_cart, menu)
       return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.clear_cart_of_my_cart-> {
                clearCart()
            }
        }
        return true
    }

    private fun clearCart(){
        val list=ArrayList<ModelCart>()
        list.addAll(mArrayList)
        Thread(
            Runnable {
                for (i in 0 until list.size) {
                    //user view
                    var ref = fStore.collection("MagicMeal").document(userid)
                        .collection("CartList").document(list[i].foodid).delete().addOnSuccessListener {
                            Toast.makeText(applicationContext,"Cart Cleared",Toast.LENGTH_SHORT).show()
                            Log.d("del","$i deleted")
                        }
                    Thread.sleep(5000)
                    //admin view
                    val ref1 = fStore.collection("MagicMealAdmin").document("CartList")
                        .collection(userid).document(list[i].foodid).delete().addOnSuccessListener {
                            Toast.makeText(applicationContext,"Admin Cart Cleared",Toast.LENGTH_SHORT).show()
                            Log.d("del","$i deleted")
                        }
                    Thread.sleep(5000)
                }
            }
        ).start()
        mArrayList.clear()
        cartAdapter.update(mArrayList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
