package com.U16.Math;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.U16.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ScoreboardActivity extends AppCompatActivity {

    Button done;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);

        Log.d("ScoreboardActivity @ Start", "Successfully started without any issues");

        done = findViewById(R.id.done);
        output = findViewById(R.id.output);

        done.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreboardActivity.this, MenuActivity.class);

            Log.d("ScoreboardActivity @ Done triggered", "The button done was pressed");

            startActivity(intent);
        });

        @SuppressLint("SetTextI18n") Thread t = new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 3333);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String receivedData = in.readLine();
                final String[] dataArray = receivedData.split(","); // server will return values together but need to split the ','

                runOnUiThread(() -> {
                    if(dataArray.length == 5) {
                        output.setText("Received data: " + dataArray[0] + ", " + dataArray[1] + ", " + dataArray[2] + ", " + dataArray[3] + ", " + dataArray[4]);
                        Log.d("ScoreboardActivity @ Server Data", "Data successfully retrieved");
                    } else {
                        output.setText("It appears that there was an issue with the server");
                        Log.d("ScoreboardActivity @ Server Data", "Data retrieval unsuccessful and unable to be retrieved");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
}
