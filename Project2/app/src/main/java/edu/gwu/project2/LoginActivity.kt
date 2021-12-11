package edu.gwu.project2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*


class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var signUp: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var remember : Switch
    private lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tells Android which layout file should be used for this screen.
        setContentView(R.layout.activity_login)


        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        preferences = getSharedPreferences("credential", Context.MODE_PRIVATE)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        signUp = findViewById(R.id.signUp)
        progressBar = findViewById(R.id.progressBar)
        remember = findViewById(R.id.remember)
        login.isEnabled = false



        val switchState = preferences.getString("switch", "")
        remember.isChecked = switchState == "1"
            if(remember.isChecked){
                val editor = preferences.edit()
                val savedUsername = preferences.getString("username","")
                val savedPassword = preferences.getString("password","")
                username.setText(savedUsername)
                password.setText(savedPassword)
                if(username != null && password!= null){
                    login.isEnabled =true
                }
                editor.putString("switch", "1").apply()
            }
            else{
                login.isEnabled = false
            }
        remember.setOnClickListener{
            if(remember.isChecked){
                val editor = preferences.edit()
                editor.putString("switch", "1").apply()
            }else{
                val editor = preferences.edit()
                editor.putString("switch", "0").apply()
            }
        }


        login.setOnClickListener {

            val inputtedUsername = username.text.toString().trim()
            val inputtedPassword = password.text.toString().trim()

            if (remember.isChecked){
                val editor = preferences.edit()
                editor.putString("username", inputtedUsername).apply()
                editor.putString("password", inputtedPassword).apply()
            }
            else{
                val editor = preferences.edit()
                editor.putString("username", "").apply()
                editor.putString("password", "").apply()
            }

            firebaseAuth.signInWithEmailAndPassword(inputtedUsername, inputtedPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this, "Logged in as user: ${user!!.email}", Toast.LENGTH_SHORT).show()
                        // Go to the next Activity ...
                        if (user.isEmailVerified) {
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
            val intent: Intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
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
                if (remember.isActivated){
                    val editor = preferences.edit()
                    editor.putString("username", inputtedUsername).apply()
                    editor.putString("password", inputtedPassword).apply()
                }
                else{
                    val editor = preferences.edit()
                    editor.putString("username", "").apply()
                    editor.putString("password", "").apply()
                }
                val enableButton: Boolean = inputtedUsername.isNotBlank() && inputtedPassword.isNotBlank()

                // Kotlin shorthand for login.setEnabled(enableButton)
                login.isEnabled = enableButton
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
//    override fun onStart() {
//        super.onStart()
//
//        // If the user is already logged in, send them directly to the Maps Activity
//        if (firebaseAuth.currentUser != null) {
//            val user = firebaseAuth.currentUser
//            Toast.makeText(this, "Logged in as user: ${user!!.email}",
//                Toast.LENGTH_SHORT).show()
//
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//    }

}
