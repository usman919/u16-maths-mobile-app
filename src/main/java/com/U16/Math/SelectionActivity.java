package com.U16.Math;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.U16.R;

public class SelectionActivity extends AppCompatActivity {

    Button back;
    TextView heading;

    Button plus;
    Button minus;
    Button multiply;
    Button division;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Log.d("SelectionActivity @ Start", "Successfully started without any issues");

        back = findViewById(R.id.back);
        heading = findViewById(R.id.heading);

        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        multiply = findViewById(R.id.multiply);
        division = findViewById(R.id.division);

        sharedPref = getSharedPreferences("com.U16", Context.MODE_PRIVATE);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MenuActivity.class);

            Log.d("SelectionActivity @ Back triggered", "Button was pressed");

            startActivity(intent);
        });

        plus.setOnClickListener(v -> {
            saveOperatorToSharedPreferences("add");
            startDifficultyActivity();
        });

        minus.setOnClickListener(v -> {
            saveOperatorToSharedPreferences("minus");
            startDifficultyActivity();
        });

        multiply.setOnClickListener(v -> {
            saveOperatorToSharedPreferences("multiply");
            startDifficultyActivity();
        });

        division.setOnClickListener(v -> {
            saveOperatorToSharedPreferences("division");
            startDifficultyActivity();
        });
    }

    private void saveOperatorToSharedPreferences(String operator) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.operator), operator);
        editor.apply();
    }

    private void startDifficultyActivity() {
        Intent intent = new Intent(SelectionActivity.this, DifficultyActivity.class);
        startActivity(intent);
    }
}
