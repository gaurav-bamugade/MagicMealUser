package com.example.minipro3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_food_menu.*

class FoodMenuActivity : AppCompatActivity() {


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_menu)

        //val actionBar = supportActionBar
        //actionBar!!.title = "Food Menu"

      //  actionBar.setDisplayHomeAsUpEnabled(true)
      //  actionBar.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(R.drawable.new_breakfast).centerCrop().dontAnimate().into(image_view_breakfast)

        Glide.with(this).load(R.drawable.new_veglunch).centerCrop().dontAnimate().into(image_view_veglunch)

        Glide.with(this).load(R.drawable.new_nonveg).centerCrop().dontAnimate().into(image_view_nonveglunch)

        Glide.with(this).load(R.drawable.new_snacks).centerCrop().dontAnimate().into(image_view_snacks)
        Glide.with(this).load(R.drawable.burgerbg).centerCrop().dontAnimate().into(image_view_burger)
        Glide.with(this).load(R.drawable.pizabg).centerCrop().dontAnimate().into(image_view_pizza)

       image_view_breakfast.setOnClickListener {
            startActivity(Intent(this, BreakfastActivity::class.java))
        }

        image_view_veglunch.setOnClickListener {
            startActivity(Intent(this, VegLunchActivity::class.java))
        }

        image_view_nonveglunch.setOnClickListener {
            startActivity(Intent(this, NonVegActivity::class.java))
        }

        image_view_snacks.setOnClickListener {
            startActivity(Intent(this, SnacksActivity::class.java))
        }
        image_view_burger.setOnClickListener{
            startActivity(Intent(this, BurgerActivity::class.java))

        }
        image_view_pizza.setOnClickListener{
            startActivity(Intent(this, PizzaActivity::class.java))

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
