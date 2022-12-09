package com.example.minipro3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_payment.*
import java.util.*
import kotlin.collections.HashMap

class PaymentActivity : AppCompatActivity() {

    private val fStore = FirebaseFirestore.getInstance()
    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        display_payment_amt.text = intent.extras!!.getString("amt")!!


        payment_mode_cod.setOnClickListener {
            payUsingCOD()
        }

    }

    private fun payUsingCOD() {
        val cod = HashMap<String, Any>()
        cod["paymentmode"] = "COD"
        val ref1 = fStore.collection("MagicMeal").document(userid).collection("CateringOrders")
        ref1.addSnapshotListener { snapshot, e ->
            for(ds in snapshot!!.documents){
                fStore.collection("MagicMeal").document(userid).collection("CateringOrders").document(ds.id)
                    .update(cod)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            fStore.collection("Orders").document(userid).collection("CateringOrders")
                                .document(ds.id).update(cod)
                                .addOnCompleteListener {
                                    if (it.isSuccessful){
                                        Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(applicationContext, MainActivity::class.java))
                                        Log.d("cnf", "Order Placed Successfully")
                                    }
                                }
                        }
                    }
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
