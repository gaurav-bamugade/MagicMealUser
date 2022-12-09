package com.example.minipro3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.example.minipro3.databinding.ActivityMainBinding

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var searchFragment: SearchFragment

    lateinit var messageFragment: MessageFragment
    lateinit var authen : FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var homeFragment: HomeFragment

    lateinit var toggle:ActionBarDrawerToggle
    lateinit var toolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar=findViewById(R.id.toolBar)
        toolbar.setTitle("Magic Meal")
        setSupportActionBar(toolbar);
        val drawerLayout:DrawerLayout=findViewById(R.id.drawer_lay)
        val navView:NavigationView=findViewById(R.id.nav_view)


        toggle=ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
      val drawerToggle : ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawer_lay, toolbar, R.string.open, R.string.close){}

        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_lay.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding.bottomNavbar.setOnItemSelectedListener {
            when(it)
            {
                R.id.home-> {
                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.search-> {
                    searchFragment = SearchFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame, searchFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
              /*  R.id.notif-> {
                    notificationFragment= NotificationFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame, notificationFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }*/
                R.id.offerpg-> {
                    messageFragment = MessageFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame, messageFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
        }
        //setupTabBar()
    }
  override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when(menuItem.itemId){
            R.id.home -> {
                homeFragment = HomeFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.foodmenu -> {
                startActivity(Intent(this,FoodMenuActivity::class.java))
            }

            R.id.myorders -> {
               startActivity(Intent(this,MyOrdersActivity::class.java))
            }
            R.id.mycart -> {
                startActivity(Intent(this,MyCartActivity::class.java))
            }
            R.id.feedback -> {
                startActivity(Intent(this,FeedbackActivity::class.java))
            }
            R.id.aboutus -> {
             startActivity(Intent(this,AboutUsActivity::class.java))
            }
            R.id.profile -> {
                startActivity(Intent(this,ProfileActivity::class.java))
            }
            R.id.logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Are you Sure?")
                    setPositiveButton("OK"){ _, _ ->
                        FirebaseAuth.getInstance().signOut()
                        LogOut()
                    }
                    setNegativeButton("Cancel"){ _, _ ->
                    }
                }.create().show()
            }
        }
        drawer_lay.closeDrawer(GravityCompat.START)
        return true
    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(R.menu.side_menu, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item?.itemId){
//            R.id.log_out_main->{
//                AlertDialog.Builder(this).apply {
//                    setTitle("Are you Sure?")
//                    setPositiveButton("OK"){ _, _ ->
//                        FirebaseAuth.getInstance().signOut()
//                        LogOut()
//                    }
//                    setNegativeButton("Cancel"){ _, _ ->
//                    }
//                }.create().show()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onBackPressed() {
        if (drawer_lay.isDrawerOpen(GravityCompat.START)){
            drawer_lay.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }
   private fun LogOut(){
        Toast.makeText(this, "Logged Out!", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}