package com.rjp.memorygame.carGame;

/**
 * author : Gimpo create on 2018/11/22 17:04
 * email  : jimbo922@163.com
 */
public class Car {
    private int left;
    private int top;
    private int type;

    public Car(int left, int top){
        this.left = left;
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
