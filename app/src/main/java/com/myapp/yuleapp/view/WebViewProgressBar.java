package com.myapp.yuleapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.myapp.yuleapp.R;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/03.10:10
 * 描述:
 **/

public class WebViewProgressBar extends View {
    private int progress = 1;
    private final static int HEIGHT = 5;
    private Paint paint;
    public WebViewProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewProgressBar(Context context) {
        super(context);
        paint=new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(HEIGHT);
        paint.setAntiAlias(true);
        paint.setColor(context.getResources().getColor(R.color.primary_light));
    }
    public void setProgress(int progress){
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth() * progress / 100, HEIGHT, paint);
    }
}