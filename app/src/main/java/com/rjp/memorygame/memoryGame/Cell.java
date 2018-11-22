package com.rjp.memorygame.memoryGame;

/**
 * author : Gimpo create on 2018/11/21 16:46
 * email  : jimbo922@163.com
 */
public class Cell {
    private int x;
    private int y;

    private int type;

    private boolean isHidden;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = (int) (Math.random() * 6);
    }

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

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
