package com.hera.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import java.util.regex.Pattern

/**
 * Registration Activity.
 */
class RegistrationActivity : AppCompatActivity() {
    // views.
    private lateinit var editImg: ImageView
    private lateinit var profileImg: CircleImageView
    private lateinit var usernameEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var confirmPasswordEt: EditText
    private lateinit var regBtn: Button
    private lateinit var loginTv: TextView
    private lateinit var progressBar: ProgressBar
    // firebase.
    private lateinit var auth: FirebaseAuth

    /**
     * On Create.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registration)

        // Setting views.
        editImg = findViewById(R.id.reg_edit_img)
        profileImg = findViewById(R.id.reg_profile_img)
        usernameEt = findViewById(R.id.reg_username_et)
        emailEt = findViewById(R.id.reg_email_et)
        passwordEt = findViewById(R.id.reg_password_et)
        confirmPasswordEt = findViewById(R.id.reg_confirm_password_et)
        regBtn = findViewById(R.id.reg_btn)
        loginTv = findViewById(R.id.reg_login_tv)
        progressBar = findViewById(R.id.reg_progress_bar)

        // Setting firebase.
        auth = Firebase.auth

        // On edit img click.
        editImg.setOnClickListener {
            Log.d(TAG, "edit img was clicked.")
            setProfileImg()
        }

        // On profile img click.
        profileImg.setOnClickListener {
            Log.d(TAG, "profile img was clicked.")
            setProfileImg()
        }

        // On reg btn click.
        regBtn.setOnClickListener {
            Log.d(TAG, "registration button was clicked.")
            val username = usernameEt.text.toString()
            Log.d(TAG, "Username: $username")
            val email = emailEt.text.toString()
            Log.d(TAG, "Email: $email")
            val password = passwordEt.text.toString()
            Log.d(TAG, "Password: $password")
            val confirmPassword = confirmPasswordEt.text.toString()
            Log.d(TAG, "Confirm Password: $confirmPassword")
            val isValid = validate(username, email, password, confirmPassword)
            Log.d(TAG, "isValid = $isValid")
            if (isValid) {
                regBtn.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
                createUserWithEmailAndPassword(email, password)
            }
        }

        // On login tv click.
        loginTv.setOnClickListener {
            Log.d(TAG, "login tv was clicked.")
            startLoginActivity()
        }
    }

    /**
     * On Activity Result.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "on activity result function has started.")
        if (requestCode == GET_IMAGE) {
            if (data != null) {
                val image = data.data.toString()
                Log.d(TAG, "Image = $image")
                Glide.with(this)
                    .load(image)
                    .error(R.drawable.ic_launcher_background)
                    .into(profileImg)
                Log.d(TAG, "Image has been loaded to profileImg view.")
            }
        }
    }

    /**
     * Validate.
     */
    private fun validate(username: String,
                         email: String,
                         password: String,
                         confirmPassword: String): Boolean {
        Log.d(TAG, "validation started.")
        var isValid = true
        // username validation.
        when {
            username.isEmpty() -> {
                isValid = false
                usernameEt.error = "empty"
                Log.d(TAG, "username is empty.")
            }
            username.length > 30 -> {
                isValid = false
                usernameEt.error = "username is too long"
                Log.d(TAG, "username is too long.")
            }
            else -> {
                usernameEt.error = null
                Log.d(TAG, "username is valid.")
            }
        }
        // email validation.
        when {
            email.isEmpty() -> {
                isValid = false
                emailEt.error = "empty"
                Log.d(TAG, "email is empty.")
            }
//            Pattern.matches(".+@.+", email) -> {
//                isValid = false
//                emailEt.error = "incorrect email."
//                Log.d(TAG, "incorrect email.")
//            }
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
            password.length < 6 -> {
                isValid = false
                passwordEt.error = "password is too weak"
                Log.d(TAG, "password length is < 6")
            }
            else -> {
                passwordEt.error = null
                Log.d(TAG, "password is valid.")
            }
        }
        // confirm password validation.
        when {
            confirmPassword != password -> {
                isValid = false
                confirmPasswordEt.error = "passwords do not match"
                Log.d(TAG, "confirm password is not equal to password.")
            }
            else -> {
                confirmPasswordEt.error = null
                Log.d(TAG, "confirm password is valid.")
            }
        }
        return isValid
    }

    /**
     * Set Profile Img.
     */
    private fun setProfileImg() {
        Log.d(TAG, "set profile img fun was called.")
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose App"), GET_IMAGE)
        Log.d(TAG, "starts activity for result to get an image.")
    }

    /**
     * Create User With Email And Password.
     */
    private fun createUserWithEmailAndPassword(email: String, password: String) {
        Log.d(TAG, "create user with email and password function was called.")
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    Log.d(TAG, "creating user.")
                    if (task.isSuccessful) {
                        Log.d(TAG, "user is created successfully.")
                        val user = auth.currentUser
                        if (user != null) {
                            sendEmailVerification(user)
                            Log.d(TAG, "user is not null.")
                        } else {
                            Log.d(TAG, "user is null.")
                        }
                    } else {
                        Log.d(TAG, "user is not created.")
                    }
                }
        regBtn.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }

    /**
     * Send Email Verification.
     */
    private fun sendEmailVerification(user: FirebaseUser) {
        Log.d(TAG, "send email verification function called.")
        user.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "email verification letter has been sent.")
                        // should send alert.
                        startLoginActivity()
                    } else {
                        Log.d(TAG, "email verification failed.")
                    }
                }
    }

    /**
     * Start Login Activity.
     */
    private fun startLoginActivity() {
        Log.d(TAG, "start login activity function was called.")
        finish()
        Log.d(TAG, "Registration Activity was popped out from the back stack.")
    }

    /**
     * Email Verification Alert.
     */
    private fun emailVerificationAlert(user: FirebaseUser) {
        Log.d(TAG, "email verification alert fun has called.")
        AlertDialog.Builder(applicationContext)
                .setMessage("We have sent email verification letter to ${user.email}." +
                        "Please verify your email.")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, which ->
                    Log.d(TAG, "user has been alerted about email verification.")
                    dialog.dismiss()
                    startLoginActivity()
                }
                .create()
                .show()
    }

    companion object {
        const val TAG = "RegistrationActivity"
        const val GET_IMAGE = 4365
    }
}