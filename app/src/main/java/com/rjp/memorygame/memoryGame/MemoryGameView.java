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
        linePaint.setStrokeWidth(4);
        linePaint.setColor(Color.parseColor("#f4f5f6"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        cellWith = width / count;
        setMeasuredDimension(width, width);
    }

    public void start(int count) {
        this.count = count;
        invalidate();
        results = new ArrayList<>();
        for (int i = 0; i < count + 3; i++) {
            int x = (int) (Math.random() * (count - 1));
            int y = (int) (Math.random() * (count - 1));
            boolean isExist = false;
            for (Cell result : results) {
                if(result.getX() == x && result.getY() == y){
                    isExist = true;
                }
            }
            if(!isExist) {
                results.add(new Cell(x, y));
            }
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
                    canvas.drawBitmap(getBitmapRandom(result.getType()), null, new RectF(left, top, left + cellWith, top + cellWith), linePaint);
                }
            }
        }
    }

    private Bitmap getBitmapRandom(int index) {
        switch (index) {
            case 1:
                return BitmapFactory.decodeResource(getResources(), R.drawable.vege_cabbage);
            case 2:
                return BitmapFactory.decodeResource(getResources(), R.drawable.vege_eggplant);
            case 3:
                return BitmapFactory.decodeResource(getResources(), R.drawable.vege_pepper);
            case 4:
                return BitmapFactory.decodeResource(getResources(), R.drawable.vege_pumpkin);
            case 5:
                return BitmapFactory.decodeResource(getResources(), R.drawable.vege_tomato);
        }
        return BitmapFactory.decodeResource(getResources(), R.drawable.vege_onion);
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
                    if (result.getX() == indexX && result.getY() == indexY) {
                        result.setHidden(false);
                        invalidate();
                        isOver = false;
                    }
                }
                if (isOver && onMemoryGameListener != null) {
                    onMemoryGameListener.gameOver();
                } else {
                    isOver = true;
                    for (Cell result : results) {
                        if (result.isHidden()) {
                            isOver = false;
                        }
                    }
                    if (isOver && onMemoryGameListener != null) {
                        onMemoryGameListener.gameSuccess(getLevelByTime(System.currentTimeMillis() - startTime));
                    }
                }
                break;
        }
        return true;
    }

    private String getLevelByTime(long time) {
        if (time > 30 * 1000) {
            return "f";
        } else if (time > 20 * 1000) {
            return "e";
        } else if (time > 10 * 1000) {
            return "d";
        } else if (time > 8 * 1000) {
            return "c";
        } else if (time > 5 * 1000) {
            return "b";
        } else if (time > 3 * 1000) {
            return "a";
        } else if (time > 1000) {
            return "s";
        }
        return "sss";
    }

    public void setOnMemoryGameListener(OnMemoryGameListener onMemoryGameListener) {
        this.onMemoryGameListener = onMemoryGameListener;
    }
}
