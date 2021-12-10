package edu.gwu.project2

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class dailyWeatherAdapter(val weathers: List<weather>) : RecyclerView.Adapter<dailyWeatherAdapter.ViewHolder>() {

    //get number of rows
    override fun getItemCount(): Int {
        return weathers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // A LayoutInflater is an object that knows how to read & parse an XML file
        val layoutInflater = LayoutInflater.from(parent.context)

        //new row creation at runtime, reference to the root layout
        val rootLayout: View = layoutInflater.inflate(R.layout.row_weather, parent, false)

        return ViewHolder(rootLayout)
    }

    // RecyclerView is ready to display a new (or recycled) row on the screen
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //row position/index are given.
        val currWeather = weathers[position]
        var day = currWeather.local
        val format = java.text.SimpleDateFormat("yyyy-MM-dd")
        val mydate = java.util.Date(day.toLong()*1000)
        val formatted = format.format(mydate)
        viewHolder.date.text = formatted
        viewHolder.condition.text = currWeather.condition
        viewHolder.temperature.text = "${currWeather.low}°F - ${currWeather.high}°F"
        if (currWeather.img.isNotBlank()) {
            //Picasso.get().setIndicatorsEnabled(true)
            var link = "https://openweathermap.org/img/wn/${currWeather.img}@2x.png"
            Picasso
                .get()
                .load(link)
                .into(viewHolder.icon)
        }

    }

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val date: TextView = rootLayout.findViewById(R.id.day)
        val icon: ImageView = rootLayout.findViewById(R.id.weatherIcon)
        val condition: TextView = rootLayout.findViewById(R.id.weatherCondition)
        val temperature: TextView = rootLayout.findViewById(R.id.temp)
        val cardView: CardView = rootLayout.findViewById(R.id.dayweatherCard)
    }
}