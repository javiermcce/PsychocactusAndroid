package com.psychocactusproject.android;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.psychocactusproject.R;
import com.psychocactusproject.databinding.ActivityFullscreenBinding;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.graphics.manager.ResourceLoader;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.TouchInputController;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

// FULL SCREEN ACTIVITY ===== GAME ACTIVITY
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivityFullscreenBinding binding;

    // MI CÓDIGO

    private GameEngine gameEngine;
    private static final String TAG_FRAGMENT = "content";
    private DebugHelper debugHelper;


    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context,
                             @NonNull AttributeSet attrs) {
        View createdView = super.onCreateView(parent, name, context, attrs);
        final FullscreenActivity activity = this;
        final ViewTreeObserver observer = createdView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Tan pronto como se acceda al código del listener, se borra la escucha
                observer.removeOnGlobalLayoutListener(this);
                // Es obtenida la instancia de activity, y su conversión al tipo del proyecto
                // Controlador principal de la impresión de los gráficos
                SurfaceGameView surfaceGameView = createdView.findViewById(R.id.simpleGameView);
                // El motor es creado con la actividad y la vista
                gameEngine = new GameEngine(activity, surfaceGameView);
                // El gestor de controles es vinculado al motor
                gameEngine.setInputController(new TouchInputController(gameEngine, createdView));
                // Arranca el juego
                gameEngine.startGame();
            }
        });
        return createdView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Es inicializada la clase que centraliza la gestión de los recursos externos
        ResourceLoader.initializeResourceLoader(this);

        // setContentView(R.layout.activity_fullscreen);

        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;

        // oldStart();




        // Set up the user interaction to manually show or hide the system UI.


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    private void oldStart() {
        // Es obtenida la instancia de activity, y su conversión al tipo del proyecto
        // Controlador principal de la impresión de los gráficos
        SurfaceGameView surfaceGameView = this.findViewById(R.id.simpleGameView);
        // El motor es creado con la actividad y la vista
        gameEngine = new GameEngine(this, surfaceGameView);
        // El gestor de controles es vinculado al motor
        gameEngine.setInputController(new TouchInputController(gameEngine, this.mContentView));
        // Arranca el juego
        gameEngine.startGame();
    }



/*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Arranca el motor de juego a través de un listener cuando la vista ya haya sido creada
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Tan pronto como se acceda al código del listener, se borra la escucha
                observer.removeOnGlobalLayoutListener(this);
                // Es obtenida la instancia de activity, y su conversión al tipo del proyecto
                GameActivity gameActivity = (GameActivity) getActivity();
                // Controlador principal de la impresión de los gráficos
                SurfaceGameView surfaceGameView = getView().findViewById(R.id.simpleGameView);
                // El motor es creado con la actividad y la vista
                // gameEngine = new GameEngine(gameActivity, surfaceGameView);
                // El gestor de controles es vinculado al motor
                gameEngine.setInputController(new TouchInputController(gameEngine, getView()));
                // Arranca el juego
                gameEngine.startGame();
            }
        });
    }
*/


    public void setDebugHelper(DebugHelper helper) {
        this.debugHelper = helper;
    }

    @Override
    public void onBackPressed() {
        final GameBaseFragment fragment = (GameBaseFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void navigateBack() {
        // Do a push on the navigation history
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        char charToDisplay = event.getKeyCharacterMap().getDisplayLabel(keyCode);
        // Si se pulsa una tecla especial
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            this.debugHelper.deleteLastCharacter();
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_SPACE) {
            this.debugHelper.addCharacterToTerminal(' ');
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            this.debugHelper.executeCommand();
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            this.debugHelper.showPreviousCommand();
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            this.debugHelper.showNextCommand();
            // En caso contrario, comprobamos si conocemos el carácter
        } else if (charToDisplay != '\u0000') {
            // Si se trata de un carácter alfabético
            if (charToDisplay >= 'A' && charToDisplay <= 'Z') {
                // Si el usuario no pulsa una combinación para letras mayúsculas, reduce a minúsculas
                if (event.isShiftPressed() || event.isShiftPressed() && event.isCapsLockOn()) {
                    this.debugHelper.addCharacterToTerminal(charToDisplay);
                } else {
                    this.debugHelper.addCharacterToTerminal(Character.toLowerCase(charToDisplay));
                }
                // Si se trata de cualquier otro carácter sí reconocido
            } else {
                this.debugHelper.addCharacterToTerminal(charToDisplay);
            }
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
    }

    private void show() {
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}