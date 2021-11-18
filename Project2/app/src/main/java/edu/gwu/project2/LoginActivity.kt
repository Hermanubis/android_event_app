package edu.gwu.project2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
//import com.google.firebase.crashlytics.ktx.crashlytics

class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var signUp: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tells Android which layout file should be used for this screen.
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
//        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
//
        Log.d("MainActivity", "onCreate called!")
        val preferences: SharedPreferences =
            getSharedPreferences("android-tweets", Context.MODE_PRIVATE)
//
//
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        signUp = findViewById(R.id.signUp)
        progressBar = findViewById(R.id.progressBar)
//
        login.isEnabled = false
        signUp.isEnabled = false
//
//
//        val savedUsername = preferences.getString("USERNAME", "")
//        username.setText(savedUsername)
//
        login.setOnClickListener {
//            firebaseAnalytics.logEvent("login_clicked", null)
//
//            // Save the username to SharedPreferences
            val inputtedUsername = username.text.toString().trim()
            val inputtedPassword = password.text.toString().trim()

            firebaseAuth.createUserWithEmailAndPassword(inputtedUsername,inputtedPassword).addOnCompleteListener { task->

                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    Toast.makeText(this, "Created user: ${user!!.email}",Toast.LENGTH_LONG).show()
                    val editor = preferences.edit()
                    editor.putString("USERNAME", inputtedUsername)
                    editor.apply()
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else {

                    val bundle = Bundle()
                    val exception: Exception? = task.exception
                    Toast.makeText(this, "Failed: $exception", Toast.LENGTH_LONG).show()

//                    when (exception) {
//                        is FirebaseAuthInvalidUserException -> {
//                            bundle.putString("error_type", "invalid_user")
//                            firebaseAnalytics.logEvent("login_failed", bundle)
//                            Toast.makeText(
//                                this,
//                                "We can\\'t find an account with this email. Try signing up for an account!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                        is FirebaseAuthInvalidCredentialsException -> {
//                            bundle.putString("error_type", "invalid_credentials")
//                            firebaseAnalytics.logEvent("login_failed", bundle)
//                            Toast.makeText(
//                                this,
//                                "Please check your username &amp; password and try again!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                        else -> {
//                            bundle.putString("error_type", "generic")
//                            firebaseAnalytics.logEvent("login_failed", bundle)
//                            Toast.makeText(
//                                this,
//                                "Failed to login:{exception}",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
                }

            }
        }
        signUp.setOnClickListener {
            val inputtedUsername: String = username.text.toString()
            val inputtedPassword: String = password.text.toString()

            firebaseAuth
                .createUserWithEmailAndPassword(inputtedUsername, inputtedPassword)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        firebaseAnalytics.logEvent("signup_success", null)
                        val currentUser: FirebaseUser = firebaseAuth.currentUser!!

                        Toast.makeText(
                            this,
                            "Registered successfully as: ${currentUser.email}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val exception: Exception? = task.exception
                        val bundle = Bundle()

                        when (exception) {
                            is FirebaseAuthWeakPasswordException -> {
                                bundle.putString("error_type", "weak_password")
                                firebaseAnalytics.logEvent("signup_failed", bundle)
                                Toast.makeText(
                                    this,
                                    "Your password does not meet minimum requirements! Must be at least 6 characters long.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            is FirebaseAuthUserCollisionException -> {
                                bundle.putString("error_type", "user_collision")
                                firebaseAnalytics.logEvent("signup_failed", bundle)
                                Toast.makeText(
                                    this,
                                    "An account already exists for this email!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                bundle.putString("error_type", "invalid_credentials")
                                firebaseAnalytics.logEvent("signup_failed", bundle)
                                Toast.makeText(
                                    this,
                                    "You must supply a valid formatted email!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else -> {
                                bundle.putString("error_type", "generic")
                                firebaseAnalytics.logEvent("signup_failed", bundle)
                                Toast.makeText(
                                    this,
                                    "Failed to register: {exception}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    }
                }
        }
        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)

    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Kotlin shorthand for username.getText().toString()
            // .toString() is needed because getText() returns an Editable (basically a char array).
            val inputtedUsername: String = username.text.toString()
            val inputtedPassword: String = password.text.toString()
            val enableButton: Boolean = inputtedUsername.isNotBlank() && inputtedPassword.isNotBlank()

            // Kotlin shorthand for login.setEnabled(enableButton)
            login.isEnabled = enableButton
            signUp.isEnabled = enableButton
        }

        override fun afterTextChanged(p0: Editable?) {}
    }
}
