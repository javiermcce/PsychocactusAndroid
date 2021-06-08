package com.psychocactusproject.android;

import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.core.view.WindowInsetsControllerCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;


import com.psychocactusproject.R;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.TouchInputController;

import org.jetbrains.annotations.NotNull;

import static androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;

public class SimpleFragment extends GameBaseFragment implements View.OnClickListener {

    private GameEngine gameEngine;

    public static SimpleFragment fragmentInstance() {
        return new SimpleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Arranca el motor de juego a través de un listener cuando la vista ya haya sido creada
        ViewTreeObserver observer = view.getViewTreeObserver();
        final View createdView = view;
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Tan pronto como se acceda al código del listener, se borra la escucha
                ViewTreeObserver currentObserver = createdView.getViewTreeObserver();
                if (currentObserver.isAlive()) {
                    currentObserver.removeOnGlobalLayoutListener(this);
                }
                // getActivity().getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.statusBars());

                // Es obtenida la instancia de activity, y su conversión al tipo del proyecto
                SimpleActivity simpleActivity = (SimpleActivity) getActivity();
                // simpleActivity.getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.statusBars());
                WindowInsetsControllerCompat insetsControllerCompat = new WindowInsetsControllerCompat(simpleActivity.getWindow(), simpleActivity.getWindow().getDecorView());
                insetsControllerCompat.setSystemBarsBehavior(BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                insetsControllerCompat.hide(WindowInsetsCompat.Type.statusBars());
                // Controlador principal de la impresión de los gráficos
                SurfaceGameView surfaceGameView = getView().findViewById(R.id.simpleGameView);
                // El motor es creado con la actividad y la vista
                gameEngine = new GameEngine(simpleActivity, surfaceGameView);
                // El gestor de controles es vinculado al motor
                gameEngine.setInputController(new TouchInputController(gameEngine, getView()));
                // Arranca el juego
                gameEngine.startGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        System.out.println("test");
    }



    @Override
    public void onPause() {
        // REVISAR POLÍTICAS Y BOTONES ANDROID
        super.onPause();
        if (gameEngine.isRunning() && !gameEngine.isPaused()){
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        // REVISAR POLÍTICAS Y BOTONES ANDROID
        super.onDestroy();
        gameEngine.stopGameApp();
    }

    @Override
    public boolean onBackPressed() {
        // REVISAR POLÍTICAS Y BOTONES ANDROID
        if (gameEngine.isRunning() && !gameEngine.isPaused()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
        return false;
    }

    private void pauseGameAndShowPauseDialog() {
        // REVISAR POLÍTICAS Y BOTONES ANDROID
        gameEngine.pauseGame();
    }
}