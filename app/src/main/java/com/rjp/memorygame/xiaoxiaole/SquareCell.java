package com.rjp.memorygame.xiaoxiaole;

/**
 * author : Gimpo create on 2018/11/19 17:07
 * email  : jimbo922@163.com
 */
public class SquareCell {
    private int type;

    public SquareCell(){
        this.type = (int) (Math.random() * 5);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
