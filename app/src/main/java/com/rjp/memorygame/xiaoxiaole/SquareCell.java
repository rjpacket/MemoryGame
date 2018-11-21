package com.rjp.memorygame.xiaoxiaole;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

/**
 * author : Gimpo create on 2018/11/19 17:07
 * email  : jimbo922@163.com
 */
public class SquareCell {
    private int left;
    private int top;

    private int type;

    public SquareCell(int left, int top){
        this.left = left;
        this.top = top;
        this.type = (int) (Math.random() * 5);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public void animTo(int endLeft, int endTop){
        if(endLeft == left){
            //向下的动画
            ValueAnimator animator = ObjectAnimator.ofInt(top, endTop);
            animator.setDuration(Math.abs(endTop - top) * 10);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (Integer) animation.getAnimatedValue();
                    setTop(animatedValue);
                }
            });
            animator.start();
        }else if(endTop == top){
            //左右移动的动画
            ValueAnimator animator = ObjectAnimator.ofInt(left, endLeft);
            animator.setDuration(Math.abs(endLeft - left) * 10);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (Integer) animation.getAnimatedValue();
                    setLeft(animatedValue);
                }
            });
            animator.start();
        }
    }
}
