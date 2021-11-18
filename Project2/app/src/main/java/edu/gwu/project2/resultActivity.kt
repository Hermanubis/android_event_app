package edu.gwu.project2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class resultActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: eventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        recyclerView = findViewById(R.id.result_recycler)

        // Retrieve data from the Intent that launched this screen
        val intent: Intent = getIntent()
        val searchTerm: String = intent.getStringExtra("searchTerm")!!

        if(!searchTerm.isNullOrEmpty()) {

            val eventManager: eventManager = eventManager()
            val newsAPI = getString(R.string.eventAPI)


            val title = "Results for ${searchTerm}"
            setTitle(title)
            doAsync {

//                    var articles: List<news> = eventManager.retrieveAllNews(newsAPI, searchTerm)
                val events: List<event> = try {
                    eventManager.retrieveEvents(newsAPI, searchTerm)
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
