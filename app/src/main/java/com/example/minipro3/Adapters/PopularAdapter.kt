package com.example.minipro3.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.minipro3.Models.CategoryModel
import com.example.minipro3.Models.ModelFood
import com.example.minipro3.ProductDetailNew
import com.example.minipro3.R
import kotlinx.android.synthetic.main.home_frag_scroll_layout.*

class PopularAdapter(var exmplist:ArrayList<ModelFood>,var context: Context): RecyclerView.Adapter<PopularAdapter.PopularViewHolder>()
{
    class PopularViewHolder(itemview: View):RecyclerView.ViewHolder(itemview)
    {
        var catImg: ImageView =itemview.findViewById(R.id.prod_imgs)
        var catTxt: TextView =itemview.findViewById(R.id.prod_namess)
        var price:TextView=itemview.findViewById(R.id.prod_pricess)
        var card:CardView=itemView.findViewById(R.id.popCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.prod_card_items,parent,false)

        return PopularViewHolder(v)
    }
    fun update(list: ArrayList<ModelFood>) {
        this.exmplist = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val currentItem=exmplist[position]

        Glide.with(context).load(currentItem.imageuri).placeholder(R.drawable.burger).into(holder.catImg)
        holder.catTxt.text=currentItem.foodname
        holder.price.text=currentItem.foodprice

        holder.card.setOnClickListener {
            val intent = Intent(context, ProductDetailNew::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("object",currentItem)
            context.startActivity(intent)
            //Toast.makeText(con,foodItem.foodname+" Clicked", Toast.LENGTH_LONG).show()
        }



    }

    override fun getItemCount(): Int {
        return exmplist.size
    }

}