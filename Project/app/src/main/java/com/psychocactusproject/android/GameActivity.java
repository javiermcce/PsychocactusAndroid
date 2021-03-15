package com.psychocactusproject.android;

import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.psychocactusproject.R;

public class GameActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "content";
    private DebugHelper debugHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainMenuFragment(), TAG_FRAGMENT)
                    .commit();
        }
    }

    public void startGame() {
        // Navigate the the game fragment, which makes the start automatically
        navigateToFragment( new GameFragment());
    }

    private void navigateToFragment(GameBaseFragment dst) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, dst, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else {
                throw new IllegalStateException("Android Lollipop required to run this game");
            }
        }
    }

    public void setDebugHelper(DebugHelper helper) {
        this.debugHelper = helper;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        char charToDisplay = event.getKeyCharacterMap().getDisplayLabel(keyCode);
        if (charToDisplay >= 'A' && charToDisplay <= 'Z') {
            // int asciiValue = keyCode + 12;
            // Si el usuario no pulsa una combinación para letras mayúsculas, reduce a minúsculas
            if (event.isShiftPressed() || event.isShiftPressed() && event.isCapsLockOn()) {
                DebugHelper.printMessage("" + charToDisplay);
                this.debugHelper.sendToTerminal(charToDisplay);
            } else {
                // asciiValue += 20;
                DebugHelper.printMessage("" + Character.toLowerCase(charToDisplay));
                this.debugHelper.sendToTerminal(Character.toLowerCase(charToDisplay));
            }
            /*
            DebugHelper.printMessage("letra: " + event.getKeyCharacterMap().getDisplayLabel(keyCode)
                    + " , valor num: " + keyCode
                    + " , ascii value: " + asciiValue
                    + " , minúscula: " + Character.toLowerCase(charToDisplay));

             */
        }
        return false;
        // si entra la secuencia de caracteres "debug" entra en modo debug???? podría ser
    }
}