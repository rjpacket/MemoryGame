package com.rjp.memorygame.xiaoxiaole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
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
    private Point startPoint;
    private Point endPoint;

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

        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_one);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_two);
        bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_three);
        bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_four);
        bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_five);
        bitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_six);

        paint = new Paint();
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
            } catch (Exception e) {
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

        array = new SquareCell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int left = j  * (cellWith + space);
                int top = i * (cellHeight + space);
                array[i][j] = new SquareCell(left, top);
            }
        }
        while (checkInit()) {

        }
    }

    private void mainDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#FFDAE6"));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Bitmap bitmapByIJ = getBitmapByIJ(array[i][j]);
                if (bitmapByIJ != null) {
                    canvas.drawBitmap(bitmapByIJ, null, new RectF(array[i][j].getLeft(), array[i][j].getTop(), array[i][j].getLeft() + cellWith, array[i][j].getTop() + cellHeight), paint);
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
                        array[k][j].animTo(j * (cellWith + space), (k + 1) * (cellHeight + space));
                        array[k + 1][j] = array[k][j];
                        array[k][j + 1].animTo((j + 1) * (cellWith + space), (k + 1) * (cellHeight + space));
                        array[k + 1][j + 1] = array[k][j + 1];
                        array[k][j + 2].animTo((j + 2) * (cellWith + space), (k + 1) * (cellHeight + space));
                        array[k + 1][j + 2] = array[k][j + 2];
                    }
                    array[0][j] = new SquareCell(j * (cellWith + space), -(cellHeight + space));
                    array[0][j].animTo(j * (cellWith + space), 0);
                    array[0][j + 1] = new SquareCell((j + 1) * (cellWith + space), -(cellHeight + space));
                    array[0][j + 1].animTo((j + 1) * (cellWith + space), 0);
                    array[0][j + 2] = new SquareCell((j + 2) * (cellWith + space), -(cellHeight + space));
                    array[0][j + 2].animTo((j + 2) * (cellWith + space), 0);
                    return true;
                }
            }
        }
        //检查列
//        for (int i = 0; i < cols - 1; i++) {
//            for (int j = rows - 1; j >= 2; j--) {
//                if (array[j][i].getType() == array[j - 1][i].getType() && array[j - 1][i].getType() == array[j - 2][i].getType()) {
//                    array[j][i].setType(-1);
//                    array[j - 1][i].setType(-1);
//                    array[j - 2][i].setType(-1);
//
//                    for (int k = j; k >= 3; k--) {
//                        array[k][i] = array[k - 3][i];
//                    }
//
//                    array[2][i] = new SquareCell(i * (cellWith + space), 2 * (cellHeight + space));
//                    array[1][i] = new SquareCell(i * (cellWith + space), cellHeight + space);
//                    array[0][i] = new SquareCell(i * (cellWith + space), 0);
//                    return true;
//                }
//            }
//        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int startX = (int) x / (cellWith + space);
                int startY = (int) y / (cellHeight + space);
                int startLeft = startX * (cellWith + space);
                int startTop = startY * (cellHeight + space);
                RectF startRect = new RectF(startLeft, startTop, startLeft + cellWith, startTop + cellHeight);
                if (startRect.contains(x, y)) {
                    startPoint = new Point(startY, startX);
                }
                break;
            case MotionEvent.ACTION_UP:
                int endX = (int) x / (cellWith + space);
                int endY = (int) y / (cellHeight + space);
                int endLeft = endX * (cellWith + space);
                int endTop = endY * (cellHeight + space);
                RectF endRect = new RectF(endLeft, endTop, endLeft + cellWith, endTop + cellHeight);
                if (endRect.contains(x, y)) {
                    endPoint = new Point(endY, endX);
                }

                if (Math.abs(endPoint.x - startPoint.x) + Math.abs(endPoint.y - startPoint.y) == 1) {
                    //说明可以交换
                    SquareCell temp = array[startPoint.x][startPoint.y];
                    array[startPoint.x][startPoint.y] = array[endPoint.x][endPoint.y];
                    array[endPoint.x][endPoint.y] = temp;

                    while (checkInit()) {
                        checkInit();
                    }
//                    if(canDisappear()){
//
//                    }else{
//                        //没有找到，交换回来
//                        temp = array[startPoint.x][startPoint.y];
//                        array[startPoint.x][startPoint.y] = array[endPoint.x][endPoint.y];
//                        array[endPoint.x][endPoint.y] = temp;
//                    }
                }
                break;
        }
        return true;
    }


    private boolean canDisappear() {

        return false;
    }
}
