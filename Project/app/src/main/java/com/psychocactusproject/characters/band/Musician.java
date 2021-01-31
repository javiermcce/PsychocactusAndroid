package com.psychocactusproject.characters.band;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.graphics.manager.AnimationController;
import com.psychocactusproject.input.InputController;

public abstract class Musician extends AnimationController {

    TextView textView;
    GameEngine gameEngine;

    protected Musician(GameEngine gameEngine, View view) {
        super(gameEngine);
        this.gameEngine = gameEngine;
        this.textView = view.findViewById(R.id.txt_score);
    }

    // Equivalente a Player según la guía

    @Override
    public void initialize() {

    }

    @Override
    public void update(long elapsedMillis, GameEngine gameEngine) {
        InputController inputController = gameEngine.getInputController();
        // aquí debería tener un gestor de movimiento que funcione como un script en un
        // hilo independiente, input controller tiene poco que ver en esto, pero lo voy a
        // mantener hasta implementar mi propio código
        // this.setPositionX();
        // this.setPositionX(this.getPositionX() + 1);
        // this.setPositionY(this.getPositionY() + 1);
    }

    @Override
    public void draw(Canvas canvas) {
        this.getMatrix().reset();
        this.getMatrix().postTranslate((float) this.getPositionX(), (float) this.getPositionY());
        canvas.drawBitmap(this.getAnimationImage(), this.getMatrix(), null);
        if (this.getPositionX() < 500) {
            this.setPositionX(this.getPositionX() + 1);
        }
        if (this.getPositionY() < 200) {
            this.setPositionY(this.getPositionY() + 1);
        }
        if (textView != null) {
            this.gameEngine.getActivity().runOnUiThread(() ->
                    textView.setText("[" + getPositionX() + ", " + getPositionY() + "]"));

        }
    }
}
