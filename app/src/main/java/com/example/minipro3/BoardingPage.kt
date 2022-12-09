package com.example.minipro3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.minipro3.Adapters.ViewIntroPGAda
import com.example.minipro3.Models.OnBoarding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_boarding_page.*

class BoardingPage : AppCompatActivity() {
    var onBoardingViewPagerADapter: ViewIntroPGAda?=null
    var tablayout: TabLayout?=null
    lateinit var onBoardingBiewpg: ViewPager
    //var next:TextView?=null
    var position=0
    var sharedPreferences: SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boarding_page)
        tablayout=findViewById(R.id.tablau)
        //next=findViewById(R.id.nextbntn)
        val onBoarding:MutableList<OnBoarding> = ArrayList()
        onBoarding.add(OnBoarding("Hello Foodie","Every Bite Takes You Home. Easiest Way To Order Food",R.drawable.trasnpfirst))
        onBoarding.add(OnBoarding("Fast Delivery","Fast Delivery With Hot Meals",R.drawable.delivery))
        onBoarding.add(OnBoarding("Great Discounts","Great Amount Of Discounts Of Your Favorite Food",R.drawable.offerpageboard))

        setOnboardViewPageADa(onBoarding)
        if(restorePrefData())
        {
            //val i= Intent(applicationContext,homeac::class.java)
            //startActivity(i)
        }
        position=onBoardingBiewpg!!.currentItem
        nextbntn.setOnClickListener{
            if(position<onBoarding.size)
            {
                position++
                onBoardingBiewpg!!.currentItem=position
            }
            if(position==onBoarding.size)
            {
                savePreDa()
                val i= Intent(applicationContext,LoginActivity::class.java)
                startActivity(i)
            }
        }

        tablayout!!.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position=tab!!.position
                if(tab.position==onBoarding.size-1)
                {
                    nextbntn!!.text="Get Started"
                }
                else
                {
                    nextbntn!!.text="Next"
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
    private fun setOnboardViewPageADa(onBoarding: List<OnBoarding>)
    {
        onBoardingBiewpg=findViewById(R.id.screnPager)
        onBoardingViewPagerADapter= ViewIntroPGAda(this,onBoarding)
        onBoardingBiewpg!!.adapter=  onBoardingViewPagerADapter
        tablayout!!.setupWithViewPager(onBoardingBiewpg)

    }
    private fun savePreDa()
    {
        sharedPreferences=applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreferences!!.edit()
        editor!!.putBoolean("isFirstTimeRun",true)
        editor.apply()
    }
    private fun restorePrefData():Boolean
    {
        sharedPreferences= applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRun",false)
    }
}