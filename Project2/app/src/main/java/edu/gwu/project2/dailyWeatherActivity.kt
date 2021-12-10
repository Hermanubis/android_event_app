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
import org.w3c.dom.Text

class dailyWeatherActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var  alert: Button
    private lateinit var  adapter: dailyWeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dailyweather)
        recyclerView = findViewById(R.id.weatherRecycler)
        alert = findViewById(R.id.alert)

        setTitle(getString(R.string.localweather))
        val weatherAPI = getString(R.string.weatherAPI)
        val eventManager: eventManager = eventManager()
        doAsync {
            val weathers: List<weather> = try {
                eventManager.retrieveFutureWeather(weatherAPI)
            } catch(exception: Exception) {
                listOf<weather>()
            }

            runOnUiThread {
                if(weathers.isNotEmpty()){
                    adapter = dailyWeatherAdapter(weathers)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@dailyWeatherActivity)
                }
                else{
                    Toast.makeText(
                        this@dailyWeatherActivity,
                        getString(R.string.error_source),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}