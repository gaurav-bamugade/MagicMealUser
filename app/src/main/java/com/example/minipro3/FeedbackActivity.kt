package com.example.minipro3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : AppCompatActivity() {

    private val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val fStore = FirebaseFirestore.getInstance()
    private val adminID = "KKpL3gQiy8Ps7x5CdobOn5uxTGl1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val actionBar = supportActionBar
//        actionBar!!.title = "Feedback"

  //      actionBar.setDisplayHomeAsUpEnabled(true)
   //     actionBar.setDisplayHomeAsUpEnabled(true)

        feedback_rating.setOnRatingBarChangeListener { _, fl, _ ->
            AlertDialog.Builder(this).apply {
                setTitle("You Have Rated : $fl")
                setPositiveButton("OK"){ _, _ ->
                }
//                setNegativeButton("Cancel"){ _, _ ->
//                }
            }.create().show()
        }

        feedback_submit.setOnClickListener {
            feedback_submit()
        }
    }

    private fun feedback_submit(){
        val feed_name = feedback_user_name.text.toString().trim()
        val feed_email = feedback_email_id.text.toString().trim()
        val feed_message = feedback_message.text.toString().trim()
        val feed_rating = feedback_rating.rating.toString()
        val feed_mob = feedback_mobile_no.text.toString().trim()

        if(TextUtils.isEmpty(feed_name)){ feedback_user_name.error = "Should not be Empty"}
        if(TextUtils.isEmpty(feed_email)){ feedback_email_id.error = "Should not be Empty"}
        if(TextUtils.isEmpty(feed_message)){ feedback_message.error = "Should not be Empty"}
        if(TextUtils.isEmpty(feed_mob)){ feedback_mobile_no.error = "Should not be Empty"}

        if(feed_name.isEmpty() || feed_email.isEmpty() || feed_message.isEmpty() || feed_mob.isEmpty()){
            Toast.makeText(this, "Enter Required Credentials", Toast.LENGTH_LONG).show()
        }else{
            try {
                feedback_progress_bar.visibility = View.VISIBLE
                val feedData = HashMap<String, Any>()
                feedData["name"] = feed_name
                feedData["emailid"] = feed_email
                feedData["message"] = feed_message
                feedData["rating"] = feed_rating
                feedData["mobileno"] = feed_mob

                val feedRef = fStore.collection("MagicMealAdmin").document(adminID)
                    .collection("Feedback").document(userID)
                feedRef.set(feedData, SetOptions.merge())
                    .addOnSuccessListener {
                        feedback_progress_bar.visibility = View.GONE
                        Toast.makeText(this, "Feedback Recorded Successfully", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener{e->
                        Toast.makeText(this, "Failed to Add", Toast.LENGTH_LONG).show()
                        Log.d("Main", e.toString())
                    }

            }catch (e: Exception){
                Log.d("Main",e.toString())
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
