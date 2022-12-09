package com.example.minipro3.Adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.minipro3.Models.OnBoarding
import com.example.minipro3.R

class ViewIntroPGAda(private var context:Context,private var onBoardingDataList:List<OnBoarding>) : PagerAdapter() {
    override fun getCount(): Int {
      return onBoardingDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=LayoutInflater.from(context).inflate(R.layout.onboarditem,null)
        val imageView:ImageView
        val title:TextView
        val desc:TextView

        imageView=view.findViewById(R.id.imgv)
        title=view.findViewById(R.id.title)
        desc=view.findViewById(R.id.desc)

        imageView.setImageResource(onBoardingDataList[position].ImageUrl)
        title.text=onBoardingDataList[position].title
        desc.text=onBoardingDataList[position].desc
        container.addView(view)
        return view
    }

}