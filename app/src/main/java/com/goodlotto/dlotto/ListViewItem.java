package com.goodlotto.dlotto;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable[] mDrawBalls = new Drawable[6];
    private String[] mBalls = new String[6];
    private String mResult;

    public void setIconBall1(Drawable icon) {
        this.mDrawBalls[0] = icon;
    }
    public void setIconBall2(Drawable icon) {
        this.mDrawBalls[1] = icon;
    }
    public void setIconBall3(Drawable icon) {
        this.mDrawBalls[2] = icon;
    }
    public void setIconBall4(Drawable icon) {
        this.mDrawBalls[3] = icon;
    }
    public void setIconBall5(Drawable icon) {
        this.mDrawBalls[4] = icon;
    }
    public void setIconBall6(Drawable icon) {
        this.mDrawBalls[5] = icon;
    }

    public void setTextBall1(String text) {
        this.mBalls[0] = text;
    }
    public void setTextBall2(String text) {
        this.mBalls[1] = text;
    }
    public void setTextBall3(String text) {
        this.mBalls[2] = text;
    }
    public void setTextBall4(String text) {
        this.mBalls[3] = text;
    }
    public void setTextBall5(String text) {
        this.mBalls[4] = text;
    }
    public void setTextBall6(String text) {
        this.mBalls[5] = text;
    }

    public void setTextResult(String text) {
        this.mResult = text;
    }

    public Drawable getIconBall1() {
        return this.mDrawBalls[0];
    }
    public Drawable getIconBall2() {
        return this.mDrawBalls[1];
    }
    public Drawable getIconBall3() {
        return this.mDrawBalls[2];
    }
    public Drawable getIconBall4() {
        return this.mDrawBalls[3];
    }
    public Drawable getIconBall5() {
        return this.mDrawBalls[4];
    }
    public Drawable getIconBall6() {
        return this.mDrawBalls[5];
    }

    public String[] getTextBalls() {
        return this.mBalls;
    }
    public String getTextBall1() { return this.mBalls[0]; }
    public String getTextBall2() {
        return this.mBalls[1];
    }
    public String getTextBall3() {
        return this.mBalls[2];
    }
    public String getTextBall4() {
        return this.mBalls[3];
    }
    public String getTextBall5() {
        return this.mBalls[4];
    }
    public String getTextBall6() {
        return this.mBalls[5];
    }

    public String getTextResult() {
        return this.mResult;
    }
}
