package com.example.minipro3.Adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.minipro3.ComboProdDetails
import com.example.minipro3.Models.ModelFood
import com.example.minipro3.ProductDetailNew
import com.example.minipro3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ComboOfferAdapter(var con: Context, var list: ArrayList<ModelFood>) :
    RecyclerView.Adapter<ComboviewHolder>() {

    private val userid = FirebaseAuth.getInstance().currentUser!!.uid.toString()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboviewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(con)
        val v : View = layoutInflater.inflate(R.layout.combo_layout_item, parent, false)
        return ComboviewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<ModelFood>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ComboviewHolder, position: Int) {
        val foodItem = list[position]

        Glide.with(con).load(foodItem.imageuri).centerCrop().dontAnimate().into(holder.ig_fd_image)
        holder.tv_fd_name.text = foodItem.foodname
        holder.tv_fd_price.text = foodItem.foodprice


        holder.parent_lay.setOnClickListener {
            val intent = Intent(con, ComboProdDetails::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("object",foodItem)
            con.startActivity(intent)
            //Toast.makeText(con,foodItem.foodname+" Clicked", Toast.LENGTH_LONG).show()
        }

        holder.ig_fd_cart.setOnClickListener {
            val intent = Intent(con, ComboProdDetails::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("object",foodItem)
            con.startActivity(intent)
        }


    }
}

class ComboviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val parent_lay = itemView.findViewById<RelativeLayout>(R.id.parent_rel_lay1)
    val ig_fd_image = itemView.findViewById<ImageView>(R.id.food_image_inside_rv1)

    val ig_fd_cart = itemView.findViewById<ImageView>(R.id.image_view_add_to_cart_inside_rv1)

    val tv_fd_name = itemView.findViewById<TextView>(R.id.txt_food_name_inside_rv1)
    val tv_fd_price = itemView.findViewById<TextView>(R.id.txt_food_price_inside_rv1)

}