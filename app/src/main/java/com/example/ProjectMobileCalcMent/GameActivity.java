package com.example.ProjectMobileCalcMent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetmobilecalcment.R;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private TextView livesTextView;
    private TextView calculationTextView;
    private EditText answerEditText;

    private int score = 0;
    private int lives = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreTextView = findViewById(R.id.scoreTextView);
        livesTextView = findViewById(R.id.livesTextView);
        calculationTextView = findViewById(R.id.calculationTextView);
        answerEditText = findViewById(R.id.answerEditText);

        // Generate a calculation and display it in calculationTextView
        String calculation = generateCalculation();
        calculationTextView.setText(calculation);

        // Check the user's answer. If it's correct, increase the score. If it's wrong, decrease the lives.
        answerEditText.setOnEditorActionListener((v, actionId, event) -> {
            String userAnswer = answerEditText.getText().toString();
            if (isAnswerCorrect(calculation, userAnswer)) {
                score++;
                updateScore();
            } else {
                lives--;
                updateLives();
                if (lives == 0) {
                    // If the user has no more lives, open the registration activity and pass the score via Intent
                    Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                }
            }
            return true;
        });
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
    }

    private void updateLives() {
        livesTextView.setText("Lives: " + lives);
    }

    private String generateCalculation() {
        Random random = new Random();
        String[] operations = {"+", "-", "*", "/"};

        int numberOfNumbers = random.nextInt(11);
        StringBuilder calculation = new StringBuilder();

        for (int i = 0; i < numberOfNumbers; i++) {
            int number = random.nextInt(101);
            String operation = operations[random.nextInt(operations.length)];

            calculation.append(number).append(" ").append(operation).append(" ");
        }

        // Add the last number
        calculation.append(random.nextInt(101));

        return calculation.toString();
    }

    private boolean isAnswerCorrect(String calculation, String userAnswer) {
        String[] parts = calculation.split(" ");
        double result = Double.parseDouble(parts[0]);

        for (int i = 1; i < parts.length; i += 2) {
            String operation = parts[i];
            double number = Double.parseDouble(parts[i + 1]);

            switch (operation) {
                case "+":
                    result += number;
                    break;
                case "-":
                    result -= number;
                    break;
                case "*":
                    result *= number;
                    break;
                case "/":
                    if (number != 0) {
                        result /= number;
                    } else {
                        return false;
                    }
                    break;
            }
        }

        double userResult = Double.parseDouble(userAnswer);

        // Compare the results with a small tolerance to account for floating point errors
        return Math.abs(result - userResult) < 0.0001;
    }
}