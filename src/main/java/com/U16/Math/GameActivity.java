package com.U16.Math;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.U16.R;

public class GameActivity extends AppCompatActivity {

    private TextView score;
    private TextView questionCount;
    private TextView timer;
    private TextView question;
    private EditText input;
    private Button enter_btn;

    private int time;
    private int totalQuestions;
    private int currentQuestion;
    private int currentScore;
    private CountDownTimer countDownTimer;

    private int operand1;
    private int operand2;
    private String operator;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d("GameActivity @ Start", "Successfully started without any issues");

        String difficulty = getIntent().getStringExtra("difficulty");
        SharedPreferences sharedPref = getSharedPreferences("com.U16", Context.MODE_PRIVATE);
        operator = sharedPref.getString(getString(R.string.operator), null);

        score = findViewById(R.id.score);
        questionCount = findViewById(R.id.questionCount);
        timer = findViewById(R.id.timer);
        question = findViewById(R.id.question);
        input = findViewById(R.id.input);
        enter_btn = findViewById(R.id.enter_btn);

        currentQuestion = 0;
        currentScore = 0;

        switch (difficulty) {
            case "easy":
                time = 30000;
                totalQuestions = 10;
                break;
            case "medium":
                time = 20000;
                totalQuestions = 10;
                break;
            case "hard":
                time = 10000;
                totalQuestions = 10;
                break;
        }

        enter_btn.setOnClickListener(v -> handleEnterButtonPressed());

        startGame();
    }

    private void startGame() {
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("Time: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                if (currentScore > 0) {
                    Toast.makeText(GameActivity.this, "Time's up! Your final score is " + currentScore, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameActivity.this, "Time's up! You didn't score any points.", Toast.LENGTH_SHORT).show();
                }
                // Save the score in the intent using setIntent()
                finish();
            }
        };

        generateQuestion();
        countDownTimer.start();
    }

    private void generateQuestion() {
        int minValue = -12;
        int maxValue = 12;

        if (operator != null) {
            if (operator.equals("division") || operator.equals("minus")) {
                minValue = 1;
            }
        }

        operand1 = generateRandomNumber(minValue, maxValue);
        operand2 = generateRandomNumber(minValue, maxValue);

        String operatorSymbol = getOperatorSymbol(operator);

        String questionText = "What is " + operand1 + " " + operatorSymbol + " " + operand2 + "?";
        question.setText(questionText);
    }

    private void handleEnterButtonPressed() {
        // Reset the timer by canceling the current timer and starting a new one
        countDownTimer.cancel();
        countDownTimer.start();

        checkAnswer();
    }

    private int generateRandomNumber(int minValue, int maxValue) {
        if (minValue < 0 && maxValue >= 0) {
            // If the range spans across zero, adjust the maximum value to include zero
            maxValue++;
        }

        int range = Math.abs(maxValue - minValue) + 1;

        int randomNumber = (int) (Math.random() * range) + minValue;
        return randomNumber;
    }

    private void checkAnswer() {
        String answerString = input.getText().toString().trim();

        if (!answerString.isEmpty()) {
            try {
                int answer = Integer.parseInt(answerString);
                int expectedAnswer = 0;

                switch (operator) {
                    case "add":
                        expectedAnswer = operand1 + operand2;
                        break;
                    case "minus":
                        expectedAnswer = operand1 - operand2;
                        break;
                    case "multiply":
                        expectedAnswer = operand1 * operand2;
                        break;
                    case "division":
                        expectedAnswer = operand1 / operand2;
                        break;
                }

                // Adjust expectedAnswer for subtraction and division questions involving negative numbers
                if ((operator.equals("minus") || operator.equals("division")) && (operand1 < 0 || operand2 < 0)) {
                    expectedAnswer--;
                }

                boolean isCorrect = answer == expectedAnswer;

                if (isCorrect) {
                    currentScore++;
                    showPopup("correct_ans_popup");
                } else {
                    showPopup("wrong_ans_popup");
                }

                currentQuestion++;
                questionCount.setText("Question: " + currentQuestion + "/" + totalQuestions);

                if (currentQuestion < totalQuestions) {
                    generateQuestion();
                } else {
                    Toast.makeText(GameActivity.this, "Game over! Your final score is " + currentScore, Toast.LENGTH_SHORT).show();
                    // Save the score in the intent using setIntent()
                    finish();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(GameActivity.this, "Invalid answer format. Please enter a valid integer.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(GameActivity.this, "Please enter an answer.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getOperatorSymbol(String operator) {
        String symbol = "";
        switch (operator) {
            case "add":
                symbol = "+";
                break;
            case "minus":
                symbol = "-";
                break;
            case "multiply":
                symbol = "×";
                break;
            case "division":
                symbol = "÷";
                break;
        }
        return symbol;
    }

    private void showPopup(String popupName) {
        int popupDuration = 5000; // 5 seconds

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(getResources().getIdentifier(popupName, "layout", getPackageName()), null);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        // Increment score based on correct answer
        if (popupName.equals("correct_ans_popup")) {
            currentScore++;
            score.setText("Score: " + currentScore);
        }

        new Handler().postDelayed(() -> {
            popupWindow.dismiss();
            popupWindow = null;
        }, popupDuration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}


//        questionRef = totalQuestions;
//
//        getIntent().putExtra("score", 0);
//
//        // set up the timer
//        new CountDownTimer(time, 1000) {
//            @SuppressLint("SetTextI18n")
//            public void onTick(long millisUntilFinished) {
//                String time = getResources().getString(R.string.timer);
//
//                timer.setText(time + " " + millisUntilFinished / 1000);
//
//                Log.d("GameActivity @ Time", "Count down time remaining: " + timer.getText().subSequence(time.length() + 1, timer.getText().length()));
//            }
//
//            public void onFinish() {
//                String timesUpString = getResources().getString(R.string.time_up);
//                timer.setText(timesUpString);
//            }
//        }.start();
//
//        enter_btn.setOnClickListener(v -> {
//            Log.d("GameActivity @ Enter triggered", "Button pressed which triggered a different .xml to play");
//
//            setContentView(R.layout.correct_ans_popup);
//            new Handler().postDelayed(() -> {
//                setContentView(R.layout.activity_game);
//            }, 5000); // 5000 milliseconds = 5 seconds
//        });
//
//
////         FUNCTIONING SWAP
//        enter_btn.setOnClickListener(v -> {
//            setContentView(R.layout.wrong_ans_popup);
//            new Handler().postDelayed(() -> {
//                setContentView(R.layout.activity_game);
//            }, 5000); // 5000 milliseconds = 5 seconds
//        });
//
//
//        final String[] x = {""};
//
//        x[0] = "19";
//
//        enter_btn.setOnClickListener(v -> {
//            x[0] += String.valueOf(input.getText());
//        });
//
//        if (Integer.parseInt(x[0]) == correctAns) {
//            setContentView(R.layout.correct_ans_popup);
//            new Handler().postDelayed(() -> setContentView(R.layout.activity_game), 5000); // 5seconds
//        }
//
//
//        // game loop
//
//        for (int i = 0; i <= totalQuestions; i++) {
//            question.setText(generateQuestion(operator));
//
//            if (input.getText().equals(String.valueOf(correctAns))) {
//
//                // display correct ans xml & switch back to normal after 5 secs
//                setContentView(R.layout.correct_ans_popup);
//                new Handler().postDelayed(() -> setContentView(R.layout.activity_game), 5000); // 5seconds
//
//                scoreCount++;
//
//            } else {
//
//                // display wrong ans xml & switch back to normal after 5 secs
//                setContentView(R.layout.wrong_ans_popup);
//                new Handler().postDelayed(() -> setContentView(R.layout.activity_game), 5000); // 5seconds
//
//
//            }
//        }
////         lead player to the end screen
//        Intent intent = new Intent(GameActivity.this, EndActivity.class);
//        Log.d("GameActivity @ End", "Transferring from GameActivity to EndActivity");
//        startActivity(intent);
//    }
//
//    // generate a question based on the operator
//    public String generateQuestion(String op) {
//        String rv = "";
//        Random random = new Random();
//        switch (op) {
//            case "+":
//                num1 = random.nextInt(12 - (-12) + 1) + -12;
//                num2 = random.nextInt(12 - (-12) + 1) + -12;
//                rv =  num1 + " + " + num2;
//                correctAns = num1 + num2;
//                break;
//            case "-":
//                num1 = random.nextInt(13);
//                num2 = random.nextInt(13);
//                rv = num1 + " - " + num2;
//                correctAns = num1 - num2;
//                break;
//            case "/":
//                num1 = random.nextInt(13);
//                num2 = random.nextInt(13);
//                rv = num1 + " ÷ " + num2;
//                correctAns = num1 / num2;
//                break;
//            case "*":
//                num1 = random.nextInt(12 - (-12) + 1) + -12;
//                num2 = random.nextInt(12 - (-12) + 1) + -12;
//                rv = num1 + " × " + num2;
//                correctAns = num1 * num2;
//                break;
//        }
//        return rv;
//    }
//
//    public void update() {
//        score.setText(score + " " + scoreCount + questionRef);
//    }
