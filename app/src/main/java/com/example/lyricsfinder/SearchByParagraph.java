package com.example.lyricsfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchByParagraph extends AppCompatActivity {

    private TextInputEditText edtPara;
    private Button searchPara;
    private TextView lyricsPara;

    private String urlBase = "https://api.musixmatch.com/ws/1.1/track.search?";
    int track_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_paragraph);

        edtPara = findViewById(R.id.edtPara);
        searchPara = findViewById(R.id.searchPara);
        lyricsPara = findViewById(R.id.lyricsPara);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#457b9d"));

        actionBar.setBackgroundDrawable(colorDrawable);

        Window window = SearchByParagraph.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(SearchByParagraph.this, R.color.dark_blue));

        searchPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTrack();
            }
        });
    }

    private void searchTrack() {
        String trackUrl = urlBase;
        String key = "&apikey=45a19f1f0dd81a4ff34ced6407b7af8e";
        trackUrl+="q_lyrics="+edtPara.getText().toString()+"";
        trackUrl+="&quorum_factor=1";
        trackUrl+=key;

        RequestQueue queue = Volley.newRequestQueue(SearchByParagraph.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                trackUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    JSONArray trackList = new JSONArray();
                    JSONObject message = response.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    trackList = body.getJSONArray("track_list");
                    JSONObject obj = trackList.getJSONObject(0);
                    JSONObject track = obj.getJSONObject("track");
                    track_id = track.getInt("track_id");
                    searchLyrics();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjReq);
    }

    private void searchLyrics() {

        String lyricsUrl ="https://api.musixmatch.com/ws/1.1/track.lyrics.get?track_id=";
        lyricsUrl+=track_id+"";
        String key = "&apikey=45a19f1f0dd81a4ff34ced6407b7af8e";
        lyricsUrl+=key;

        RequestQueue queue = Volley.newRequestQueue(SearchByParagraph.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                lyricsUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    String lyrics_body;
                    JSONObject message = response.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONObject lyrics = body.getJSONObject("lyrics");
                    lyrics_body = lyrics.getString("lyrics_body");
                    lyricsPara.setText(lyrics_body);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjReq);
    }
}