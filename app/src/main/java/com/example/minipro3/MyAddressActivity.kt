package com.example.minipro3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Adapters.AddressAdapter
import com.example.minipro3.Models.ModelAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_address.*
import kotlin.collections.ArrayList

class MyAddressActivity : AppCompatActivity() {

    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val fStore = FirebaseFirestore.getInstance()
    lateinit var recylerView: RecyclerView
    lateinit var addressList: ArrayList<ModelAddress>
    lateinit var addressAdapter: AddressAdapter
    private val mArratList: ArrayList<ModelAddress> = ArrayList()
    private var from_other="self"
    private val adminID = "KKpL3gQiy8Ps7x5CdobOn5uxTGl1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_address)

        try {
            if (intent.extras!!.containsKey("other_activity")) {
                from_other = intent.extras!!.getString("other_activity")!!.toString()
            }
        }catch (e: Exception){
            Log.d("ex", e.message.toString())
        }

   //     val actionBar = supportActionBar
       // actionBar!!.title = "My Address"

     //   actionBar.setDisplayHomeAsUpEnabled(true)
     //   actionBar.setDisplayHomeAsUpEnabled(true)

        add_new_address.setOnClickListener {
            startActivityForResult(Intent(this, ProfMyAddressActivity::class.java),1234)
        }

        reqNewData()
        recylerView = findViewById(R.id.recyler_view_address)
        recylerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recylerView.layoutManager = layoutManager
        addressAdapter = AddressAdapter(applicationContext, mArratList, from_other,this)
        recylerView.adapter = addressAdapter

    }

    private fun reqNewData(){
        mArratList.clear()
        val retData = fStore.collection("MagicMeal").document(userid).collection("UsersAddress")
        retData.get()
            .addOnSuccessListener {
                for (i in it) {
                    if (!i.exists()) {
                        Log.d("Main", "Empty")
                    } else {
                        Log.d("data", i.get("buildno").toString())
                        try {
                            val types: ModelAddress = ModelAddress(
                                i.getString("name")!!,
                                i.getString("buildno")!!,
                                i.getString("roadname")!!,
                                i.getString("city")!!,
                                i.getString("pincode")!!,
                                i.getString("state")!!,
                                i.getString("landmark")!!,
                                i.getString("mobileno")!!,
                                i.getString("altmob")!!,
                                i.getString("addresstype")!!
                            )
                            mArratList.add(types)
                        } catch (e: Exception) {
                            Log.d("exe", e.toString())
                        }
                    }
                }
                addressAdapter.update(mArratList)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1234) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("data", "data")
                reqNewData()

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
