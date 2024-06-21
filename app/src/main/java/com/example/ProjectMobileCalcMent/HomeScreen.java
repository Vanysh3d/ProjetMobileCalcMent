package com.example.ProjectMobileCalcMent;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.projetmobilecalcment.R;

import java.util.Locale;

public class HomeScreen extends AppCompatActivity {

        private boolean isLanguageChanged = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_home_screen);

            Intent intentService = new Intent(this, MusicService.class);
            startService(intentService);

            Button buttonPlay = findViewById(R.id.button_play);
            buttonPlay.setOnClickListener(v -> {
                stopService(intentService);
                Intent intent = new Intent(HomeScreen.this, GameActivity.class);
                startActivity(intent);
            });

            Button buttonScore = findViewById(R.id.button_score);
            buttonScore.setOnClickListener(v -> {
                Intent intent = new Intent(HomeScreen.this, ScoreActivity.class);
                startActivity(intent);
            });

            Button buttonQuit = findViewById(R.id.button_quit);
            buttonQuit.setOnClickListener(v -> {
                finish();
                System.exit(0);
            });

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSecondary, typedValue, true);
            int color = ContextCompat.getColor(this, typedValue.resourceId);

            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                SpannableString s = new SpannableString(menuItem.getTitle());
                s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
                menuItem.setTitle(s);
            }
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.menu_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }

            if (id == R.id.menu_about) {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        public void setLocale(String lang) {
            String currentLang = getSharedPreferences("Settings", MODE_PRIVATE).getString("My_Lang", "");
            if (!currentLang.equals(lang)) {
                isLanguageChanged = true;
                Locale locale = new Locale(lang);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                // Save data to shared preferences
                SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                editor.putString("My_Lang", lang);
                editor.apply();
            }
        }

        public void loadLocale() {
            SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            String language = prefs.getString("My_Lang", "");
            setLocale(language);
        }

        @Override
        protected void onStop() {
            super.onStop();
            if (isFinishing()){
                Intent intent = new Intent(this, MusicService.class);
                stopService(intent);
            }
        }

        private BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                    Intent intentService = new Intent(context, MusicService.class);
                    stopService(intentService);
                }
            }
        };

        @Override
        protected void onResume(){
            super.onResume();
            if (!MusicService.isRunning) {
                Intent intentService = new Intent(this, MusicService.class);
                startService(intentService);
            }
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            registerReceiver(screenOffReceiver, filter);
        }

        @Override
        protected void onPause() {
            super.onPause();
            unregisterReceiver(screenOffReceiver);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            Intent intent = new Intent(this, MusicService.class);
            stopService(intent);
        }
    }