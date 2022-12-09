package com.example.minipro3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide

import com.example.minipro3.Models.ModelFood
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_product_detail_new.*
import java.util.*

class ProductDetailNew : AppCompatActivity() {
    private lateinit var food_image : ImageView
    private lateinit var food_name : TextView
    private lateinit var food_price : TextView
    //private lateinit var food_quantity : ElegantNumberButton
    private lateinit var food_desc : TextView
    private lateinit var food_cart_btn : LinearLayout
    private lateinit var food_category : TextView
    lateinit var img : String
    lateinit var offer : String
    private val fStore = FirebaseFirestore.getInstance()
    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    private var fid : String? = null

    private var totalquantity = 1
    private var stockCtn:TextView?=null

    private var plusbtn: ImageView? = null
    private  var minusbtn:ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail_new)
        val i = intent
        val m  = i.getSerializableExtra("object") as ModelFood
        fid = m.foodid
        Log.d("Main", fid.toString())

      //  val actionBar = supportActionBar
//        actionBar!!.title = m.foodname

       // actionBar.setDisplayHomeAsUpEnabled(true)
       // actionBar.setDisplayHomeAsUpEnabled(true)

        food_image = findViewById(R.id.product_details_food_image)
        food_name = findViewById(R.id.product_details_food_name)
        food_price = findViewById(R.id.product_details_food_price)
        //food_quantity = findViewById(R.id.product_details_food_quantity)
        food_desc = findViewById(R.id.product_details_food_description)
        food_cart_btn = findViewById(R.id.product_details_button)
        food_category = findViewById(R.id.product_details_food_category)



        stockCtn=findViewById(R.id.stockCt)
        plusbtn = findViewById<ImageView>(R.id.plus)
        minusbtn = findViewById<ImageView>(R.id.minus)


        food_name.text = m.foodname
        Log.d("Main", food_name.text.toString().trim())
        food_price.text = m.foodprice
        food_desc.text = m.fooddescription
        food_category.text = m.foodcategory
        img = m.imageuri
        Log.d("Main", "Image URL : $img")
        offer = m.foodofferprice
        Log.d("Main", "Offer : $offer")
        Glide.with(this).load(m.imageuri).centerCrop().dontAnimate().into(food_image)

        plusbtn?.setOnClickListener(View.OnClickListener {
            if (totalquantity < 10) {
                totalquantity++
                stockCtn?.setText(totalquantity.toString())
            }
        })
        minusbtn?.setOnClickListener(View.OnClickListener {
            if (totalquantity > 0) {
                totalquantity--
                stockCtn?.setText(totalquantity.toString())
            }
        })

        food_cart_btn.setOnClickListener {
            addToCart()
        }
    }
    private fun addToCart() {
        val fd_name = food_name.text.toString().trim()
        val fd_price = food_price.text.toString().trim()
        val fd_desc = food_desc.text.toString().trim()
        val fd_cat = food_category.text.toString().trim()
        Log.d("Main", "Category : $fd_cat")
       val fd_qty = totalquantity.toString().trim()
        val fd_img = img
        val fd_offer = offer

        product_details_progress_bar.visibility = View.VISIBLE

        try {
            val cartData = HashMap<String, Any>()
            cartData["nameoffood"] = fd_name
            cartData["priceoffood"] = fd_price
            cartData["descoffood"] = fd_desc
            cartData["categoryoffood"] = fd_cat
            cartData["qtyoffood"] = fd_qty
            cartData["imageoffood"] = fd_img
            cartData["offeroffood"] = fd_offer
            cartData["foodid"]=fid.toString()

            val ref = fStore.collection("MagicMeal").document(userid).collection("CartList").document(fid!!)
            ref.set(cartData, SetOptions.merge())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val mRef = fStore.collection("MagicMealAdmin")
                            .document("CartList")
                            .collection(userid)
                            .document(fid!!)
                        mRef.set(cartData, SetOptions.merge())
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    product_details_progress_bar.visibility = View.INVISIBLE
                                    Toast.makeText(this, "$fd_name added to Cart", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                    Log.d("Main", "$fd_name added to Cart")
                                } else {
                                    Toast.makeText(this, "$fd_name Failed to add into Cart", Toast.LENGTH_LONG).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, it.message.toString().trim(), Toast.LENGTH_LONG).show()
                                Log.d("Main", it.message.toString().trim())
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message.toString().trim(), Toast.LENGTH_LONG).show()
                    Log.d("Main", it.message.toString().trim())
                }

        }catch (e: Exception){
            Log.d("Main", e.message.toString().trim())
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}