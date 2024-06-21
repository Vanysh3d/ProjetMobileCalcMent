package com.example.ProjectMobileCalcMent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ProjectMobileCalcMent.Database.ScoreDao;
import com.example.ProjectMobileCalcMent.Database.ScorebaseHelper;
import com.example.ProjectMobileCalcMent.Entities.Score;
import com.example.projetmobilecalcment.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button registerButton;

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameEditText = findViewById(R.id.usernameEditText);
        registerButton = findViewById(R.id.registerButton);

        score = getIntent().getIntExtra("score", 0);

        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();

            // Save the username and score in a database
            ScoreDao scoreDao = new ScoreDao(new ScorebaseHelper(this, "game.db", 1));
            Score scoreEntity = new Score();
            scoreEntity.setUsername(username);
            scoreEntity.setScore(score);
            scoreDao.create(scoreEntity);
            Intent intent = new Intent(RegistrationActivity.this, HomeScreen.class);
            startActivity(intent);
        });
    }
}