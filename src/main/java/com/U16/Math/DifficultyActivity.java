package com.U16.Math;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.U16.R;

public class DifficultyActivity extends AppCompatActivity {

    Button back;
    Button easy;
    Button medium;
    Button hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        Log.d("DifficultyActivity @ Start", "Successfully started without any issues");

        back = findViewById(R.id.back);
        easy = findViewById(R.id.easy);
        medium = findViewById(R.id.medium);
        hard = findViewById(R.id.hard);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(DifficultyActivity.this, SelectionActivity.class);
            startActivity(intent);
        });

        easy.setOnClickListener(v -> {
            Intent intent = new Intent(DifficultyActivity.this, GameActivity.class);
            intent.putExtra("difficulty", "easy");
            startActivity(intent);
        });

        medium.setOnClickListener(v -> {
            Intent intent = new Intent(DifficultyActivity.this, GameActivity.class);
            intent.putExtra("difficulty", "medium");
            startActivity(intent);
        });

        hard.setOnClickListener(v -> {
            Intent intent = new Intent(DifficultyActivity.this, GameActivity.class);
            intent.putExtra("difficulty", "hard");
            startActivity(intent);
        });
    }
}
