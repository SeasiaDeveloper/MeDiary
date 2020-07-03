package com.mediary.utils;

public class MyColor {
    public int color;
    public int progress = 30;
    public byte warmWhite;

    public MyColor() {
    }

    public MyColor(int color, byte warmWhite, int progress) {
        this.color = color;
        this.warmWhite = warmWhite;
        this.progress = progress;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public byte getWarmWhite() {
        return warmWhite;
    }

    public void setWarmWhite(byte warmWhite) {
        this.warmWhite = warmWhite;
    }

    @Override
    public String toString() {
        return "MyColor{" +
                "color=" + color +
                ", progress=" + progress +
                ", warmWhite=" + warmWhite +
                '}';
    }
}
