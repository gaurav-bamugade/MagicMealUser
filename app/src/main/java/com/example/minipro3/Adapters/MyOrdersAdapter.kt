package com.example.minipro3

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Models.ModelOrders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyOrdersAdapter(var con: Context, var list: ArrayList<ModelOrders>) :
    RecyclerView.Adapter<myOrdersViewHolder>() {

    private val fStore = FirebaseFirestore.getInstance()
    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myOrdersViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(con)
        val v : View = layoutInflater.inflate(R.layout.rv_order_items, parent, false)
        return myOrdersViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<ModelOrders>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: myOrdersViewHolder, position: Int) {
        try {
            val orderItem = list[position]

            holder.ord_name.text = orderItem.name
            holder.ord_del_time.text = orderItem.deliverytime
            holder.ord_home.text = orderItem.home
            holder.ord_mobno.text = orderItem.mobno
            holder.ord_city.text = orderItem.city
            holder.ord_ordered_at.text = orderItem.orderedat
            holder.ord_pin.text = orderItem.pin
            holder.ord_road.text = orderItem.road
            holder.ord_state.text = orderItem.state
            holder.ord_tot_price.text = orderItem.totalprice
            holder.status.text = orderItem.statusoforder

            holder.cancel_my_order.setOnClickListener {
                if(holder.status.text == "Left For Delivery"){
                    Toast.makeText(con, "You cannot cancel a Order!!!", Toast.LENGTH_LONG).show()
                    holder.cancel_my_order.visibility = View.GONE
                }
                else if(holder.status.text == "Order Delivered"){
                    Toast.makeText(con, "You cannot cancel a Order!!!", Toast.LENGTH_LONG).show()
                    holder.cancel_my_order.visibility = View.GONE
                }
                else if(holder.status.text == "Order Cancelled"){
                    Toast.makeText(con, "You cannot cancel a Order, its already cancelled!!!", Toast.LENGTH_LONG).show()
                    holder.cancel_my_order.visibility = View.GONE
                }
                else {
                    val cancelStatus = HashMap<String, Any>()
                    cancelStatus["statusoforder"] = "Order Cancelled"

                    fStore.collection("Orders").document(userid).collection("CateringOrders").get()
                        .addOnSuccessListener { data ->
                            for (ds in data.documents) {
                                fStore.collection("Orders").document(userid)
                                    .collection("CateringOrders").document(ds.id.toString())
                                    .update(cancelStatus)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful){
                                            fStore.collection("MagicMeal").document(userid)
                                                .collection("CateringOrders")
                                                .document(ds.id.toString())
                                                .update(cancelStatus)
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful){
                                                        Toast.makeText(con, "Order Cancelled", Toast.LENGTH_LONG).show()
                                                        holder.cancel_my_order.visibility = View.GONE
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
            Log.d("ex",e.message.toString().trim())
        }
    }
}

class myOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val ord_name = itemView.findViewById<TextView>(R.id.order_tv_name)
    val ord_del_time = itemView.findViewById<TextView>(R.id.order_tv_delivery_time)
    val ord_home = itemView.findViewById<TextView>(R.id.order_tv_home)
    val ord_mobno = itemView.findViewById<TextView>(R.id.order_tv_mob_no)
    val ord_city = itemView.findViewById<TextView>(R.id.order_tv_city)
    val ord_ordered_at = itemView.findViewById<TextView>(R.id.order_tv_ordered_at)
    val ord_pin = itemView.findViewById<TextView>(R.id.order_tv_pin_code)
    val ord_road = itemView.findViewById<TextView>(R.id.order_tv_road)
    val ord_state = itemView.findViewById<TextView>(R.id.order_tv_state)
    val ord_tot_price = itemView.findViewById<TextView>(R.id.order_tv_total_amt)
    //val ord_show_btn = itemView.findViewById<Button>(R.id.show_this_order_foods)
    val cancel_my_order = itemView.findViewById<TextView>(R.id.cancel_my_order)
    val status = itemView.findViewById<TextView>(R.id.my_orders_tv_status)
}