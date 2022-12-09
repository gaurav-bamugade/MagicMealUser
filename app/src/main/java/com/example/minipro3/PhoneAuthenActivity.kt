package com.example.minipro3

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_phone_authen.*
import java.util.concurrent.TimeUnit

class PhoneAuthenActivity : AppCompatActivity() {

    private var forceResendingToken:PhoneAuthProvider.ForceResendingToken ? =null
    private var mCalBack:PhoneAuthProvider.OnVerificationStateChangedCallbacks?=null
    private var mVerificationId : String?=null
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var inputPhone:LinearLayout
    private val TAG="MAIN_TAG"
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_authen)

        firebaseAuth= FirebaseAuth.getInstance()
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        inputPhone=findViewById(R.id.inputphone)
        inputPhone.visibility= View.VISIBLE


        inputCode.visibility= View.GONE
        numotpmove()
        mCalBack=object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@PhoneAuthenActivity,"${e.message}",Toast.LENGTH_SHORT).show()

            }

            override fun onCodeSent(verificationID: String,token: PhoneAuthProvider.ForceResendingToken) {
                mVerificationId=verificationID
                forceResendingToken=token
                progressDialog.dismiss()

                inputphone.visibility= View.GONE
                inputCode.visibility= View.VISIBLE
                codeSendDesc.text="Please Type the verification Code We sent"

            }
        }

        phoneContinueBtn.setOnClickListener{
            val phone=phoneNum.text.toString().trim()
            if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(this,"Please Enter Phone Number",Toast.LENGTH_SHORT).show()
            }
            else
            {
                startPhoneNumberVerification(phone)
            }
        }
        resendCode.setOnClickListener {
            val phone=phoneNum.text.toString().trim()
            if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(this,"Please Enter Phone Number",Toast.LENGTH_SHORT).show()

            }
            else
            {
                resendrVerificationOTP(phone, forceResendingToken!!)
            }
        }
        codeSubmit.setOnClickListener {

            if(TextUtils.isEmpty(inp1.text.toString()) && TextUtils.isEmpty(inp2.text.toString()) && TextUtils.isEmpty(inp3.text.toString())&& TextUtils.isEmpty(inp4.text.toString())&& TextUtils.isEmpty(inp5.text.toString())&& TextUtils.isEmpty(inp6.text.toString()) )
            {

                Toast.makeText(this,"Please Enter Code Number",Toast.LENGTH_SHORT).show()

            }
            else
            {
                val enterCodeOtp: String = inp1.getText().toString() +
                        inp2.getText().toString() +
                        inp3.getText().toString() +
                        inp4.getText().toString() +
                        inp5.getText().toString() +
                        inp6.getText().toString()
                verifyPhoneNumberWithCode(mVerificationId!!,enterCodeOtp)

            }
        }
    }
    private fun startPhoneNumberVerification(phone:String)
    {
        progressDialog.setMessage("Verifying Phone Number")
        progressDialog.show()
        val options= PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCalBack!!)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    private fun resendrVerificationOTP(phone:String,token:PhoneAuthProvider.ForceResendingToken)
    {
        progressDialog.setMessage("Resending OTP")
        progressDialog.show()
        val options= PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCalBack!!)
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    private fun verifyPhoneNumberWithCode(verificationid:String,code:String)
    {
        progressDialog.setMessage("verifying Code...")
        progressDialog.show()
        val credentials=PhoneAuthProvider.getCredential(verificationid,code)
        signInWithCredentials(credentials)
    }

    private fun signInWithCredentials(credentials: PhoneAuthCredential) {
        progressDialog.setMessage("Logging IN")
        firebaseAuth.signInWithCredential(credentials)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val Phone=firebaseAuth.currentUser?.phoneNumber
                Toast.makeText(this,"Logged In As $Phone",Toast.LENGTH_SHORT).show()

                startActivity(Intent(this,PersonalDetailActivity::class.java))

            }.addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun numotpmove() {

        inp1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inp2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inp2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inp3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inp3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inp4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inp5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inp5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inp6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


}