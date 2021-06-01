package com.hera.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            Log.d(TAG, "recovery button was clicked.")
            val email = emailEt.text.toString()
            Log.d(TAG, "Email: $email")
            if (validate(email)) {
                sendPasswordResetEmail(email)
            }
        }

        // On login tv click.
        loginTv.setOnClickListener {
            Log.d(TAG, "login tv was clicked.")
            finish()
            Log.d(TAG, "PasswordRecoveryActivity is popped out from the back stack, user is in LoginActivity.")
        }
    }

    /**
     * Validate.
     */
    private fun validate(email: String): Boolean {
        var isValid = true
        Log.d(TAG, "validation started.")
        when {
            email.isEmpty() -> {
                isValid = false
                emailEt.error = "empty"
                Log.d(TAG, "email is empty.")
            }
            else -> {
                emailEt.error = null
                Log.d(TAG, "email is valid.")
            }
        }
        Log.d(TAG, "isValid = $isValid")
        return isValid
    }

    /**
     * Send Password Reset Email.
     */
    private fun sendPasswordResetEmail(email: String) {
        Log.d(TAG, "send password reset fun started.")
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "password recovery letter was sent.")
                    finish()
                    Log.d(TAG, "PasswordRecoveryActivity is popped out from the back stack, user is in LoginActivity.")
                } else {
                    Log.d(TAG, "password is not sent.")
                }
            }
    }

    companion object {
        const val TAG = "PasswordRecovery"
    }
}