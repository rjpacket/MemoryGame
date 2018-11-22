package com.rjp.memorygame.memoryGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rjp.memorygame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Gimpo create on 2018/11/21 16:11
 * email  : jimbo922@163.com
 */
public class MemoryGameView extends View {

    private Context mContext;
    private int count = 5;
    private int width;
    private Paint linePaint;
    private int cellWith;
    private List<Cell> results;
    private Bitmap bitmap;
    private boolean isRealRunning;
    private OnMemoryGameListener onMemoryGameListener;
    private long startTime;

    public MemoryGameView(Context context) {
        this(context, null);
    }

    public MemoryGameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vege_onion);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        cellWith = width / count;
        setMeasuredDimension(width, width);
    }

    public void start() {
        results = new ArrayList<>();
        for (int i = 0; i < count + 3; i++) {
            int x = (int) (Math.random() * 4);
            int y = (int) (Math.random() * 4);
            results.add(new Cell(x, y));
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRealRunning = true;

                for (Cell result : results) {
                    result.setHidden(true);
                }

                invalidate();

                startTime = System.currentTimeMillis();
            }
        }, 3000);
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        for (int i = 0; i <= count; i++) {
            int height = i * cellWith;
            canvas.drawLine(0, height, width, height, linePaint);
            canvas.drawLine(height, 0, height, width, linePaint);
        }

        if (results != null) {
            for (Cell result : results) {
                if (!result.isHidden()) {
                    int left = result.getX() * cellWith;
                    int top = result.getY() * cellWith;
                    canvas.drawBitmap(bitmap, null, new RectF(left, top, left + cellWith, top + cellWith), linePaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isRealRunning) {
            return true;
        }
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                int indexX = (int) x / cellWith;
                int indexY = (int) y / cellWith;
                boolean isOver = true;
                for (Cell result : results) {
                    if(result.getX() == indexX && result.getY() == indexY){
                        result.setHidden(false);
                        invalidate();
                        isOver = false;
                    }
                }
                if(isOver && onMemoryGameListener != null){
                    onMemoryGameListener.gameOver();
                }else{
                    isOver = true;
                    for (Cell result : results) {
                        if(result.isHidden()){
                            isOver = false;
                        }
                    }
                    if(isOver && onMemoryGameListener != null){
                        onMemoryGameListener.gameSuccess(getLevelByTime(System.currentTimeMillis() - startTime));
                    }
                }
                break;
        }
        return true;
    }

    private String getLevelByTime(long time) {
        if(time > 30 * 1000){
            return "F";
        }else if(time > 20 * 1000){
            return "E";
        }else if(time > 10 * 1000){
            return "D";
        }else if(time > 8 * 1000){
            return "C";
        }else if(time > 5 * 1000){
            return "B";
        }else if(time > 3 * 1000){
            return "A";
        }else if(time > 1000){
            return "S";
        }
        return "SSS";
    }

    public void setOnMemoryGameListener(OnMemoryGameListener onMemoryGameListener) {
        this.onMemoryGameListener = onMemoryGameListener;
    }
}
