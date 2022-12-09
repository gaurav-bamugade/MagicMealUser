package com.example.minipro3.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Models.ModelHomeOrders
import com.example.minipro3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class MyHomeOrdersAdapter(var con: Context, var list: ArrayList<ModelHomeOrders>, var activity: AppCompatActivity) :
    RecyclerView.Adapter<homeOrdersViewHolder>()
{
    private val fStore = FirebaseFirestore.getInstance()
    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeOrdersViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(con)
        val v: View = layoutInflater.inflate(R.layout.rv_home_orders_item, parent, false)
        return homeOrdersViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<ModelHomeOrders>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: homeOrdersViewHolder, position: Int) {
        try {
            val homeOrderItem = list[position]

            holder.deliveryaddress.text = homeOrderItem.deliveryaddress
            holder.deliverytime.text = homeOrderItem.deliverytime
            holder.mobileno.text = homeOrderItem.mobileno
            holder.name.text = homeOrderItem.name
            holder.ordered_at.text = homeOrderItem.ordered_at
            holder.pickupaddress.text = homeOrderItem.pickupaddress
            holder.status.text = homeOrderItem.statusoforder
            holder.subscription.text = homeOrderItem.subscription
            holder.total_price.text = homeOrderItem.price

            holder.cancel_order.setOnClickListener {
                if(holder.status.text == "Left For Delivery" || holder.status.text == "Order Delivered"){
                    Toast.makeText(con, "You cannot cancel a Order!!!", Toast.LENGTH_LONG).show()
                    holder.cancel_order.visibility = View.GONE
                }
                else if(holder.status.text == "Order Delivered"){
                    holder.cancel_order.visibility = View.GONE
                }
                else if(holder.status.text == "Order Cancelled"){
                    Toast.makeText(con, "You cannot cancel a Order, its already cancelled!!!", Toast.LENGTH_LONG).show()
                    holder.cancel_order.visibility = View.GONE
                }
                else {
                    val cancelStatus = HashMap<String, Any>()
                    cancelStatus["statusoforder"] = "Order Cancelled"

                    fStore.collection("Orders").document(userid).collection("HomeOrders").get()
                        .addOnSuccessListener { data ->
                            for (ds in data.documents) {
                                fStore.collection("Orders").document(userid)
                                    .collection("HomeOrders").document(ds.id.toString())
                                    .update(cancelStatus)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful){
                                            fStore.collection("HotBox").document(userid).collection("HomeOrders")
                                                .document(ds.id.toString())
                                                .update(cancelStatus)
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful){
                                                        Toast.makeText(con, "Order Cancelled", Toast.LENGTH_LONG).show()
                                                        holder.cancel_order.visibility = View.GONE
                                                    }
                                                }
                                        }
                                    }
                            }
                        }
                }
            }

        }catch (e: Exception){
            e.printStackTrace()
            Log.d("ex", e.message.toString().trim())
        }
    }
}

class homeOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val deliveryaddress = itemView.findViewById<TextView>(R.id.my_home_orders_tv_delivery_add)
    val deliverytime = itemView.findViewById<TextView>(R.id.my_home_orders_tv_delivery_time)
    val mobileno = itemView.findViewById<TextView>(R.id.my_home_orders_tv_mob_no)
    val name = itemView.findViewById<TextView>(R.id.my_home_orders_tv_name)
    val ordered_at = itemView.findViewById<TextView>(R.id.my_home_orders_tv_ordered_at)
    val pickupaddress = itemView.findViewById<TextView>(R.id.my_home_orders_tv_home_add)
    val subscription = itemView.findViewById<TextView>(R.id.my_home_orders_tv_subscription)
    val status = itemView.findViewById<TextView>(R.id.my_home_orders_tv_status)
    val card = itemView.findViewById<CardView>(R.id.my_home_orders_card)
    val cancel_order = itemView.findViewById<TextView>(R.id.cancel_my_home_order)
    val total_price = itemView.findViewById<TextView>(R.id.my_home_orders_tv_total_price)
}