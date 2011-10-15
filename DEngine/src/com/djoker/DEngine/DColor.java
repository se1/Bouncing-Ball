package com.djoker.DEngine;

public class DColor {
    public static final DColor black = new DColor(0, 0, 0);
    public static final DColor white = new DColor(255, 255, 255);
    public static final DColor yellow = new DColor(255, 255, 0);
    public static final DColor green = new DColor(0, 255, 0);
    public static final DColor cyan = new DColor(0, 255, 255);
    public static final DColor blue = new DColor(0, 0, 255);
    public static final DColor magenta = new DColor(255, 0, 255);
    public static final DColor red = new DColor(255, 0, 0);

    public static final DColor pink = new DColor(255, 140, 140);
    public static final DColor orange = new DColor(255, 140, 0);

    public static final DColor grey = new DColor(128, 128, 128);
    public static final DColor gray = new DColor(128, 128, 128);

    /** a value between 0 and 255 */
    public int r, g, b;
    /** a value between 0 and 255, default is 255 (opaque) */
    public int alpha = 255;

    public Object impl;

    public DColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public DColor(int r, int g, int b, int alpha) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
    }

    public DColor(double r, double g, double b) {
        this.r = (int) (r * 255.95);
        this.g = (int) (g * 255.95);
        this.b = (int) (b * 255.95);
    }

    public DColor(double r, double g, double b, double alpha) {
        this.r = (int) (r * 255.95);
        this.g = (int) (g * 255.95);
        this.b = (int) (b * 255.95);
        this.alpha = (int) (alpha * 255.95);
    }

}
