package com.U16.Math;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.U16.R;

import java.util.Random;

public class MenuActivity extends AppCompatActivity {

    TextView title;
    TextView tagline;

    Button play;
    Button scoreboard;
    Button exit;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Log.d("MenuActivity @ Start", "Successfully started without any issues");

        title = findViewById(R.id.title);
        tagline = findViewById(R.id.tagline);

        play = findViewById(R.id.play);
        scoreboard = findViewById(R.id.scoreboard);
        exit = findViewById(R.id.exit);

        tagline.setText(setTagline());

        play.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SelectionActivity.class);

            Log.d("MenuActivity @ Button Press", "A button was pressed");

            startActivity(intent);
        });

        scoreboard.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ScoreboardActivity.class);

            Log.d("MenuActivity @ Button Press", "A button was pressed");

            startActivity(intent);
        });

        exit.setOnClickListener(v -> {
            Toast toast = Toast.makeText(getApplicationContext(), "Closing application", Toast.LENGTH_SHORT);
            toast.show();

            Log.d("MenuActivity @ Exit Button Triggered", "The exit button has been triggered and so the application will be ending");
            System.exit(0);
            new Handler().postDelayed(toast::cancel, 5000);
        });
    }

    private String setTagline() {
        String[] taglines = {"Math++ is so much fun!", "Maths made easy", "It's super easy & fun!", "Learn maths for free!", "Test out your maths knowledge"};
        return taglines[random.nextInt(taglines.length-1)];
    }
}
