package com.psychocactusproject.logic.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.psychocactusproject.R;

import java.util.LinkedList;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static java.security.AccessController.getContext;

public class CanvasActivity extends Activity implements View.OnClickListener {

    LinkedList<Point> listaPuntos = new LinkedList<>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println(event.getX() + ", " + event.getY());
        return false;
    }

    @Override
    public void onClick(View v) {
        /*
        System.out.println("ASDAHSDASDJHASDJH");
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        System.out.println("Width: " + width + ", Height: " + height + ", reaLWidth: " + (width + getNavigationBarWidth()) + ", reaLHeight: " + (height + getNavigationBarHeight()));
        Point point = this.checkSize();
        System.out.println("Width: " + width + ", Height: " + height + ", reaLWidth: " + point.x);
        */
        System.out.println(v.getX() + ", " + v.getY());
        Point punto = new Point((int)v.getX(), (int)v.getY());
        if (!listaPuntos.contains(punto)) {
            listaPuntos.addFirst(punto);
        }
        if (listaPuntos.size() > 50) {
            listaPuntos.removeLast();
        }
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // Reference to Image View
        ImageView frame = (ImageView) findViewById(R.id.gameCanvas);
        frame.setOnClickListener(this);
        // Create bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        System.out.println("Width: " + width + ", Height: " + height);

        // Create canvas
        Canvas canvas = new Canvas(bitmap);

        // Paint object to draw on canvas
        Paint paint = new Paint();

        canvas.drawColor(Color.WHITE);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        for (Point pDibujo : listaPuntos) {
            canvas.drawPoint(pDibujo.x, pDibujo.y, paint);
            System.out.print(listaPuntos.toString());
        }
        System.out.println("");

        frame.setImageBitmap(bitmap);
    }

    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    private int getNavigationBarWidth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableWidth = metrics.widthPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realWidth = metrics.widthPixels;
            if (realWidth > usableWidth)
                return realWidth - usableWidth;
            else
                return 0;
        }
        return 0;
    }

    private Point checkSize() {
        int Measuredwidth = 0;
        int Measuredheight = 0;
        Point size = new Point();
        WindowManager w = getWindowManager();
        Point sizeCalculada;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        }else{
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }
        return new Point(Measuredwidth, Measuredheight);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas_test);
/*
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | SYSTEM_UI_FLAG_FULLSCREEN
        | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
*/

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        
        // Reference to Image View
        final ImageView frame = (ImageView) findViewById(R.id.gameCanvas);
        frame.setOnClickListener(this);
        //frame.setOnTouchListener((View.OnTouchListener) this);

        // Create bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        System.out.println("Width: " + width + ", Height: " + height);

        // Create canvas
        Canvas canvas = new Canvas(bitmap);

        // Paint object to draw on canvas
        Paint paint = new Paint();

        canvas.drawColor(Color.WHITE);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setTextSize(40);

        canvas.drawText("Score: 42 Loves: 3 Hi: 97", 10, 50, paint);
        canvas.drawLine(10, 70, 200, 70, paint);
        canvas.drawCircle(110, 180, 100, paint);
        canvas.drawPoint(10, 280, paint);
        canvas.drawText("Width: " + width + ", Height: " + height, 10, 340, paint);

        frame.setImageBitmap(bitmap);
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }


}
