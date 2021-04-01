package com.example.weatherapp


import android.content.Intent
import android.os.AsyncTask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*


import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    var city : String = "Dhaka"
    var country: String = ""
    var saveLocation : Boolean = false
    val API : String = "2afec2db7666aacfcc87650321550e5b"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchAddress:LinearLayout = findViewById(R.id.searchAddress)
        searchAddress.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        notification.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        weatherTask().execute()
    }


    inner class weatherTask() : AsyncTask<String , Void, String>(){


        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loder).visibility = View.VISIBLE
            findViewById<TextView>(R.id.errorTxt).visibility = View.GONE
            findViewById<ImageView>(R.id.mainImageView).visibility = View.GONE
            findViewById<LinearLayout>(R.id.Container1).visibility = View.GONE


        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e : Exception){
                response = null
            }
            return  response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updateAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updateAt *1000)
                )
                val temp = main.getString("temp") + "°C"
                val humdity = main.getString("humidity")
                val windSpreed = wind.getString("speed")
                val status = weather.getString("main")
                val addressAP = jsonObj.getString("name")+ ", " + sys.getString("country")

                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.wind).text = windSpreed + " Km/h"
                findViewById<TextView>(R.id.humdity).text = humdity + " %"
                findViewById<TextView>(R.id.status).text = status
                findViewById<TextView>(R.id.today).text = updatedAtText
                findViewById<TextView>(R.id.address).text = addressAP


                if(status.equals("Clouds") || status.equals("Haze") || status.equals("Clear")){
                    findViewById<ImageView>(R.id.mainImageView).setImageResource(R.drawable.cloud)
                }

                findViewById<ProgressBar>(R.id.loder).visibility = View.GONE
                findViewById<TextView>(R.id.errorTxt).visibility = View.GONE
                findViewById<ImageView>(R.id.mainImageView).visibility = View.VISIBLE
                findViewById<LinearLayout>(R.id.Container1).visibility = View.VISIBLE

            }
            catch (e : Exception){
                findViewById<ProgressBar>(R.id.loder).visibility = View.GONE
                findViewById<TextView>(R.id.errorTxt).visibility = View.VISIBLE
            }
        }
    }



}