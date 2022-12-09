package com.example.minipro3

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.minipro3.Adapters.CategoryAdapter
import com.example.minipro3.Adapters.FoodAdapter
import com.example.minipro3.Adapters.PopularAdapter
import com.example.minipro3.Models.CategoryModel
import com.example.minipro3.Models.ModelFood

import com.example.minipro3.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_frag_scroll_layout.*
import kotlinx.android.synthetic.main.home_item_layout.*

import java.util.*


@Suppress("SENSELESS_COMPARISON")
class HomeFragment : Fragment() {

    private val adminID ="KKpL3gQiy8Ps7x5CdobOn5uxTGl1"

    lateinit var dots_layout: LinearLayout
    lateinit var mPager: ViewPager
    var path: IntArray = intArrayOf(
        R.drawable.bg1,
        R.drawable.bg2,
        R.drawable.bg3,
        R.drawable.bg4
    )


    private val fStore = FirebaseFirestore.getInstance()
    lateinit var recylerView: RecyclerView
    lateinit var foodAdapter: PopularAdapter
    private var mArrayList: ArrayList<ModelFood> =ArrayList()

    lateinit var dots: Array<ImageView>
    lateinit var adapter: PageView
    var currentPage: Int = 0
    lateinit var timer: Timer
    private val DELAY_MS: Long = 1500
    private val PERIOD_MS: Long = 1500


    private var rc:RecyclerView ? =null
    private var catls = ArrayList<CategoryModel>()
    private lateinit var catada:CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val retv = inflater.inflate(R.layout.fragment_home, container, false)


        return retv
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkConnection()) {

            Toast.makeText(requireContext().applicationContext, "Internet is Connected", Toast.LENGTH_LONG).show()
            relative_layout_main_home.visibility = View.VISIBLE
            relative_layout_no_internet_home.visibility = View.GONE
            val inc_lay = view.findViewById<View>(R.id.include_lay_home)

            val retData = fStore.collection("MagicMealAdmin").document(adminID).collection("Popular")

            retData.addSnapshotListener { snap, e ->
                if (snap != null) {
                    for (i in snap.documentChanges) {
                        if (i.document.exists()) {
                            try {
                                val a: ModelFood = ModelFood(
                                    i.document.getString("imageuri")!!,
                                    i.document.getString("foodname")!!,
                                    i.document.getString("foodprice")!!,
                                    i.document.getString("foodofferprice")!!,
                                    i.document.getString("fooddescription")!!,
                                    i.document.getString("foodcategory")!!,
                                    i.document.getString("foodid")!!
                                )
                                Log.d("foodnam", i.document.getString("imageuri").toString())

                                mArrayList.add(a)

                            } catch (e: Exception) {
                                Log.d("exe", e.toString())
                            }
                        }
                    }
                    foodAdapter.update(mArrayList)
                }
            }

            popRc.setHasFixedSize(true)
            val layoutmanager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
            popRc.layoutManager=layoutmanager
            foodAdapter = PopularAdapter( mArrayList,requireContext())
            Log.d("popls", PopularAdapter( mArrayList,requireContext()).toString())
            popRc.adapter = foodAdapter


            catls= ArrayList()
            catls = items()
            catls.reverse()
            catada= CategoryAdapter(catls,requireContext())
            categoryRC.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            )
            categoryRC.itemAnimator = DefaultItemAnimator()
            categoryRC.adapter = catada

            mPager = view.findViewById(R.id.view_pager) as ViewPager
            adapter = PageView(requireContext(), path)
            mPager.adapter = adapter
            dots_layout = view.findViewById(R.id.dotsLayout) as LinearLayout
            createDots(0)
            updatePage()
            mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    currentPage = position
                    try {
                        createDots(position)
                    }
                    catch (e: NullPointerException) {
                    }
                }
            })

        } else {
            Toast.makeText(requireContext().applicationContext, "Internet is Not Connected", Toast.LENGTH_LONG).show()
            relative_layout_main_home.visibility = View.GONE
            relative_layout_no_internet_home.visibility = View.VISIBLE
        }

        b1.setOnClickListener {
            pb.visibility = View.VISIBLE
            if (checkConnection()){
                relative_layout_main_home.visibility = View.VISIBLE
                relative_layout_no_internet_home.visibility = View.GONE
                pb.visibility = View.GONE


                mPager = view.findViewById(R.id.view_pager) as ViewPager
                adapter = PageView(requireContext(), path)
                mPager.adapter = adapter
                dots_layout = view.findViewById(R.id.dotsLayout) as LinearLayout
                createDots(0)
                updatePage()
                mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    }

                    override fun onPageSelected(position: Int) {
                        currentPage = position
                        try {
                            createDots(position)
                        }
                        catch (e: NullPointerException) {
                        }
                    }
                })
            }
            else{
                relative_layout_main_home.visibility = View.GONE
                relative_layout_no_internet_home.visibility = View.VISIBLE
                pb.visibility = View.GONE
            }
        }

    }

    private fun checkConnection(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null) {
                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }

    private fun updatePage() {
        val handler = Handler()
        val Update: Runnable = Runnable {
            if (currentPage == path.size) {
                currentPage = 0
            }
            mPager.setCurrentItem(currentPage++, true)
        }
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    fun createDots(position: Int) {
        if (dots_layout != null) {
            dots_layout.removeAllViews()
        }
        dots = Array(path.size) { ImageView(context) }

        for (i in 0..path.size - 1) {
            dots[i] = ImageView(context)
            if (i == position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.active_dots))
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.inactive_dots))
            }

            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(4, 0, 4, 0)
            dots_layout.addView(dots[i], params)

        }
    }

    private fun items(): ArrayList<CategoryModel>  {
        var arrayList: ArrayList<CategoryModel> = ArrayList()

        arrayList.add(CategoryModel(R.drawable.piz, "Pizza"))
        arrayList.add(CategoryModel(R.drawable.burger, "Burger"))
        arrayList.add(CategoryModel(R.drawable.breakfastpng, "Break Fast"))
        arrayList.add(CategoryModel(R.drawable.snack, "snack"))
        arrayList.add(CategoryModel(R.drawable.vegie, "Veg"))
        arrayList.add(CategoryModel(R.drawable.nonvegies, "Non Veg"))
        arrayList.add(CategoryModel(R.drawable.combooffer, "Combo Offer"))
        return arrayList
    }

}
