package edu.gwu.project2

import android.content.Intent
import android.os.Bundle
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class alertActivity : AppCompatActivity() {

    private lateinit var type: TextView
    private lateinit var description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        type = findViewById(R.id.type)
        description = findViewById(R.id.alert_content)

        setTitle(getString(R.string.alertTitle))
        val weatherAPI = getString(R.string.weatherAPI)
        val eventManager: eventManager = eventManager()

        doAsync {
            val alert: alert = eventManager.retrieveAlert(weatherAPI)

            runOnUiThread {
                if (alert.description.isNotEmpty()) {
                    type.text = alert.type
                    description.text = alert.description
                } else {
                    type.text = getString(R.string.alertError)
                    Toast.makeText(
                        this@alertActivity,
                        getString(R.string.error_result),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}