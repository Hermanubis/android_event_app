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
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.*

class MainActivity : AppCompatActivity() {
    private lateinit var searchbar: SearchView
    private lateinit var search: Button
    private lateinit var venuesearch: Button
    private lateinit var mapButton: Button
    private lateinit var logout:Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        val preferences: SharedPreferences =
            getSharedPreferences("eventapp", Context.MODE_PRIVATE)

        searchbar = findViewById(R.id.searchbar)
        search = findViewById(R.id.search)
        venuesearch = findViewById(R.id.venue)
        mapButton = findViewById(R.id.mapButton)
        logout = findViewById(R.id.logout)

        setTitle(getString(R.string.Home_title))

        search.isEnabled = false

        val savedSearch = preferences.getString("SEARCHTERM", "")
        val enableButton: Boolean = savedSearch!!.isNotBlank()
        search.isEnabled = enableButton
        venuesearch.isEnabled = enableButton
        searchbar.setQuery(savedSearch, false)

        searchbar.setOnSearchClickListener {
            val editor = preferences.edit()
            editor.putString("SEARCHTERM", searchbar.query.toString()).apply()
            val intent: Intent = Intent(this, resultActivity::class.java)
            intent.putExtra("TERM", searchbar.query.toString())
            startActivity(intent)
        }


        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(term: String?): Boolean {
                val editor = preferences.edit()
                editor.putString("SEARCHTERM", term).apply()
                val intent: Intent = Intent(applicationContext, resultActivity::class.java)
                intent.putExtra("TERM", searchbar.query.toString())
                startActivity(intent)
                return false
            }

            override fun onQueryTextChange(term1: String?): Boolean {
                val editor = preferences.edit()
                editor.putString("SEARCHTERM", term1).apply()
                val enableButton: Boolean = searchbar.query.isNotBlank()
                search.isEnabled = enableButton
                venuesearch.isEnabled = enableButton
                return enableButton
            }
        })

        search.setOnClickListener {
            val editor = preferences.edit()
            editor.putString("SEARCHTERM", searchbar.query.toString()).apply()
            val intent: Intent = Intent(this, resultActivity::class.java)
            intent.putExtra("TERM", searchbar.query.toString())
            intent.putExtra("type","keyword")
            startActivity(intent)
        }

        venuesearch.setOnClickListener {
            val editor = preferences.edit()
            editor.putString("SEARCHTERM", searchbar.query.toString()).apply()
            val intent: Intent = Intent(this, resultActivity::class.java)
            intent.putExtra("TERM", searchbar.query.toString())
            intent.putExtra("type","venue")
            startActivity(intent)
        }

        mapButton.setOnClickListener {
            val intent: Intent = Intent(this, dailyWeatherActivity::class.java)
            startActivity(intent)
        }
//        viewEvents.setOnClickListener {
//            val intent: Intent = Intent(this, Activity::class.java)
//            startActivity(intent)
//        }
        logout.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            firebaseAuth.signOut()
        }
    }
}