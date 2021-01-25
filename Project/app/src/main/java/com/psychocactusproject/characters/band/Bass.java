package com.psychocactusproject.characters.band;

import android.graphics.Canvas;
import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;

import java.util.LinkedList;
import java.util.List;

public class Bass extends Musician {

    public Bass(GameEngine gameEngine, View view) {
        super(gameEngine, view);
    }

    @Override
    protected List<int[]> obtainBitmapCodes() {
        List<int[]> bitmapCodesList = new LinkedList();
        int[] list = new int[8];
        list[0] = R.drawable.bass_idle_1;
        list[1] = R.drawable.bass_idle_2;
        list[2] = R.drawable.bass_idle_3;
        list[3] = R.drawable.bass_idle_4;
        list[4] = R.drawable.bass_idle_5;
        list[5] = R.drawable.bass_idle_6;
        list[6] = R.drawable.bass_idle_7;
        list[7] = R.drawable.bass_idle_8;
        bitmapCodesList.add(list);
        /*
        int[] list = new int[8];
        list[0] = R.drawable.bass_idle_1;
        list[1] = R.drawable.bass_idle_2;
        list[2] = R.drawable.bass_idle_3;
        list[3] = R.drawable.bass_idle_4;
        list[4] = R.drawable.bass_idle_5;
        list[5] = R.drawable.bass_idle_6;
        list[6] = R.drawable.bass_idle_7;
        list[7] = R.drawable.bass_idle_8;
        bitmapCodesList.add(list);
        * */
        return bitmapCodesList;
    }

    @Override
    protected String[] obtainActionNames() {
        return new String[] {"Idle"};
    }

    @Override
    protected String obtainEntityRole() {
        return "Bass";
    }

    @Override
    public String obtainCharacterName() {
        return "Ronaldo";
    }
}
