package com.rjp.memorygame.carGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rjp.memorygame.R;
import com.rjp.memorygame.utils.AppUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author : Gimpo create on 2018/11/22 16:47
 * email  : jimbo922@163.com
 */
public class RoadPanelView extends View implements Runnable{

    private Context mContext;
    private int width;
    private int height;
    private int count = 3;
    private int space = 10;
    private List<Car> cars = new CopyOnWriteArrayList<>();
    private int carWidth;
    private int carHeight;
    private int speed = 5;
    private Thread thread;
    private boolean isStop;
    private Bitmap bitmap;
    private Paint paint;
    private boolean isInit;

    public RoadPanelView(Context context) {
        this(context, null);
    }

    public RoadPanelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        space = AppUtil.dp2px(mContext, 20);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_open_six);

        thread = new Thread(this);

        paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        carWidth = (width - 6 * space) / 3;
        carHeight = carWidth * 3 / 2;

        setMeasuredDimension(width, height);

        if(!isInit){
            int index = (int)(Math.random() * 2);
            cars.add(new Car(carWidth * index + 2 * index * space + space, -carHeight));
            isInit = true;
        }
    }

    public void start(){

        thread.start();
    }

    public void stop(){
        isStop = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Car car : cars) {
            canvas.drawBitmap(bitmap, null, new RectF(car.getLeft(), car.getTop(), car.getLeft() + carWidth, car.getTop() + carHeight), paint);
        }
    }

    @Override
    public void run() {
        while (!isStop){
            int size = cars.size();
            for (int i = size - 1; i >= 0; i--) {
                Car car = cars.get(i);
                int top = car.getTop() + speed;
                car.setTop(top);
                if(top > height + carHeight){
                    cars.remove(car);
                }else if(top > height / 2){
                    int index = (int)(Math.random() * 2);
                    cars.add(new Car(carWidth * index + 2 * index * space + space, -carHeight));
                }
            }

            invalidate();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
