package com.example.lyricsfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class SearchBySongAndArtist extends AppCompatActivity {

    private Button search;
    private TextInputEditText edtSong, edtArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_song_and_artist);

        search = findViewById(R.id.searchSong);
        edtSong = findViewById(R.id.edtSong);
        edtArtist = findViewById(R.id.edtArtist);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#457b9d"));

        actionBar.setBackgroundDrawable(colorDrawable);

        Window window = SearchBySongAndArtist.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(SearchBySongAndArtist.this, R.color.dark_blue));


    }
}