package com.hera.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Login Activity.
 */
class  LoginActivity : AppCompatActivity() {
    // views.
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private lateinit var passwordRecTv: TextView
    private lateinit var regTv: TextView
    // firebase.
    private lateinit var auth: FirebaseAuth

    /**
     * On Create.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // Setting views.
        emailEt = findViewById(R.id.login_email_et)
        passwordEt = findViewById(R.id.login_password_et)
        loginBtn = findViewById(R.id.login_btn)
        passwordRecTv = findViewById(R.id.login_password_rec_tv)
        regTv = findViewById(R.id.login_reg_tv)

        // Setting firebase.
        auth = Firebase.auth


        // On login btn click.
        loginBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            if (validate(email, password))  {
                signInWithEmailAndPassword(email, password)
            }
        }

        // On password rec tv click.
        passwordRecTv.setOnClickListener {
            val intent = Intent(this, PasswordRecoveryActivity::class.java)
            startActivity(intent)
        }

        // On reg tv click.
        regTv.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Validate.
     */
    private fun validate(email: String, password: String): Boolean {
        var isValid = true
        // email validation.
        when {
            email.isEmpty() -> {
                isValid = false
                emailEt.error = "empty"
            }
            else -> {
                emailEt.error = null
            }
        }
        // password validation.
        when {
            password.isEmpty() -> {
                isValid = false
                passwordEt.error = "empty"
            }
            else -> {
                passwordEt.error = null
            }
        }
        return isValid
    }

    /**
     * Sign In With Email And Password.
     */
    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        if (user.isEmailVerified) {
                        } else {
                        }
                    } else {
                    }
                } else {
                }
            }
    }
}