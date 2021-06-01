package com.hera.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

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
            setProfileImg()
        }

        // On profile img click.
        profileImg.setOnClickListener {
            setProfileImg()
        }

        // On reg btn click.
        regBtn.setOnClickListener {
            val username = usernameEt.text.toString()
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            val confirmPassword = confirmPasswordEt.text.toString()
            val isValid = validate(username, email, password, confirmPassword)
            if (isValid) {
                regBtn.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
                createUserWithEmailAndPassword(email, password)
            }
        }

        // On login tv click.
        loginTv.setOnClickListener {
            startLoginActivity()
        }
    }

    /**
     * On Activity Result.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_IMAGE) {
            if (data != null) {
                val image = data.data.toString()
                Glide.with(this)
                    .load(image)
                    .error(R.drawable.ic_launcher_background)
                    .into(profileImg)
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
        var isValid = true
        // username validation.
        when {
            username.isEmpty() -> {
                isValid = false
                usernameEt.error = "empty"
            }
            username.length > 30 -> {
                isValid = false
                usernameEt.error = "username is too long"
            }
            else -> {
                usernameEt.error = null
            }
        }
        // email validation.
        when {
            email.isEmpty() -> {
                isValid = false
                emailEt.error = "empty"
            }
//            Pattern.matches(".+@.+", email) -> {
//                isValid = false
//                emailEt.error = "incorrect email."
//                Log.d(TAG, "incorrect email.")
//            }
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
            password.length < 6 -> {
                isValid = false
                passwordEt.error = "password is too weak"
            }
            else -> {
                passwordEt.error = null
            }
        }
        // confirm password validation.
        when {
            confirmPassword != password -> {
                isValid = false
                confirmPasswordEt.error = "passwords do not match"
            }
            else -> {
                confirmPasswordEt.error = null
            }
        }
        return isValid
    }

    /**
     * Set Profile Img.
     */
    private fun setProfileImg() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose App"), GET_IMAGE)
    }

    /**
     * Create User With Email And Password.
     */
    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            sendEmailVerification(user)
                        } else {
                        }
                    } else {
                    }
                }
        regBtn.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }

    /**
     * Send Email Verification.
     */
    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // should send alert.
                        startLoginActivity()
                    } else {
                    }
                }
    }

    /**
     * Start Login Activity.
     */
    private fun startLoginActivity() {
        finish()
    }

    /**
     * Email Verification Alert.
     */
    private fun emailVerificationAlert(user: FirebaseUser) {
        AlertDialog.Builder(applicationContext)
                .setMessage("We have sent email verification letter to ${user.email}." +
                        "Please verify your email.")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                    startLoginActivity()
                }
                .create()
                .show()
    }

    companion object {
        const val GET_IMAGE = 4365
    }
}