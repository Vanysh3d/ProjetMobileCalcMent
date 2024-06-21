package com.example.ProjectMobileCalcMent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.util.Stack;

public class GameActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    private CountDownTimer countDownTimer;

    private TextView scoreTextView;
    private TextView livesTextView;
    private TextView calculationTextView;
    private EditText answerEditText;

    private int score = 0;
    private int lives = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mediaPlayer = MediaPlayer.create(this, R.raw.puzzles);
        mediaPlayer.setLooping(true);

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isMusicEnabled = sharedPreferences.getBoolean("MusicEnabled", true);
        if (isMusicEnabled) {
            mediaPlayer.start();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreTextView = findViewById(R.id.scoreTextView);
        livesTextView = findViewById(R.id.livesTextView);
        calculationTextView = findViewById(R.id.calculationTextView);
        answerEditText = findViewById(R.id.answerEditText);

        // Generate a calculation and display it in calculationTextView
        final String[] calculation =new String[] { generateCalculation() };
        calculationTextView.setText(calculation[0]);

        // Check the user's answer. If it's correct, increase the score. If it's wrong, decrease the lives.
        answerEditText.setOnEditorActionListener((v, actionId, event) -> {
            String userAnswer = answerEditText.getText().toString();
            if (isAnswerCorrect(calculation[0], userAnswer)) {
                score++;
                updateScore();
            } else {
                lives--;
                updateLives();
                if (lives == 0) {
                    // If the user has no more lives, open the registration activity and pass the score via Intent
                    Intent intent = new Intent(GameActivity.this, RegistrationActivity.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }
            }
            // Generate a new calculation and display it
            calculation[0] = generateCalculation();
            calculationTextView.setText(calculation[0]);

            // Clear the answer field
            answerEditText.setText("");

            countDownTimer.cancel();
            countDownTimer.start();
            return true;
        });

        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                TextView timerTextView = findViewById(R.id.timerTextView);
                timerTextView.setText("Time: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                lives--;
                updateLives();
                if (lives == 0) {
                    Intent intent = new Intent(GameActivity.this, RegistrationActivity.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                } else {
                    calculation[0] = generateCalculation();
                    calculationTextView.setText(calculation[0]);
                    start();
                }
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isMusicEnabled = sharedPreferences.getBoolean("MusicEnabled", true);
        if (isMusicEnabled) {
            mediaPlayer.start();
        }
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
    }

    private void updateLives() {
        livesTextView.setText("Lives: " + lives);
    }

    private String generateCalculation() {
        Random random = new Random();
        String[] operations = {"+", "-", "*"};

        String calculation;
        double result;

        do {
            int numberOfNumbers = random.nextInt(2) + 1;
            StringBuilder calculationBuilder = new StringBuilder();

            for (int i = 0; i < numberOfNumbers; i++) {
                int number = random.nextInt(51);
                String operation = operations[random.nextInt(operations.length)];

                calculationBuilder.append(number).append(" ").append(operation).append(" ");
            }

            // Add the last number
            calculationBuilder.append(random.nextInt(51));

            calculation = calculationBuilder.toString();
            result = calculateResult(calculation);
        } while (result <= 0); // Continue generating a new calculation until a positive result is found

        return calculation;
    }

    private double calculateResult(String calculation) {
        String[] parts = calculation.split(" ");
        Stack<Double> numbers = new Stack<>();
        Stack<String> operations = new Stack<>();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            if ("+".equals(part) || "-".equals(part) || "*".equals(part)) {
                while (!operations.isEmpty() && "*".equals(operations.peek())) {
                    double right = numbers.pop();
                    double left = numbers.pop();
                    numbers.push(left * right);
                    operations.pop();
                }
                operations.push(part);
            } else {
                numbers.push(Double.parseDouble(part));
            }
        }

        while (!operations.isEmpty()) {
            double right = numbers.pop();
            double left = numbers.pop();
            String operation = operations.pop();

            switch (operation) {
                case "+":
                    numbers.push(left + right);
                    break;
                case "-":
                    numbers.push(left - right);
                    break;
                case "*":
                    numbers.push(left * right);
                    break;
            }
        }

        return numbers.pop();
    }

    private boolean isAnswerCorrect(String calculation, String userAnswer) {
        if(userAnswer.isEmpty()) {
            return false;
        }
        double result = calculateResult(calculation);
        double userResult = Double.parseDouble(userAnswer);
        double tolerance = 0.0001;

        return Math.abs(result - userResult) < tolerance;
    }
}