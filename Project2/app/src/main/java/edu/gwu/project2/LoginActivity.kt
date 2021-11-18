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
        login.setOnClickListener {
            val inputtedUsername = username.text.toString().trim()
            val inputtedPassword = password.text.toString().trim()

            firebaseAuth.signInWithEmailAndPassword(inputtedUsername, inputtedPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this, "Logged in as user: ${user!!.email}", Toast.LENGTH_SHORT).show()
                        // Go to the next Activity ...
                        if (user.isEmailVerified) {
                            // Do some more privileged action
                            val intent: Intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val exception = task.exception
                        Toast.makeText(this, "Failed: $exception", Toast.LENGTH_SHORT).show()
                    }
                }


        }
        signUp.setOnClickListener {
//            firebaseAnalytics.logEvent("login_clicked", null)
//
//            // Save the username to SharedPreferences
            val inputtedUsername = username.text.toString().trim()
            val inputtedPassword = password.text.toString().trim()

            firebaseAuth.createUserWithEmailAndPassword(inputtedUsername,inputtedPassword).addOnCompleteListener { task->

                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    user?.sendEmailVerification()
                    Toast.makeText(this, "Created user: ${user!!.email}",Toast.LENGTH_LONG).show()
                    val editor = preferences.edit()
                    editor.putString("USERNAME", inputtedUsername)
                    editor.apply()

                }else {

                    val bundle = Bundle()
                    val exception: Exception? = task.exception
                    Toast.makeText(this, "Failed: $exception", Toast.LENGTH_LONG).show()


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
