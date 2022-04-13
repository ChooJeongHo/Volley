package com.example.volley

import android.content.Context
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var btn : Button
    lateinit var rv : RecyclerView
    lateinit var et : EditText
    lateinit var adapter : RvAdapter

    var arr : ArrayList<Movie> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.btn)
        rv = findViewById(R.id.rv)
        et = findViewById(R.id.et)

        adapter = RvAdapter(this, arr)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        btn.setOnClickListener {
            request("http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=2965ab5d32ae5db7e464c368159aaf62&targetDt=20211213")
//            request1("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=2965ab5d32ae5db7e464c368159aaf62&movieCd=20196264")
        }
    }

    fun request( url : String){
        val requestQueue = Volley.newRequestQueue(this)

        val request: StringRequest = object : StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                response(response)
                //무비코드로 감독 정보요청(요기)
            },
            Response.ErrorListener { error ->
                Toast.makeText(MainActivity@this, error.toString(), Toast.LENGTH_LONG).show()
            }
        ) {

        }

        requestQueue.add(request)

    }

//    fun request1( url : String){
//        val requestQueue = Volley.newRequestQueue(this)
//
//        val request: StringRequest = object : StringRequest(
//            Request.Method.GET, url,
//            Response.Listener { response ->
//                response(response)
//            },
//            Response.ErrorListener { error ->
//                Toast.makeText(MainActivity@this, error.toString(), Toast.LENGTH_LONG).show()
//            }
//        ) {
//
//        }
//
//        requestQueue.add(request)
//
//    }

    fun response( response :String){

        var jsonObj : JSONObject = JSONObject(response)
        var jsonObj2 = jsonObj.getJSONObject("boxOfficeResult")
        var jsonArr : JSONArray = jsonObj2.getJSONArray("dailyBoxOfficeList");

        for (i in 0 until jsonArr.length() step 1){
            var jsonObj3 : JSONObject = jsonArr.getJSONObject(i)
            var title = jsonObj3.getString("movieNm")
            var rank = jsonObj3.getInt("rank")
            var audience = jsonObj3.getInt("audiCnt")
            var jsonArr1 : JSONArray = jsonObj3.getJSONArray("directors")
            var director = jsonArr1[0]

        }
    }

    class RvAdapter(val context: Context, val arr: ArrayList<Movie>) :
        RecyclerView.Adapter<RvAdapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun getItemCount(): Int {
            return arr.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.tv1.setText(arr.get(position).MovieNm)
            holder.tv2.setText(arr.get(position).MovieCd)

            holder.tv1.setOnClickListener {
                arr.get(position).MovieNm = ""
                notifyDataSetChanged()
            }

            holder.itemView.setOnClickListener{
                arr.get(position).MovieNm = ""
                notifyDataSetChanged()
            }
        }

        inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
            var tv1: TextView = itemView!!.findViewById(R.id.tv1)
            val tv2: TextView = itemView!!.findViewById(R.id.tv2)
        }
    }

}