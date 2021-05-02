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

import org.json.JSONException;
import org.json.JSONObject;

public class SearchBySongAndArtist extends AppCompatActivity {

    private Button search;
    private TextInputEditText edtSong, edtArtist;
    private TextView lyricsSong;

    private String urlBase = "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_song_and_artist);

        search = findViewById(R.id.searchSong);
        edtSong = findViewById(R.id.edtSong);
        edtArtist = findViewById(R.id.edtArtist);
        lyricsSong = findViewById(R.id.lyricsSong);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#457b9d"));

        actionBar.setBackgroundDrawable(colorDrawable);

        Window window = SearchBySongAndArtist.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(SearchBySongAndArtist.this, R.color.dark_blue));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAndRequestResponse();
            }
        });
    }

    private void sendAndRequestResponse() {


        String url = urlBase;
        String key = "&apikey=45a19f1f0dd81a4ff34ced6407b7af8e";
        url+="q_track="+edtSong.getText().toString()+"";
        url+="&q_artist="+edtArtist.getText().toString()+"";
        url+=key;

        RequestQueue queue = Volley.newRequestQueue(SearchBySongAndArtist.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    String lyrics_body;
                    JSONObject message = response.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONObject lyrics = body.getJSONObject("lyrics");
                    lyrics_body = lyrics.getString("lyrics_body");
                    lyricsSong.setText(lyrics_body);

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