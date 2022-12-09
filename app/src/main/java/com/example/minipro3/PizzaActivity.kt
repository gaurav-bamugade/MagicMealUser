package com.example.minipro3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Adapters.FoodAdapter
import com.example.minipro3.Models.ModelFood
import com.google.firebase.firestore.FirebaseFirestore

class PizzaActivity : AppCompatActivity() {
    private val fStore = FirebaseFirestore.getInstance()
    lateinit var recylerView: RecyclerView
    lateinit var foodAdapter: FoodAdapter
    private val mArrayList: ArrayList<ModelFood> = ArrayList()
    private val adminID = "KKpL3gQiy8Ps7x5CdobOn5uxTGl1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizza)
        val retData = fStore.collection("MagicMealAdmin").document(adminID)
            .collection("Pizza")
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

            }
        }

        recylerView = findViewById(R.id.recyler_view_pizza)
        recylerView.setHasFixedSize(true)
        val layoutmanager = LinearLayoutManager(this)
        recylerView.layoutManager = layoutmanager
        foodAdapter = FoodAdapter(applicationContext, mArrayList)
        recylerView.adapter = foodAdapter
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
