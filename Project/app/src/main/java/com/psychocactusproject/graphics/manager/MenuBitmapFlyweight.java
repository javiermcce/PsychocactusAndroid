package com.psychocactusproject.graphics.manager;

import android.graphics.Bitmap;

import com.psychocactusproject.R;

public class MenuBitmapFlyweight {

    public enum MenuType { CONTEXT_MENU_TYPE, DIALOG_MENU_TYPE, PAUSE_MENU_TYPE }
    private static MenuBitmapFlyweight.ContextMenuFlyweight contextMenuInstance;
    private static MenuBitmapFlyweight.DialogMenuFlyweight dialogMenuInstance;
    private static MenuBitmapFlyweight.PauseMenuFlyweight pauseMenuInstance;

    public static ContextMenuFlyweight getContextMenuInstance() {
        if (contextMenuInstance == null) {
            contextMenuInstance = new MenuBitmapFlyweight.ContextMenuFlyweight();
        }
        return contextMenuInstance;
    }

    public static DialogMenuFlyweight getDialogMenuInstance() {
        if (dialogMenuInstance == null) {
            dialogMenuInstance = new MenuBitmapFlyweight.DialogMenuFlyweight();
        }
        return dialogMenuInstance;
    }

    public static PauseMenuFlyweight getPauseMenuInstance() {
        if (pauseMenuInstance == null) {
            pauseMenuInstance = new MenuBitmapFlyweight.PauseMenuFlyweight();
        }
        return pauseMenuInstance;
    }

    public static class ContextMenuFlyweight {

        private final Bitmap bottomPiece;
        private final Bitmap bottomLeftPiece;
        private final Bitmap bottomRightPiece;
        private final Bitmap centerPiece;
        private final Bitmap leftPiece;
        private final Bitmap rightPiece;
        private final Bitmap topPiece;
        private final Bitmap topLeftPiece;
        private final Bitmap topRightPiece;

        private ContextMenuFlyweight() {
            this.bottomPiece = ResourceLoader.loadBitmap(R.drawable.context_bottom);
            this.bottomLeftPiece = ResourceLoader.loadBitmap(R.drawable.context_bottom_left);
            this.bottomRightPiece = ResourceLoader.loadBitmap(R.drawable.context_bottom_right);
            this.centerPiece = ResourceLoader.loadBitmap(R.drawable.context_center);
            this.leftPiece = ResourceLoader.loadBitmap(R.drawable.context_left);
            this.rightPiece = ResourceLoader.loadBitmap(R.drawable.context_right);
            this.topPiece = ResourceLoader.loadBitmap(R.drawable.context_top);
            this.topLeftPiece = ResourceLoader.loadBitmap(R.drawable.context_top_left);
            this.topRightPiece = ResourceLoader.loadBitmap(R.drawable.context_top_right);
        }

        public Bitmap getBottomPiece() {
            return this.bottomPiece;
        }

        public Bitmap getBottomLeftPiece() {
            return this.bottomLeftPiece;
        }

        public Bitmap getBottomRightPiece() {
            return this.bottomRightPiece;
        }

        public Bitmap getCenterPiece() {
            return this.centerPiece;
        }

        public Bitmap getLeftPiece() {
            return this.leftPiece;
        }

        public Bitmap getRightPiece() {
            return this.rightPiece;
        }

        public Bitmap getTopPiece() {
            return this.topPiece;
        }

        public Bitmap getTopLeftPiece() {
            return this.topLeftPiece;
        }

        public Bitmap getTopRightPiece() {
            return this.topRightPiece;
        }
    }

    public static class DialogMenuFlyweight {

        private final Bitmap bottomPiece;
        private final Bitmap bottomLeftPiece;
        private final Bitmap bottomRightPiece;
        private final Bitmap centerPiece;
        private final Bitmap leftPiece;
        private final Bitmap rightPiece;
        private final Bitmap topPiece;
        private final Bitmap topLeftPiece;
        private final Bitmap topRightPiece;

        private DialogMenuFlyweight() {
            this.bottomPiece = ResourceLoader.loadBitmap(R.drawable.dialog_bottom);
            this.bottomLeftPiece = ResourceLoader.loadBitmap(R.drawable.dialog_bottom_left);
            this.bottomRightPiece = ResourceLoader.loadBitmap(R.drawable.dialog_bottom_right);
            this.centerPiece = ResourceLoader.loadBitmap(R.drawable.dialog_center);
            this.leftPiece = ResourceLoader.loadBitmap(R.drawable.dialog_left);
            this.rightPiece = ResourceLoader.loadBitmap(R.drawable.dialog_right);
            this.topPiece = ResourceLoader.loadBitmap(R.drawable.dialog_top);
            this.topLeftPiece = ResourceLoader.loadBitmap(R.drawable.dialog_top_left);
            this.topRightPiece = ResourceLoader.loadBitmap(R.drawable.dialog_top_right);
        }

        public Bitmap getBottomPiece() {
            return this.bottomPiece;
        }

        public Bitmap getBottomLeftPiece() {
            return this.bottomLeftPiece;
        }

        public Bitmap getBottomRightPiece() {
            return this.bottomRightPiece;
        }

        public Bitmap getCenterPiece() {
            return this.centerPiece;
        }

        public Bitmap getLeftPiece() {
            return this.leftPiece;
        }

        public Bitmap getRightPiece() {
            return this.rightPiece;
        }

        public Bitmap getTopPiece() {
            return this.topPiece;
        }

        public Bitmap getTopLeftPiece() {
            return this.topLeftPiece;
        }

        public Bitmap getTopRightPiece() {
            return this.topRightPiece;
        }
    }


    public static class PauseMenuFlyweight {

        private final Bitmap verticalBarPiece;
        private final Bitmap horizontalBarPiece;
        private final Bitmap[] faces;

        private PauseMenuFlyweight() {
            this.horizontalBarPiece = ResourceLoader.loadBitmap(R.drawable.pause_horizontal_frame);
            this.verticalBarPiece = ResourceLoader.loadBitmap(R.drawable.pause_vertical_frame_2);
            this.faces = new Bitmap[5];
            this.faces[0] = ResourceLoader.loadBitmap(R.drawable.face_bass_complete);
            this.faces[1] = ResourceLoader.loadBitmap(R.drawable.face_guitar_complete);
            this.faces[2] = ResourceLoader.loadBitmap(R.drawable.face_singer_complete);
            this.faces[3] = ResourceLoader.loadBitmap(R.drawable.face_drums_complete);
            this.faces[4] = ResourceLoader.loadBitmap(R.drawable.face_barry_complete);
        }

        public Bitmap getHorizontalBarPiece() {
            return this.horizontalBarPiece;
        }

        public Bitmap getVerticalBarPiece() {
            return this.verticalBarPiece;
        }

        public Bitmap getRandomFace(int randomIndex) {
            return this.faces[randomIndex];
        }
    }
}
