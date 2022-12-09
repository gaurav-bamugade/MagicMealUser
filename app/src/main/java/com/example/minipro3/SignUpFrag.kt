package com.example.minipro3

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_signupfrag.*
import kotlinx.android.synthetic.main.fragment_signupfrag.email_id
import kotlinx.android.synthetic.main.fragment_signupfrag.fullname
import kotlinx.android.synthetic.main.fragment_signupfrag.mobilenumber
import kotlinx.android.synthetic.main.fragment_signupfrag.password
import kotlinx.android.synthetic.main.fragment_signupfrag.signupbtn
import kotlinx.android.synthetic.main.fragment_signupfrag.username

class SignUpFrag : Fragment(){
    lateinit var fstore : FirebaseFirestore
    lateinit var authen : FirebaseAuth
    lateinit var docRef : DocumentReference

    private val fStore = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_signupfrag, container, false)




        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authen = FirebaseAuth.getInstance()

        fstore = FirebaseFirestore.getInstance()
        signupbtn.setOnClickListener {
            //startActivity(Intent(this, MainActivity::class.java))
            signUp()
        }

        fullname.setTranslationX(800F)
        username.setTranslationX(800F)
        mobilenumber.setTranslationX(800F)
        email_id.setTranslationX(800F)
        password.setTranslationX(8000F)
        signupbtn.setTranslationX(8000F)

        fullname.setAlpha(0F)
        username.setAlpha(0F)
        mobilenumber.setAlpha(0F)
        email_id.setAlpha(0F)
        password.setAlpha(0F)
        signupbtn.setAlpha(0F)

        fullname.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(300).start()
        username.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()
        mobilenumber.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(700).start()
        email_id.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(900).start()
        password.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(1100).start()
        signupbtn.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(1100).start()


    }
    private fun signUp(){/*
        val fname = findViewById<View>(R.id.fullname) as EditText
        val uname = findViewById<View>(R.id.username) as EditText
        val emails = findViewById<View>(R.id.email_id) as EditText
        val pass = findViewById<View>(R.id.password) as EditText*/
        authen = FirebaseAuth.getInstance()
        val fn = fullname.text.toString()
        val un = username.text.toString()
        val em = email_id.text.toString()
        val ps = password.text.toString()
        val mob = mobilenumber.text.toString()

        if(fn.isEmpty() || un.isEmpty() || em.isEmpty() || ps.isEmpty() || mob.isEmpty()){
            Toast.makeText(context, "Enter Required Credentials ", Toast.LENGTH_LONG ).show()
        }

        if (TextUtils.isEmpty(fn)){
            fullname.error = "Fullname is Required"
            return
        }

        if (TextUtils.isEmpty(un)){
            username.error = "Username is Required"
            return
        }

        if (TextUtils.isEmpty(ps)){
            password.error = "Password is Required"
            return
        }

        if (ps.length < 6){
            password.error = "Password must be at least 6 characters"
            return
        }

        if (TextUtils.isEmpty(em)){
            email_id.error = "Email ID is Required"
            return
        }

        if (TextUtils.isEmpty(mob)){
            mobilenumber.error = "Mobile Number is Required"
            return
        }

        if(mob.length > 10 || mob.length < 10){
            mobilenumber.error = "Invalid Mobile Number Format"
            return
        }
        authen.createUserWithEmailAndPassword(em,ps).addOnCompleteListener {
            if(it.isSuccessful){
                val usernew = HashMap<String, Any>()
                usernew["FullName"] = fn
                usernew["UserName"] = un
                usernew["Email_ID"] = em
                usernew["mobilenumber"] = mob
//                    usernew["Password"] = ps
                docRef = fstore.collection("MagicMeal").document(FirebaseAuth.getInstance().uid.toString())
                    .collection("Users").document("PersonalDetails")
                docRef.set(usernew, SetOptions.merge())
                    .addOnCompleteListener {
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
                        Toast.makeText(context, "Registered Successfully to MagicMeal!!", Toast.LENGTH_LONG).show()
                        Toast.makeText(context, "Welcome to MagicMeal!!", Toast.LENGTH_LONG).show()
                        Log.d(ContentValues.TAG, "UID:${FirebaseAuth.getInstance().uid}")

                        var intent = Intent(activity,MainActivity::class.java)
                       /* intent.putExtra("mob","+91"+mob)*/
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Log.w(ContentValues.TAG, "Error Adding document")
                        Toast.makeText(context, "Failed to Register", Toast.LENGTH_LONG ).show()
                    }

                return@addOnCompleteListener
            }else{
                Log.w(ContentValues.TAG, "Error Adding document")
                Toast.makeText(context, "Failed to Register to Database", Toast.LENGTH_LONG ).show()
            }
        }

    }
}