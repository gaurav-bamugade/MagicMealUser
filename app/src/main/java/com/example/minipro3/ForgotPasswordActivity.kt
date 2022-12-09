package com.example.minipro3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private val adminID = "F0y2F2SeaoWHjY7sIHFr4JRf1HF2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgot_pwd_reset_button.setOnClickListener {
            val email = forgot_email_id.text.toString().trim()

            if (email.isEmpty()){
                forgot_email_id.error = "Email ID Required"
                forgot_email_id.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                forgot_email_id.error = "Valid Email ID Required"
                forgot_email_id.requestFocus()
                return@setOnClickListener
            }

            forgot_password_progress_bar.visibility = View.VISIBLE
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    forgot_password_progress_bar.visibility = View.INVISIBLE
                    if (it.isSuccessful){
                        Toast.makeText(this, "Please Check your Mail ", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, it.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
