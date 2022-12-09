package com.example.minipro3.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.minipro3.LoginTab
import com.example.minipro3.SignUpFrag


internal class LoginAdapter (var context: Context, fm: FragmentManager, var totalTabs: Int) : FragmentPagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    LoginTab()
                }
                1 -> {
                    SignUpFrag()
                }
                else -> getItem(position)
            }
        }

    override fun getCount(): Int {
        return totalTabs!!
    }
}