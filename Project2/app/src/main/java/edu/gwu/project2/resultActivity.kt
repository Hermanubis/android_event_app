package edu.gwu.project2

import android.content.Intent
import android.os.Bundle
import android.net.Uri
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

class resultActivity : AppCompatActivity() {

    private lateinit var adapter: eventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherCard: CardView
    private lateinit var weatherImage: ImageView
    private lateinit var temperature: TextView
    private lateinit var city_name: TextView
    private lateinit var condition: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        recyclerView = findViewById(R.id.result_recycler)
        weatherCard = findViewById(R.id.weatherCard)
        weatherImage = findViewById(R.id.weatherImage)
        temperature = findViewById(R.id.temperature)
        city_name = findViewById(R.id.city_name)
        condition = findViewById(R.id.condition)

        // Retrieve data from the Intent that launched this screen
        val intent: Intent = getIntent()
        val searchTerm: String = intent.getStringExtra("TERM")!!

        if(!searchTerm.isNullOrEmpty()) {

            val eventManager: eventManager = eventManager()
            val eventAPI = getString(R.string.eventAPI)
            val weatherAPI = getString((R.string.weatherAPI))
            val city = "washington"

            val title = "Results for ${searchTerm}"
            setTitle(title)
            doAsync {

//                    var articles: List<news> = eventManager.retrieveAllNews(eventAPI, searchTerm)
                val weather: weather = eventManager.retrieveWeather(weatherAPI, city)
                val check =  weather("", "", "", "", "")
                runOnUiThread {
                    if(weather != check){
                       if(weather.img.isNotBlank()) {
                           Picasso
                               .get()
                               .load(weather.img)
                               .into(weatherImage)
                       }
                       city_name.setText(weather.city)
                       condition.setText(weather.condition)
                       temperature.setText("Temperature: ${weather.low}°F - ${weather.high}°F")
                    }
                    else{
                        Toast.makeText(
                            this@resultActivity,
                            getString(R.string.error_result),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            doAsync {

//                    var articles: List<news> = eventManager.retrieveAllNews(eventAPI, searchTerm)
                val events: List<event> = try {
                    eventManager.retrieveEvents(eventAPI, searchTerm)
                } catch(exception: Exception) {
//                        Log.e("resultActivity", "Retrieving news failed!", exception)
                    listOf<event>()
                }

                runOnUiThread {
                    if(events.isNotEmpty()){
                        adapter = eventAdapter(events)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@resultActivity)
                    }
                    else{
                        Toast.makeText(
                            this@resultActivity,
                            getString(R.string.error_result),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        }
        else{
            setTitle("No Search Term Found")
        }
    }
}
