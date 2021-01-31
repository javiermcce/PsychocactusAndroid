package com.psychocactusproject.manager;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;

public class ScoreGameObject /* extends GameEntity */ {

    private final TextView mText;
    private long mTotalMilis;

    public ScoreGameObject(View view, int viewResId) {
        mText = (TextView) view.findViewById(viewResId);
    }
    /*
    @Override
    public void initialize() {
        mTotalMilis = 0;
    }

    @Override
    public void update(long elapsedMillis, GameEngine gameEngine) {
        mTotalMilis += elapsedMillis;
    }

    @Override
    public void draw(Canvas canvas) {
        mText.setText(String.valueOf(mTotalMilis));
    }


    @Override
    public String obtainCharacterName() {
        return "Score Table";
    } */

}
