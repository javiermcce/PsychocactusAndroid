package com.psychocactusproject.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.psychocactusproject.R;
import com.psychocactusproject.characters.band.Bass;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.graphics.views.GameView;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.BasicInputController;
import com.psychocactusproject.input.InputController;

import java.util.concurrent.BrokenBarrierException;

public class GameFragment extends GameBaseFragment implements View.OnClickListener {

    private GameEngine gameEngine;

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
        view.findViewById(R.id.button_play_pause).setOnClickListener(this);
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                observer.removeOnGlobalLayoutListener(this);
                GameView gameView = (GameView) getView().findViewById(R.id.gameView);
                gameEngine = GameEngine.getInstance(getActivity(), gameView);
                gameEngine.adjustScreenAspectRatio(gameView.getWidth(), gameView.getHeight());
                gameEngine.setInputController(new BasicInputController(getView()));
                gameEngine.addGameEntity(new Bass(gameEngine, getView().findViewById(R.id.gameView)));
                gameEngine.startGame();
            }
        });

        //GameView gameView = getView().findViewById(R.id.gameView);
        //this.gameEngine = new GameEngine(getActivity(), gameView);
        //gameEngine.addGameEntity(new ScoreGameObject(view, R.id.txt_score));
        //this.gameEngine.setInputController(new InputController());
        /*
        * Este es el lugar en que meter los músicos, en lugar de new Player añadir a los 4
        * */
        //this.gameEngine.addGameEntity(new Player(getView()));
        //this.gameEngine.startGame();
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

//    private void startOrStop() {
//        Button button = (Button) getView().findViewById(R.id.btn_start_stop);
//        Button playPauseButton = (Button) getView().findViewById(R.id.btn_play_pause);
//        if (mGameEngine.isRunning()) {
//            mGameEngine.stopGame();
//            button.setText(R.string.start);
//            playPauseButton.setEnabled(false);
//        }
//        else {
//            mGameEngine.startGame();
//            button.setText(R.string.stop);
//            playPauseButton.setEnabled(true);
//            playPauseButton.setText(R.string.pause);
//        }
//    }

}