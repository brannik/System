package com.brannik.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CameraOverlayFrame extends View {
    Paint paint;
    Path path;
    GlobalVariables globe;

    public CameraOverlayFrame(Context context) {
        super(context);
        init();

    }

    public CameraOverlayFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraOverlayFrame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);



    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawColor(0);
        canvas.drawRect(
                getLeft()+(getRight()-getLeft())-200,
                getTop()+((getBottom()-getTop())/2)-40,
                getRight()-(getRight()-getLeft())+200,
                getBottom()-((getBottom()-getTop())/2)+60,paint);

        //drawRect(left, top, right, bottom, paint)

    }

}
