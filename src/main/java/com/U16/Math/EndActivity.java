package com.U16.Math;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.U16.R;

public class EndActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_screen);

        Log.d("EndActivity @ Start", "Successfully started without any issues");

        TextView score_output;

        score_output = findViewById(R.id.score_output);

        String score = getIntent().getStringExtra("score");

        score_output.setText("Score: " + score);

        // shut down the program after 30 seconds if the user doesn't already shut it down
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                Log.d("EndActivity @ End", "Awaiting 30 seconds before closing application");
                System.exit(0);
            }
        }.start();
    }
}
