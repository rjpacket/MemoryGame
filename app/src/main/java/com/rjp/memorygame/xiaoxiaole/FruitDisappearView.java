package com.rjp.memorygame.xiaoxiaole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.rjp.memorygame.R;
import com.rjp.memorygame.utils.AppUtil;

/**
 * author : Gimpo create on 2018/11/19 19:32
 * email  : jimbo922@163.com
 */
public class FruitDisappearView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private Context mContext;
    private Thread mDrawThread;
    private boolean drawFlag;
    private SurfaceHolder mHolder;
    private int cellWith;
    private int cellHeight;
    private int cols = 8;
    private int rows = 10;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;
    private Bitmap bitmap6;
    private Paint paint;
    private int space;
    private SquareCell[][] array;

    public FruitDisappearView(Context context) {
        this(context, null);
    }

    public FruitDisappearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);

        mDrawThread = new Thread(this);

        array = new SquareCell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = new SquareCell();
            }
        }

        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_one);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_two);
        bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_three);
        bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_four);
        bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_five);
        bitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_six);

        paint = new Paint();

        while (checkInit()){
            checkInit();
        }
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
        while (drawFlag) {
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

        int width = MeasureSpec.getSize(widthMeasureSpec);
        space = AppUtil.dp2px(mContext, 4);
        cellWith = cellHeight = (width - space * (cols - 1)) / cols;
        int height = cellHeight * rows + space * (rows - 1);
        setMeasuredDimension(width, height);
    }

    private void mainDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#FFDAE6"));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int left = (j % cols) * (cellWith + space);
                int top = (i % rows) * (cellHeight + space);
                Bitmap bitmapByIJ = getBitmapByIJ(array[i][j]);
                if (bitmapByIJ != null) {
                    canvas.drawBitmap(bitmapByIJ, null, new RectF(left, top, left + cellWith, top + cellHeight), paint);
                }
            }
        }
    }

    private Bitmap getBitmapByIJ(SquareCell squareCell) {
        if (squareCell == null) {
            return bitmap1;
        }
        switch (squareCell.getType()) {
            case 0:
                return bitmap1;
            case 1:
                return bitmap2;
            case 2:
                return bitmap3;
            case 3:
                return bitmap4;
            case 4:
                return bitmap5;
            case 5:
                return bitmap6;
        }
        return null;
    }

    private boolean checkInit() {
        //检查行
        for (int i = rows - 1; i >= 0; i--) {
            for (int j = 0; j < cols - 2; j++) {
                if (array[i][j].getType() == array[i][j + 1].getType() && array[i][j + 1].getType() == array[i][j + 2].getType()) {
                    array[i][j].setType(-1);
                    array[i][j + 1].setType(-1);
                    array[i][j + 2].setType(-1);

                    for (int k = i - 1; k >= 0; k--) {
                        array[k + 1][j] = array[k][j];
                        array[k + 1][j + 1] = array[k][j + 1];
                        array[k + 1][j + 2] = array[k][j + 2];
                    }
                    array[0][j] = new SquareCell();
                    array[0][j + 1] = new SquareCell();
                    array[0][j + 2] = new SquareCell();
                    return true;
                }
            }
        }
        //检查列
        for (int i = 0; i < cols - 1; i++) {
            for (int j = rows - 1; j >= 2; j--) {
                if (array[j][i].getType() == array[j - 1][i].getType() && array[j - 1][i].getType() == array[j - 2][i].getType()) {
                    array[j][i].setType(-1);
                    array[j - 1][i].setType(-1);
                    array[j - 2][i].setType(-1);

                    for (int k = j; k >= 3; k--) {
                        array[k][i] = array[k - 3][i];
                    }

                    array[2][i] = new SquareCell();
                    array[1][i] = new SquareCell();
                    array[0][i] = new SquareCell();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
