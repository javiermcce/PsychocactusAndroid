package com.psychocactusproject.manager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.psychocactusproject.R;

import java.util.Locale;

public class GameActivity extends Activity implements View.OnClickListener {

    private int partA;
    private int partB;
    private int correctAnswer;
    private int wrongAnswer1;
    private int wrongAnswer2;
    private int score;
    private int level;
    
    private TextView partAText;
    private TextView partBText;
    private Button buttonChoiceText1;
    private Button buttonChoiceText2;
    private Button buttonChoiceText3;
    private TextView textScore;
    private TextView textLevel;

    @Override
    public void onClick(View v) {
        int userChoice;
        switch (v.getId()) {
            case R.id.buttonChoice1:
                userChoice = Integer.parseInt(buttonChoiceText1.getText().toString());
                break;
            case R.id.buttonChoice2:
                userChoice = Integer.parseInt(buttonChoiceText2.getText().toString());
                break;
            case R.id.buttonChoice3:
                userChoice = Integer.parseInt(buttonChoiceText3.getText().toString());
                break;
            default:
                userChoice = 0;
        }
        String shownMessage;
        if (userChoice == this.correctAnswer) {
            shownMessage = "Well done!";
            this.updateScoreAndLevel(true);
        } else {
            shownMessage = "Sorry, that's wrong!";
            this.updateScoreAndLevel(false);
        }
        Toast.makeText(getApplicationContext(), shownMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.initNumbers();

        this.partAText = findViewById(R.id.textPartA);
        this.partBText = findViewById(R.id.textPartB);
        this.buttonChoiceText1 = findViewById(R.id.buttonChoice1);
        this.buttonChoiceText2 = findViewById(R.id.buttonChoice2);
        this.buttonChoiceText3 = findViewById(R.id.buttonChoice3);
        this.textScore = findViewById(R.id.textScore);
        this.textLevel = findViewById(R.id.textLevel);

        this.buttonChoiceText1.setOnClickListener(this);
        this.buttonChoiceText2.setOnClickListener(this);
        this.buttonChoiceText3.setOnClickListener(this);

        this.updateTextLabels();
    }

    private void initNumbers() {
        this.score = 0;
        this.level = 1;
        this.generateNumbers();
    }

    private void generateNumbers() {
        this.partA = randomNumber(this.level * 3);
        this.partB = randomNumber(this.level * 3);
        this.correctAnswer = this.partA * this.partB;
        this.wrongAnswer1 = this.correctAnswer + randomNumber(10);
        this.wrongAnswer2 = this.correctAnswer + randomNumber(10);
        while (this.existEqualNumber()) {
            this.generateNumbers();
        }
    }

    private boolean existEqualNumber() {
        return this.correctAnswer == this.wrongAnswer1
                || this.correctAnswer == this.wrongAnswer2
                || this.wrongAnswer1 == this.wrongAnswer2;
    }

    private int randomNumber(long maxNumber) {
        return randomNumber(maxNumber, true);
    }

    private int randomNumber(long maxNumber, boolean negativeAllowed) {
        int result = (int) (Math.random() * maxNumber);
        if (negativeAllowed) {
            // Could multiply per 1 or minus 1 with a 0.5 chance each
            result *= (Math.random() < 0.5 ? 1 : -1);
        }
        return result;
    }

    private void updateScoreAndLevel(boolean correct) {
        if (correct) {
            this.score += this.level;
            this.level++;
        } else {
            this.score = 0;
            this.level = 1;
        }
        this.generateNumbers();
        this.updateTextLabels();
    }

    private void updateTextLabels() {

        this.partAText.setText(String.valueOf(this.partA));
        this.partBText.setText(String.valueOf(this.partB));

        int randomOrder = this.randomNumber(3, false);
        Button[] buttonOptions = new Button[3];
        buttonOptions[0] = buttonChoiceText1;
        buttonOptions[1] = buttonChoiceText2;
        buttonOptions[2] = buttonChoiceText3;
        String textValue;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                textValue = String.valueOf(this.correctAnswer);
            } else if (i == 1) {
                textValue = String.valueOf(this.wrongAnswer1);
            } else {
                textValue = String.valueOf(this.wrongAnswer2);
            }
            buttonOptions[randomOrder].setText(textValue);
            randomOrder = (randomOrder + 1) % 3;
        }

        this.textScore.setText(String.format(Locale.getDefault(),"Score: %1$d", this.score));
        this.textLevel.setText(String.format(Locale.getDefault(),"Level: %1$d", this.level));
    }
}