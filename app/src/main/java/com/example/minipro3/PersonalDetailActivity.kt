package com.example.minipro3

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_personal_detail.*
import kotlinx.android.synthetic.main.fragment_signupfrag.*

class PersonalDetailActivity : AppCompatActivity() {
    lateinit var fstore : FirebaseFirestore
    lateinit var authen : FirebaseAuth
    lateinit var docRef : DocumentReference
    private val fStore = FirebaseFirestore.getInstance()
    lateinit var db: FirebaseAuth
    lateinit var valid : DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_detail)
        db = FirebaseAuth.getInstance()



        val docRef = fStore.collection("MagicMeal").document(FirebaseAuth.getInstance().currentUser!!.uid.toString())
            .collection("Users").document("PersonalDetails")
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot!=null) {
                if(snapshot!!.exists()){
                    startActivity(Intent(this@PersonalDetailActivity,MainActivity::class.java))
                }

            }
            else
            {
                Toast.makeText(this@PersonalDetailActivity,"Please Fill The Details",Toast.LENGTH_SHORT).show()
            }
        }

      /*  if(db.currentUser!=null)
        {
            valid=fstore.collection("MagicMeal").document(FirebaseAuth.getInstance().uid.toString())
                .collection("Users").document("PersonalDetails")
            valid.get()
                .addOnSuccessListener { document ->
                    if (db.currentUser)
                    {
                       startActivity(Intent(this@PersonalDetailActivity,MainActivity::class.java))
                    }
                }
        }*/

        phone_signupbtn.setOnClickListener {

            signup()
            finish()
        }
    }
    private fun signup(){
        authen = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val fn = phone_fullname.text.toString()
        val un = phone_username.text.toString()
        val em = phone_email_id.text.toString()


        if(fn.isEmpty() || un.isEmpty() || em.isEmpty() || em.isEmpty() ){
            Toast.makeText(this@PersonalDetailActivity, "Enter Required Credentials ", Toast.LENGTH_LONG ).show()
        }

        if (TextUtils.isEmpty(fn)){
            fullname.error = "Fullname is Required"
            return
        }

        if (TextUtils.isEmpty(un)){
            username.error = "Username is Required"
            return
        }
        if (TextUtils.isEmpty(em)){
            email_id.error = "Email ID is Required"
            return
        }

            val usernew = HashMap<String, Any>()
            usernew["FullName"] = fn
            usernew["UserName"] = un
            usernew["Email_ID"] = em

            docRef = fstore.collection("MagicMeal").document(FirebaseAuth.getInstance().uid.toString())
                .collection("Users").document("PersonalDetails")
            docRef.set(usernew, SetOptions.merge()).addOnCompleteListener {
                    if (it.isSuccessful){
                        fStore.collection("Users").document("AllUsers").get()
                            .addOnSuccessListener { data->
                                if (data.exists()){
                                    fStore.collection("Users").document("AllUsers")
                                        .update(
                                            "TotalList",
                                            FieldValue.arrayUnion(FirebaseAuth.getInstance().uid.toString())
                                        ).addOnSuccessListener {
                                            Log.d("ffd", FirebaseAuth.getInstance().uid.toString())
                                        }
                                }
                                else{
                                    fStore.collection("Users").document("AllUsers")
                                        .set(
                                            hashMapOf(
                                                "TotalList" to arrayListOf(FirebaseAuth.getInstance().uid.toString())
                                            )
                                        ).addOnSuccessListener {
                                            Log.d("ffd", FirebaseAuth.getInstance().uid.toString())
                                        }
                                }
                            }
                    }
                }
                .addOnSuccessListener {
                    Toast.makeText(this@PersonalDetailActivity, "Registered Successfully to MagicMeal!!", Toast.LENGTH_LONG).show()
                    Toast.makeText(this@PersonalDetailActivity, "Welcome to MagicMeal!!", Toast.LENGTH_LONG).show()
                    Log.d(ContentValues.TAG, "UID:${FirebaseAuth.getInstance().uid}")

                    var intent = Intent(this@PersonalDetailActivity,MainActivity::class.java)
                    /* intent.putExtra("mob","+91"+mob)*/
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.w(ContentValues.TAG, "Error Adding document")
                    Toast.makeText(this@PersonalDetailActivity, "Failed to Register", Toast.LENGTH_LONG ).show()
                }


        }


}