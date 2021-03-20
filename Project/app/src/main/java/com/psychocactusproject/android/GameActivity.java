package com.psychocactusproject.android;

import android.os.Build;
import android.os.Bundle;
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
        // Si se pulsa una tecla especial
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            this.debugHelper.deleteLastCharacter();
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_SPACE) {
            this.debugHelper.addCharacterToTerminal(' ');
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            this.debugHelper.executeCommand();
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
}