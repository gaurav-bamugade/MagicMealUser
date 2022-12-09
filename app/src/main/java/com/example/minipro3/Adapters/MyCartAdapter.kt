package com.example.minipro3.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.minipro3.Models.ModelCart
import com.example.minipro3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MyCartAdapter(var con: Context, var list: ArrayList<ModelCart>) :
        RecyclerView.Adapter<myCartViewHolder>() {

    private val userid = FirebaseAuth.getInstance().currentUser!!.uid.toString()
    private var overAllTotalPrice : Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myCartViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(con)
        val v : View = layoutInflater.inflate(R.layout.rv_mycart_items, parent, false)
        return myCartViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<ModelCart>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: myCartViewHolder, position: Int) {
        try {
            val cartItem = list[position]

            val a = cartItem.qtyoffood.toInt()
            val b = cartItem.priceoffood.toInt()

            Glide.with(con).load(cartItem.imageoffood).centerCrop().dontAnimate().into(holder.cart_food_image)
            holder.cart_food_name.text = cartItem.nameoffood
            holder.cart_food_qty.text = cartItem.qtyoffood
            holder.cart_food_price.text = cartItem.priceoffood
            holder.cart_food_total.text = (a * b).toString().trim()

            val tot = a * b
            overAllTotalPrice += tot
            val intent = Intent("totalprice")
            intent.putExtra("OverAllTotalPrice", overAllTotalPrice.toString())
            LocalBroadcastManager.getInstance(con).sendBroadcast(intent)

            holder.cart_food_delete.setOnClickListener {
                val iter = list.iterator()
                while (iter.hasNext()) {
                    if (iter.next().foodid == cartItem.foodid) {
                        iter.remove()
                        update(list)
                        break
                    }
                }
                //For User View
                FirebaseFirestore.getInstance().collection("MagicMeal")
                    .document(userid)
                    .collection("CartList")
                    .document(cartItem.foodid)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("Main", "Removed from cart")
                        Toast.makeText(con, "Removed from Cart", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Log.d("Main", it.message.toString())
                        Toast.makeText(con, "Failed to Remove", Toast.LENGTH_LONG).show()
                    }
                //For Admin View
                FirebaseFirestore.getInstance().collection("MagicMealAdmin")
                    //.document("F0y2F2SeaoWHjY7sIHFr4JRf1HF2")
                    .document("CartList")
                    .collection(userid)
                    .document(cartItem.foodid)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("Main", "Removed from Admin cart")
                        //Toast.makeText(con, "Removed from Cart", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Log.d("Main", it.message.toString())
                        //Toast.makeText(con, "Failed to Remove", Toast.LENGTH_LONG).show()
                    }

            }
        }
        catch (e:Exception){
            e.printStackTrace()
            Log.d("exee",e.toString())
        }
    }

}

class myCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val cart_food_image = itemView.findViewById<ImageView>(R.id.my_cart_food_image)
    val cart_food_name = itemView.findViewById<TextView>(R.id.my_cart_food_name)
    val cart_food_qty = itemView.findViewById<TextView>(R.id.my_cart_food_quantity)
    val cart_food_price = itemView.findViewById<TextView>(R.id.my_cart_food_price)
    val cart_food_total = itemView.findViewById<TextView>(R.id.my_cart_food_total_price)
    val cart_food_delete = itemView.findViewById<ImageView>(R.id.my_cart_delete_food)

}