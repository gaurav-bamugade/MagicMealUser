package com.example.minipro3.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.*
import com.example.minipro3.Models.CategoryModel

class CategoryAdapter(private val exmplist:ArrayList<CategoryModel>,var context:Context): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()
{
    class CategoryViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {
        var catImg:ImageView=itemview.findViewById(R.id.catImg)
        var catTxt:TextView=itemview.findViewById(R.id.catTxt)
        var card:ConstraintLayout=itemview.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
       val v=LayoutInflater.from(parent.context).inflate(R.layout.category_item_layout,parent,false)

        return CategoryViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem=exmplist[position]
        holder.catImg.setImageResource(currentItem.catImg)
        holder.catTxt.text=currentItem.catTxt
        holder.card.setOnClickListener {
            if(position == 0){
                val intent= Intent(context,ComboOffer::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 1){
                val intent= Intent(context,NonVegActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 2){
                val intent= Intent(context,VegLunchActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 3){
                val intent= Intent(context,SnacksActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 4){
                val intent= Intent(context,BreakfastActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 5){
                val intent= Intent(context,BurgerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 6){
                val intent= Intent(context,PizzaActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }


        }

    }

    override fun getItemCount(): Int {
        return exmplist.size
    }
}