@file:Suppress("DEPRECATION")

package com.example.minipro3

import  android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.app.ActivityCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView



import java.io.ByteArrayOutputStream
import java.lang.Exception
import kotlin.collections.HashMap

@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class ProfileActivity : AppCompatActivity() {

    private var selectedPhotoUri : Uri? = null
    lateinit var fAuth : FirebaseAuth
    lateinit var fStore: FirebaseFirestore
    private var storage : FirebaseStorage? = null
    private var PICK_IMAGE_REQUEST = 1234
    private var coms : Bitmap? = null
    private var storageReference : StorageReference? = null
    lateinit var byteArrayOutputStream: ByteArrayOutputStream
    lateinit var imgPath : UploadTask
    lateinit var imgData : ByteArray
    private val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val adminID = "KKpL3gQiy8Ps7x5CdobOn5uxTGl1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        val fName = findViewById<TextView>(R.id.name_of_user_tv)





        fStore.collection("MagicMeal")
            .document(userid)
            .collection("Users").document("PersonalDetails")
            .get().addOnSuccessListener { ds->
                val username = ds.getString("FullName")
                val profile = ds.getString("url").toString()
                val email_id = ds.getString("Email_ID").toString()
                fName.text= username
                email_of_user_tv.text = email_id
                Glide.with(this).load(profile).placeholder(R.drawable.unisex_avatar).dontAnimate()
                    .fitCenter().into(user_profile_image)
            }



        tv_myprofile.setOnClickListener {
            startActivity(Intent(this, ProfMyProfileActivity::class.java))
        }



        tv_myaddress.setOnClickListener {
            startActivity(Intent(this, MyAddressActivity::class.java))
        }

        user_profile_image.setOnClickListener {
            //            showFileChooser()
            val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (permission != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
                }
                else{
                    showFileChooser()
                }
            }else{
                showFileChooser()
            }
        }
        upload_profile_image.setOnClickListener {

          if (selectedPhotoUri != null){
                imgPath = storageReference!!.child(FirebaseAuth.getInstance().uid.toString()).putFile(selectedPhotoUri!!)
                imgPath.addOnCompleteListener {
                    if (it.isSuccessful){
                        storeData(it as UploadTask)
                    }else{
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
          }

        }
    }

    private fun storeData(task: UploadTask){
        try {
            task.result.storage.downloadUrl.addOnSuccessListener { uri->
                val userData = HashMap<String, Any>()
                userData["url"] = uri.toString()
                val ref = fStore.collection("MagicMeal").document(userid)
                    .collection("Users").document("PersonalDetails")
                ref.update(userData)
                Toast.makeText(this, "Uploaded Successfully to Database", Toast.LENGTH_LONG).show()
            }
        }catch (e : Exception){
            Log.d("Main",e.toString())
        }
    }

    private fun showFileChooser(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1,1)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result : CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK){
                selectedPhotoUri = result.uri
                try {
                    val b: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
//                    user_profile_image!!.setImageBitmap(b)
                    Glide.with(this).load(b).into(user_profile_image)
                }catch (e : IOException){
                    e.printStackTrace()
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val ex : Exception = result.error
                Log.d("Main", ex.toString())
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
