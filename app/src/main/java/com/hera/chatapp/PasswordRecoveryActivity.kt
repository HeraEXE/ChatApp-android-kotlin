package com.hera.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Password Recovery Activity.
 */
class PasswordRecoveryActivity : AppCompatActivity() {
    // views.
    private lateinit var emailEt: EditText
    private lateinit var recBtn: Button
    private lateinit var loginTv: TextView
    // firebase.
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_recovery)

        // Setting views.
        emailEt = findViewById(R.id.pswrd_rec_email_et)
        recBtn = findViewById(R.id.pswrd_rec_btn)
        loginTv = findViewById(R.id.pswrd_rec_login_tv)

        // Setting firebase.
        auth = Firebase.auth

        // On rec btn click.
        recBtn.setOnClickListener {
            val email = emailEt.text.toString()
            if (validate(email)) {
                sendPasswordResetEmail(email)
            }
        }

        // On login tv click.
        loginTv.setOnClickListener {
            finish()
        }
    }

    /**
     * Validate.
     */
    private fun validate(email: String): Boolean {
        var isValid = true
        when {
            email.isEmpty() -> {
                isValid = false
                emailEt.error = "empty"
            }
            else -> {
                emailEt.error = null
            }
        }
        return isValid
    }

    /**
     * Send Password Reset Email.
     */
    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    finish()
                } else {
                }
            }
    }
}