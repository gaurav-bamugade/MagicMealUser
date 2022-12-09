package com.example.minipro3

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_loginfrag.*
import kotlinx.android.synthetic.main.fragment_loginfrag.emaillog
import kotlinx.android.synthetic.main.fragment_loginfrag.passlog
import kotlinx.android.synthetic.main.fragment_loginfrag.signinbtn
import kotlinx.android.synthetic.main.fragment_signupfrag.*

class LoginTab   : Fragment() {
    lateinit var db: FirebaseAuth
    lateinit var sh: SharedPreferences
    private val adminID = "KKpL3gQiy8Ps7x5CdobOn5uxTGl1"
    lateinit var signInButton: SignInButton
    private lateinit var signInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1
    private val fStore = FirebaseFirestore.getInstance()
    private val fAuth = FirebaseAuth.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_loginfrag, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emaillog.setTranslationX(800F)
        passlog.setTranslationX(800F)
        signinbtn.setTranslationX(800F)

        emaillog.setAlpha(0F)
        passlog.setAlpha(0F)
        signinbtn.setAlpha(0F)
        signinbtn.setOnClickListener {
            signIn()
        }
        emaillog.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(300).start()
        passlog.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()
        signinbtn.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(700).start()

       /* val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(context, gso)
        signInButton = findViewById(R.id.google_sign_in_button)
        signInButton.setOnClickListener {
            googleSignInFun()
        }*/
        forgot_passwords.setOnClickListener {
            startActivity(Intent(context,ForgotPasswordActivity::class.java))
        }

    }

    private fun googleSignInFun(){
        val signInIntent = signInClient.signInIntent
        signInClient.signOut()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(comptask: Task<GoogleSignInAccount>?) {
        try{
            val acc = comptask!!.getResult(ApiException::class.java)
            Toast.makeText(context, "Signed In Successfully", Toast.LENGTH_LONG).show()
            firebaseGoogleAuth(acc)
        }catch (e: ApiException){
            e.printStackTrace()
            Log.d("ex", e.message.toString().trim())
            Toast.makeText(context, "Signed In Failed", Toast.LENGTH_LONG).show()
            firebaseGoogleAuth(null)
        }
    }

    private fun firebaseGoogleAuth(acct: GoogleSignInAccount?) {
        db = FirebaseAuth.getInstance()
        val authCredential = GoogleAuthProvider.getCredential(acct!!.idToken, null)
        db.signInWithCredential(authCredential).addOnCompleteListener {
            if(it.isSuccessful){
                val firebaseUser = db.currentUser
                updateUI(firebaseUser)
                return@addOnCompleteListener
            }
            else{
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                updateUI(null)
            }
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        db = FirebaseAuth.getInstance()
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context)
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
                    var ias:Intent?=null
                    Toast.makeText(context, "Logged In Successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun signIn() {
        db = FirebaseAuth.getInstance()
        val em = emaillog.text.toString()
        val ps = passlog.text.toString()

        if (em.isEmpty() || ps.isEmpty()) {
            Toast.makeText(context, "Please Enter the Required Credentials ", Toast.LENGTH_LONG).show()
            return
        }
        db.signInWithEmailAndPassword(em, ps)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Main", "Successfully Logged in User: ${it.result?.user?.uid}")
                    Toast.makeText(context, "Welcome to MagicMeal!!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                    return@addOnCompleteListener
                } else {
                    Toast.makeText(context, "Failed to Login", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Log.d("Main", " Failed to Login : ${it.message}")
                Toast.makeText(context, "Failed to Login : ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

/*    override fun onStart() {
        super.onStart()

        //sh = getSharedPreferences("com.example.menulayout", 0)

        if (FirebaseAuth.getInstance().currentUser != null) {
            if (sh.getBoolean("isadmin", false)) {
//                startActivity(Intent(this, AdminMainActivity::class.java))
//                finish()
            } else {
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
        }
    }*/
}