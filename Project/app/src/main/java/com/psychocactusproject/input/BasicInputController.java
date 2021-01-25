package com.psychocactusproject.input;

import android.view.MotionEvent;
import android.view.View;

public class BasicInputController extends InputController implements View.OnTouchListener {

    public BasicInputController(View view) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
