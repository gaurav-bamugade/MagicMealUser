package com.example.minipro3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Adapters.FoodAdapter
import com.example.minipro3.Models.ModelFood
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VegLunchActivity : AppCompatActivity() {

    private val fStore = FirebaseFirestore.getInstance()
    lateinit var recylerView: RecyclerView
    lateinit var foodAdapter: FoodAdapter
    private val mArrayList: ArrayList<ModelFood> = ArrayList()
    private val adminID = "KKpL3gQiy8Ps7x5CdobOn5uxTGl1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veg_lunch)

        /*val actionBar = supportActionBar
        actionBar!!.title = "Veg Lunch"

        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
*/
        val retData = fStore.collection("MagicMealAdmin").document(adminID).collection("VegLunch")
        retData.addSnapshotListener { snap, e ->
            if (snap != null) {
                for (i in snap.documentChanges) {
                    if (i.document.exists()) {
                        try {
                            val types: ModelFood = ModelFood(
                                i.document.getString("imageuri")!!,
                                i.document.getString("foodname")!!,
                                i.document.getString("foodprice")!!,
                                i.document.getString("foodofferprice")!!,
                                i.document.getString("fooddescription")!!,
                                i.document.getString("foodcategory")!!,
                                i.document.getString("foodid")!!

                            )
                            types.set(i.document.id)
                            mArrayList.add(types)
                        } catch (e: Exception) {
                            Log.d("exe", e.toString())
                        }
                    }
                }
                foodAdapter.update(mArrayList)
                checkfav()
            }
        }

        recylerView = findViewById(R.id.recyler_view_veglunch)
        recylerView.setHasFixedSize(true)
        val layoutmanager = LinearLayoutManager(this)
        recylerView.layoutManager = layoutmanager
        foodAdapter = FoodAdapter(applicationContext, mArrayList)
        recylerView.adapter = foodAdapter

    }

    fun checkfav() {
        if (mArrayList.isNotEmpty()) {
            val userid = FirebaseAuth.getInstance().currentUser!!.uid.toString()
            val ref = FirebaseFirestore.getInstance().collection("MagicMeal")
                .document(userid)
                .collection("Favorites")
            ref.addSnapshotListener { snap, e ->
                if (snap != null) {
                    for (i in snap!!.documentChanges) {
                        val id = i.document.id
                        mArrayList.forEach { d ->
                            if (id == d.foodid) {
                                d.setl(1)
                            }
                        }
                    }
                    foodAdapter.update(mArrayList)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
