package com.example.minipro3.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ColorSpace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Models.ModelAddress
import com.example.minipro3.R
import java.io.Serializable

class AddressAdapter(var con: Context, var list: ArrayList<ModelAddress>, val form_other:String, var activity:AppCompatActivity) :
    RecyclerView.Adapter<AddressView>(), Serializable {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressView {
        val layoutInflater: LayoutInflater = LayoutInflater.from(con)
        val view: View = layoutInflater.inflate(R.layout.rv_address_items, parent, false)
        return AddressView(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<ModelAddress>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AddressView, position: Int) {
        val addressItem = list[position]

        holder.tv_name.text = addressItem.name
        Log.d("name",addressItem.name)
        holder.tv_house.text = addressItem.buildno
        holder.tv_road.text = addressItem.roadname
        holder.tv_landmark.text = addressItem.landmark
        holder.tv_city.text = addressItem.city
        holder.tv_state.text = addressItem.state
        holder.tv_pin_code.text = addressItem.pincode
        holder.tv_mobile.text = addressItem.mobileno

        holder.cv_address.setOnClickListener {
            if(form_other=="checkout") {
                try {
                    val intent = Intent()
                    intent.putExtra("address_object", addressItem)
                    activity.setResult(Activity.RESULT_OK,intent)
                    activity.finish()

                } catch (e: Exception) {
                    Log.d("nul", e.toString())
                }
            }
        }
    }
}

class AddressView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv_name = itemView.findViewById(R.id.name_add) as TextView
    val tv_house = itemView.findViewById(R.id.house_add) as TextView
    val tv_road = itemView.findViewById(R.id.road_add) as TextView
    val tv_landmark = itemView.findViewById(R.id.landmark_add) as TextView
    val tv_city = itemView.findViewById(R.id.city_add) as TextView
    val tv_state = itemView.findViewById(R.id.state_add) as TextView
    val tv_pin_code = itemView.findViewById(R.id.pin_code_add) as TextView
    val tv_mobile = itemView.findViewById(R.id.mob_no_add) as TextView
    val cv_address = itemView.findViewById(R.id.address_card) as CardView
}