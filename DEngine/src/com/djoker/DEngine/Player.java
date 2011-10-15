package com.djoker.DEngine;

public class Player {

    public float gravity = 20.0f;
    public float xspeed;
    public final Circle bounds;
    int scroll = 0;
    float y = 0, x = 0;
    boolean left = false, right = false;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.bounds = new Circle(x, y, 8);

    }

    public void Colide(int y) {
        gravity = -12;
        // this.y=y;
    }

    public void move() {

        if (right & xspeed < 8)
            xspeed += 2;

        if (left & xspeed > -8)
            xspeed -= 2;

        if (xspeed > 0) {
            xspeed = xspeed - 1;
        }
        if (xspeed < 0) {
            xspeed = xspeed + 1;
        }

        y = y + gravity;
        x = x + xspeed;

        gravity += 1;

        if (y > 320) {
            x = 100;
            y = 100;
            gravity = 10;
        }

    }

    public void draw(Engine engine)

    {
        engine.setColor(DColor.white);
        engine.drawOval(((int) x) - scroll, (int) y, 12);

        engine.setColor(DColor.red);
        engine.drawFillOval(((int) x) - scroll, (int) y, 11);
        bounds.center.set(x - scroll, y + 10);
    }

}
