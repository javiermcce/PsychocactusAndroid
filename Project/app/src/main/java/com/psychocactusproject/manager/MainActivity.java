package com.psychocactusproject.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.psychocactusproject.R;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button playButton;
    private Button highScoresButton;
    private Button quitButton;
    private boolean debugModeOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("hey");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.buttonPlay);
        playButton.setOnClickListener(this);

        highScoresButton = findViewById(R.id.buttonHighScores);
        highScoresButton.setOnClickListener(this);

        quitButton = findViewById(R.id.buttonQuit);
        quitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.buttonPlay:
                i = new Intent(this, GameActivity.class);
                break;
            case R.id.buttonHighScores:
                System.out.println("HOLAAA");
                // i = new Intent(this, CanvasActivity.class);
                break;
            case R.id.buttonQuit:
                System.exit(0);
            default:
                throw new IllegalStateException();
        }
        startActivity(i);
    }
}
