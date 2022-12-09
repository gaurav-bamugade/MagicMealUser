package com.example.minipro3

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.minipro3.Adapters.LoginAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mikhaellopez.circularimageview.CircularImageView
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var tablayout: TabLayout
    lateinit var viewpg: ViewPager
    lateinit var floatActionBtn:CircularImageView
    lateinit var phoneBtn:CircularImageView
    lateinit var db: FirebaseAuth
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var googleSignInClient:GoogleSignInClient
    lateinit var authen : FirebaseAuth
    private val fStore = FirebaseFirestore.getInstance()
    private val fAuth = FirebaseAuth.getInstance()
    private companion object{
        private const val RC_SIGN_IN=100
        private const val TAG="GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginnew)


        tablayout=findViewById(R.id.tab_layoyut)
       viewpg=findViewById(R.id.viewpger)
        floatActionBtn=findViewById(R.id.fab_google)
        db = FirebaseAuth.getInstance()
        phoneBtn=findViewById(R.id.fab_phone)

        tablayout!!.addTab(tablayout!!.newTab().setText("Login"))
        tablayout!!.addTab(tablayout!!.newTab().setText("Sign Up"))
        tablayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = LoginAdapter(this, supportFragmentManager,
            tablayout.tabCount)
       viewpg.adapter = adapter

        viewpg.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))
        floatActionBtn.setTranslationY(300F)
        tablayout.setTranslationY(300F)
        phoneBtn.setTranslationY(300F)

        floatActionBtn.setAlpha(0F)
        floatActionBtn.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(400).start()
        tablayout.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(400).start()

        phoneBtn.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(400).start()



        val googlesignINoptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("421943511870-lq5ldaivj04hqsg5nsrcrrv3ve1amk94.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this,googlesignINoptions)
        firebaseAuth=FirebaseAuth.getInstance()


        floatActionBtn.setOnClickListener {
            Log.d(TAG,"onCreate:Begin Google Signin")
            val intent=googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)

        }
        phoneBtn.setOnClickListener {
           startActivity(Intent(this@LoginActivity,PhoneAuthenActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== RC_SIGN_IN)
        {
            val accountTask=GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account=accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e:Exception){

            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential=GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val firebaseUser = db.currentUser
                updateUI(firebaseUser)
                return@addOnCompleteListener
            }
            else{
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
                updateUI(null)
            }

         }.addOnFailureListener {
                Toast.makeText(this@LoginActivity,"Logged failed ",Toast.LENGTH_SHORT).show()

        }
    }


    private fun updateUI(firebaseUser: FirebaseUser?) {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (googleSignInAccount != null){
            val personName = googleSignInAccount.displayName.toString().trim()
            val personEmail = googleSignInAccount.email.toString().trim()
            val personUsername = googleSignInAccount.givenName.toString().trim()
            val storeDetails = HashMap<String, Any>()
            storeDetails["FullName"] = personName
            storeDetails["UserName"] = personUsername
            storeDetails["Email_ID"] = personEmail
            val dbRef = fStore.collection("MagicMeal").document(fAuth.uid.toString())
                .collection("Users").document("PersonalDetails")
            dbRef.set(storeDetails, SetOptions.merge())
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
                    Toast.makeText(applicationContext, "Logged In Successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
                }
        }
    }
    override fun onStart() {
        super.onStart()

        if(db.currentUser!=null)
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}