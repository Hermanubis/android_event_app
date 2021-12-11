package edu.gwu.project2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
//import com.google.firebase.crashlytics.ktx.crashlytics

class SignupActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var password2: EditText
    private lateinit var Back: Button
    private lateinit var signUp: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.Signup)
        // Tells Android which layout file should be used for this screen.
        setContentView(R.layout.activity_signup)

        firebaseAuth = FirebaseAuth.getInstance()
//        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
//
        Log.d("MainActivity", "onCreate called!")
        val preferences: SharedPreferences =
            getSharedPreferences("username", Context.MODE_PRIVATE)
//
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        password2 = findViewById(R.id.password2)
        signUp = findViewById(R.id.signUp)
        Back = findViewById(R.id.Back)
        progressBar = findViewById(R.id.progressBar)

        signUp.isEnabled = false
        Back.isEnabled = true

        signUp.setOnClickListener {
            val inputtedUsername = username.text.toString().trim()
            val inputtedPassword = password.text.toString().trim()
            val inputtedPassword2 = password2.text.toString().trim()
            if(inputtedPassword == inputtedPassword2){
                firebaseAuth.createUserWithEmailAndPassword(inputtedUsername,inputtedPassword).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        val user = firebaseAuth.currentUser
                        user?.sendEmailVerification()
                        Toast.makeText(this, "Created user: ${user!!.email}",Toast.LENGTH_LONG).show()
                        val editor = preferences.edit()
                        editor.putString("USERNAME", inputtedUsername)
                        editor.apply()
//                        if (user.isEmailVerified) {
//                            progressBar.visibility = View.INVISIBLE
//                        }else{
//                            Toast.makeText(this, "Please confirm your email",Toast.LENGTH_LONG).show()
//                            progressBar.visibility = View.VISIBLE
//                        }
                    }else {
                        val bundle = Bundle()
                        val exception: Exception? = task.exception
                        Toast.makeText(this, "Failed: $exception", Toast.LENGTH_LONG).show()
                    }

                }

            }else{
                Toast.makeText(this, "Password does not match", Toast.LENGTH_LONG).show()
            }


        }
        Back.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        password2.addTextChangedListener(textWatcher)

    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Kotlin shorthand for username.getText().toString()
            // .toString() is needed because getText() returns an Editable (basically a char array).
            val inputtedUsername: String = username.text.toString()
            val inputtedPassword: String = password.text.toString()
            val inputtedPassword2: String = password2.text.toString()
            val enableButton: Boolean = inputtedUsername.isNotBlank() && inputtedPassword.isNotBlank() && inputtedPassword2.isNotBlank()

            // Kotlin shorthand for login.setEnabled(enableButton)

            signUp.isEnabled = enableButton
        }

        override fun afterTextChanged(p0: Editable?) {}
    }
    override fun onStart() {
        super.onStart()

        // If the user is already logged in, send them directly to the Maps Activity
        if (firebaseAuth.currentUser != null) {
            val user = firebaseAuth.currentUser
            Toast.makeText(this, "Logged in as user: ${user!!.email}",
                Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
