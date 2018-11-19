package com.rjp.memorygame.xiaoxiaole;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * author : Gimpo create on 2018/11/19 19:32
 * email  : jimbo922@163.com
 */
public class FruitDisappearView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread mDrawThread;
    private boolean drawFlag;
    private SurfaceHolder mHolder;

    public FruitDisappearView(Context context) {
        this(context, null);
    }

    public FruitDisappearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);

        mDrawThread = new Thread(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawFlag = true;
        mDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawFlag = false;
    }

    @Override
    public void run() {
        while (drawFlag){
            long start = System.currentTimeMillis();
            Canvas canvas = mHolder.lockCanvas();
            mainDraw(canvas);
            mHolder.unlockCanvasAndPost(canvas);
            long end = System.currentTimeMillis();

            try {
                Thread.sleep(60 - (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    private void mainDraw(Canvas canvas) {

    }
}
