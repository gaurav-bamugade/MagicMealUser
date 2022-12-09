package com.example.minipro3.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Models.ModelOffers
import com.example.minipro3.R

class OffersAdapter(var con : Context, var list : ArrayList<ModelOffers>) :
    RecyclerView.Adapter<offersViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): offersViewHolder {
        return offersViewHolder(LayoutInflater.from(con).inflate(R.layout.rv_offers_items, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<ModelOffers>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: offersViewHolder, position: Int) {
        try {
            val offersItem = list[position]

            holder.foodcategory.text = offersItem.foodcategory
            holder.foodname.text = offersItem.foodname
            holder.foodoffer.text = offersItem.foodoffer
            holder.foodprice.text = offersItem.foodprice

        }catch (e: Exception){
            e.printStackTrace()
            Log.d("ex", e.message.toString().trim())
        }
    }
}

class offersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var foodcategory = itemView.findViewById<TextView>(R.id.offer_food_category)
    var foodname = itemView.findViewById<TextView>(R.id.offer_food_name)
    var foodoffer = itemView.findViewById<TextView>(R.id.offer_food_offer_price)
    var foodprice = itemView.findViewById<TextView>(R.id.offer_food_price)
}