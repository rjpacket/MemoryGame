package com.rjp.memorygame.xiaoxiaole;

import android.graphics.Bitmap;

/**
 * author : Gimpo create on 2018/11/19 17:07
 * email  : jimbo922@163.com
 */
public class SquareCell {
    private int x;
    private int y;
    private Bitmap bitmap;


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
