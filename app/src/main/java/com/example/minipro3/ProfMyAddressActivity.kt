package com.example.minipro3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.core.view.isEmpty
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_my_address.*
import kotlinx.android.synthetic.main.activity_prof_my_address.*
import java.util.*
import kotlin.collections.HashMap

class ProfMyAddressActivity : AppCompatActivity() {

    lateinit var fAuth : FirebaseAuth
    lateinit var fStore : FirebaseFirestore
    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val adminID = "KKpL3gQiy8Ps7x5CdobOn5uxTGl1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prof_my_address)

        //val actionBar = supportActionBar
       // actionBar!!.title = "Add a new Address"

      //  actionBar.setDisplayHomeAsUpEnabled(true)
     //   actionBar.setDisplayHomeAsUpEnabled(true)

        savebtn.setOnClickListener {
            save_add_to_db()
        }

    }

    private fun save_add_to_db(){

        fStore = FirebaseFirestore.getInstance()
        fAuth = FirebaseAuth.getInstance()

        val house_no = findViewById<EditText>(R.id.houseno)
        val road_name = findViewById<EditText>(R.id.roadname)
        val city_pro = findViewById<EditText>(R.id.city)
        val pin_code = findViewById<EditText>(R.id.pincode)
        val state_pro = findViewById<EditText>(R.id.state)
        val land_mark = findViewById<EditText>(R.id.landmark)
        val name = findViewById<EditText>(R.id.prof_add_name)
        val mobno = findViewById<EditText>(R.id.mob_no)
        val altmobno = findViewById<EditText>(R.id.alt_mob_no)
        val homeradio = findViewById<RadioButton>(R.id.home_address)
        val workradio = findViewById<RadioButton>(R.id.work_address)

        val hno = house_no.text.toString()
        val rname = road_name.text.toString()
        val cypro = city_pro.text.toString()
        val pcode = pin_code.text.toString()
        val stpro = state_pro.text.toString()
        val lmark = land_mark.text.toString()
        val napro = name.text.toString()
        val mno = mobno.text.toString()
        val altmno = altmobno.text.toString()
        val hradio = homeradio.text.toString()
        val wradio = workradio.text.toString()

        if(TextUtils.isEmpty(hno)){ house_no.error = "Should not be Empty"}
        if(TextUtils.isEmpty(rname)){ road_name.error = "Should not be Empty"}
        if(TextUtils.isEmpty(cypro)){ city_pro.error = "Should not be Empty"}
        if(TextUtils.isEmpty(pcode)){ pin_code.error = "Should not be Empty"}
        if(TextUtils.isEmpty(napro)){ name.error = "Should not be Empty"}
        if(TextUtils.isEmpty(mno)){ mobno.error = "Should not be Empty"}

        if(radio_group.checkedRadioButtonId == -1){
            workradio.error = "Should not be empty"
            homeradio.error = "Should not be empty"
        }else{
            Log.d("Main","Added")
        }

        if(hno.isEmpty() || rname.isEmpty() || cypro.isEmpty() || pcode.isEmpty() || napro.isEmpty()
            || mno.isEmpty()){
            Toast.makeText(this, "Enter Required Credentials", Toast.LENGTH_LONG).show()
        }
        else {
            try {
                val userData = HashMap<String, Any>()
                userData["buildno"] = hno
                userData["roadname"] = rname
                userData["city"] = cypro
                userData["pincode"] = pcode
                userData["state"] = stpro
                userData["landmark"] = lmark
                userData["name"] = napro
                userData["mobileno"] = mno
                userData["altmob"] = altmno
                if (radio_group.checkedRadioButtonId == R.id.home_address) {
                    userData["addresstype"] = hradio
                } else {
                    userData["addresstype"] = wradio
                }
                val uuid = UUID.randomUUID().toString()
                val ref = fStore.collection("MagicMeal").document(userid)
                    .collection("UsersAddress").document(uuid)
                ref.set(userData, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Address Added Successfully", Toast.LENGTH_LONG).show()
//                        startActivity(Intent(this, MyAddressActivity::class.java))
                        setResult(Activity.RESULT_OK,Intent())
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to Add", Toast.LENGTH_LONG).show()
                        Log.d("Main", e.toString())
                    }
            } catch (e: Exception) {
                Log.d("Main", e.toString())
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
