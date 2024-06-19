package com.example.ProjectMobileCalcMent;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ProjectMobileCalcMent.Database.ScoreDao;
import com.example.ProjectMobileCalcMent.Database.ScorebaseHelper;
import com.example.ProjectMobileCalcMent.Entities.Score;
import com.example.projetmobilecalcment.R;

import java.util.List;

// ScoreActivity.java
public class ScoreActivity extends AppCompatActivity {

    private ListView scoreListView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreListView = findViewById(R.id.scoreListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        scoreListView.setAdapter(adapter);

        ScoreDao scoreDao = new ScoreDao(new ScorebaseHelper(this, "game.db", 1));
        List<Score> scores = scoreDao.getAllScores();

        if (scores.isEmpty()) {
            scoreListView.setVisibility(View.GONE);
        } else {
            for (Score score : scores) {
                adapter.add(score.getUsername() + ": " + score.getScore());
            }
        }
    }
}