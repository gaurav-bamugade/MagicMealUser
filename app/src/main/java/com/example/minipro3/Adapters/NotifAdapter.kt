package com.example.minipro3.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Models.ModelNotif
import com.example.minipro3.R

class NotifAdapter(var con : Context, var list : ArrayList<ModelNotif>) :
    RecyclerView.Adapter<notifViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notifViewHolder {
        return notifViewHolder(LayoutInflater.from(con).inflate(R.layout.rv_notif_items, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<ModelNotif>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: notifViewHolder, position: Int) {
        try {
            val offersItem = list[position]

            holder.notif_foodcategory.text = offersItem.foodcategory
            holder.notif_foodname.text = offersItem.foodname
            holder.notif_foodmsg.text = offersItem.notifmsg

        }catch (e: Exception){
            e.printStackTrace()
            Log.d("ex", e.message.toString().trim())
        }
    }
}

class notifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var notif_foodcategory = itemView.findViewById<TextView>(R.id.notif_food_category)
    var notif_foodname = itemView.findViewById<TextView>(R.id.notif_food_name)
    var notif_foodmsg = itemView.findViewById<TextView>(R.id.notif_food_message)
}