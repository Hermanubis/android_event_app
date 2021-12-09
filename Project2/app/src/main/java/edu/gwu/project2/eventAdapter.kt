package edu.gwu.project2

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.net.URI

class eventAdapter(val events: List<event>) : RecyclerView.Adapter<eventAdapter.ViewHolder>() {
    //get number of rows
    override fun getItemCount(): Int {
        return events.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // A LayoutInflater is an object that knows how to read & parse an XML file
        val layoutInflater = LayoutInflater.from(parent.context)

        //new row creation at runtime, reference to the root layout
        val rootLayout: View = layoutInflater.inflate(R.layout.row_event, parent, false)

        return ViewHolder(rootLayout)
    }

    // RecyclerView is ready to display a new (or recycled) row on the screen
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //row position/index are given.
        val currEvent = events[position]
        viewHolder.name.text = currEvent.name
        viewHolder.time.text = currEvent.time
        viewHolder.venue.text = currEvent.venue
        viewHolder.location.text = currEvent.info
        if (currEvent.img.isNotBlank()) {
            Picasso.get().setIndicatorsEnabled(true)

            Picasso
                .get()
                .load(currEvent.img)
                .into(viewHolder.thumbnail)
        }
        //onclick website
        viewHolder.cardView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currEvent.link))
            it.context.startActivity(intent)
        }
    }

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val name: TextView = rootLayout.findViewById(R.id.event_name)
        val time: TextView = rootLayout.findViewById(R.id.event_time)
        val thumbnail: ImageView = rootLayout.findViewById(R.id.eventImage)
        val venue: TextView = rootLayout.findViewById(R.id.event_venue)
        val cardView: CardView = rootLayout.findViewById(R.id.eventCard)
        val location:TextView = rootLayout.findViewById(R.id.event_loc)
    }
}