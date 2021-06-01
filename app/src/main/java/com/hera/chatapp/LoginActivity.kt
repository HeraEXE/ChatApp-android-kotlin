package com.hera.chatapp

import android.content.Intent
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
            Log.d(TAG, "login button was clicked.")
            val email = emailEt.text.toString()
            Log.d(TAG, "Email: $email")
            val password = passwordEt.text.toString()
            Log.d(TAG, "Password: $password")
            if (validate(email, password))  {
                Log.d(TAG, "validation is successful.")
                signInWithEmailAndPassword(email, password)
            }
        }

        // On password rec tv click.
        passwordRecTv.setOnClickListener {
            Log.d(TAG, "password recovery tv was clicked.")
            val intent = Intent(this, PasswordRecoveryActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "password recovery activity was started.")
        }

        // On reg tv click.
        regTv.setOnClickListener {
            Log.d(TAG, "registration tv was clicked.")
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "Registration Activity was started.")
        }
    }

    /**
     * Validate.
     */
    private fun validate(email: String, password: String): Boolean {
        Log.d(TAG, "validation started.")
        var isValid = true
        // email validation.
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
        // password validation.
        when {
            password.isEmpty() -> {
                isValid = false
                passwordEt.error = "empty"
                Log.d(TAG, "password is empty.")
            }
            else -> {
                passwordEt.error = null
                Log.d(TAG, "password is valid.")
            }
        }
        Log.d(TAG, "isValid = $isValid")
        return isValid
    }

    /**
     * Sign In With Email And Password.
     */
    private fun signInWithEmailAndPassword(email: String, password: String) {
        Log.d(TAG, "sign in with email and password fun was called.")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "this user exists.")
                    val user = auth.currentUser
                    if (user != null) {
                        Log.d(TAG, "user is not null.")
                        if (user.isEmailVerified) {
                            Log.d(TAG, "user email is verified.")
                        } else {
                            Log.d(TAG, "user email is not verified.")
                        }
                    } else {
                        Log.d(TAG, "user is null.")
                    }
                } else {
                    Log.d(TAG, "there is no such a user.")
                    Log.d(TAG, "there is no such a userd.")
                }
            }
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}