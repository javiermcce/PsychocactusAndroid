package com.psychocactusproject.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.graphics.views.GameView;
import com.psychocactusproject.input.TouchInputController;

public class GameFragment extends GameBaseFragment implements View.OnClickListener {

    private GameEngine gameEngine;
    private static TextView debugTextView;
    private static Activity activity;

    public static void setDebugText(String text){
        activity.runOnUiThread(() -> { debugTextView.setText(text); });
    }

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // El código a ejecutar por el botón de pausa es el ubicado en esta clase
        view.findViewById(R.id.button_play_pause).setOnClickListener(this);
        // Arranca el motor de juego a través de un listener cuando la vista ya haya sido creada
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Tan pronto como se acceda al código del listener, se borra la escucha
                observer.removeOnGlobalLayoutListener(this);
                // Interfaz GameView implementada por la clase que extiende SurfaceView
                GameView gameView = (GameView) getView().findViewById(R.id.gameView);
                if (GameEngine.DEBUGGING) {
                    GameFragment.debugTextView = getView().findViewById(R.id.txt_debug);
                    GameFragment.debugTextView.setVisibility(View.GONE);
                    activity = getActivity();
                } else {
                    ((TextView) getView().findViewById(R.id.txt_debug)).setText("");
                }
                // El motor es creado con la actividad y la vista
                gameEngine = new GameEngine(getActivity(), gameView);
                // El gestor de controles es vinculado al motor
                gameEngine.setInputController(new TouchInputController(gameEngine, getView()));
                // Arranca el juego
                gameEngine.startGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_play_pause) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (gameEngine.isRunning()){
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        if (gameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
        return false;
    }

    private void pauseGameAndShowPauseDialog() {
        gameEngine.pauseGame();
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pause_dialog_title)
                .setMessage(R.string.pause_dialog_message)
                .setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        gameEngine.resumeGame();
                    }
                })
                .setNegativeButton(R.string.stop, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        gameEngine.stopGame();
                        ((GameActivity)getActivity()).navigateBack();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        gameEngine.resumeGame();
                    }
                })
                .create()
                .show();

    }

    private void playOrPause() {
        Button button = (Button) getView().findViewById(R.id.button_play_pause);
        if (gameEngine.isPaused()) {
            gameEngine.resumeGame();
            button.setText(R.string.pause);
        }
        else {
            gameEngine.pauseGame();
            button.setText(R.string.resume);
        }
    }
}