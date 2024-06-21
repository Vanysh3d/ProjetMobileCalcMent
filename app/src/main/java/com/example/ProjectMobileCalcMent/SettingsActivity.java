package com.example.ProjectMobileCalcMent;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.projetmobilecalcment.R;

public class SettingsActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isMusicEnabled = sharedPreferences.getBoolean("MusicEnabled", true);
        SwitchCompat switchMusic = findViewById(R.id.switch_music);
        switchMusic.setChecked(isMusicEnabled);
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
            editor.putBoolean("MusicEnabled", isChecked);
            editor.apply();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}