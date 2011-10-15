package com.djoker.DEngine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class DEngine extends Canvas {
    private static final long serialVersionUID = 7526472295622771478L;
    private BufferStrategy strategy;
    private boolean gameRunning = true;
    private int fps = 30;
    public int currentFPS = 0;
    public int FPS = 0;
    private double start;
    private Graphics2D buffer = null;
    public boolean MouseClik = false;
    public int touchx, touchy;
    public int drawcount = 0;

    final static int VIDEOY = 238;
    final static int VIDEOX = 400;

    public DEngine() {
        JFrame container = new JFrame("Android Emulator - Djoker Soft - Luis Santos");
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(VIDEOX, VIDEOY));
        panel.setLayout(null);
        setBounds(0, 0, VIDEOX, VIDEOY);
        panel.add(this);
        setIgnoreRepaint(true);
        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(new KeyInputHandler());
        addMouseListener(new MouseInputHandler());
        requestFocus();
        createBufferStrategy(2);
        strategy = getBufferStrategy();

    }

    // ****************************************************************************
    public boolean isOnScreen(int x, int y, int w, int h) {
        boolean onScreen = true;

        if ((x) <= VIDEOX && x + w >= 0 && y <= VIDEOY && y + h >= 0) {
            onScreen = true;
        } else {
            onScreen = false;
        }

        return onScreen;
    }

    public DImage loadImage(String ref) {
        DImage image = new DImage(ref);
        return image;
    }

    public void drawImage(DImage buff, int x, int y) {
        if (isOnScreen(x, y, buff.width, buff.height)) {
            buffer.drawImage(buff.getBuffer(), x, y, null);
        }
    }

    public void drawImage(DImage buff, int x, int y, int srcx, int srcy, int srcw, int srch) {
        buffer.drawImage(buff.getBuffer(), x, y, x + (srcw - srcx), y + (srch - srcy), srcx, srcy, srcw, srch, null);
    }

    public void drawImageScale(DImage buff, int x, int y, int w, int h, int srcx, int srcy, int srcw, int srch) {
        buffer.drawImage(buff.getBuffer(), x, y, x + w, y + h, srcx, srcy, srcw, srch, null);
    }

    public void drawImage(DImage buff, int x, int y, int w, int h, int srcx, int srcy, int srcw, int srch) {
        buffer.drawImage(buff.getBuffer(), x, y, w, h, srcx, srcy, srcw, srch, null);
    }

    // PRIMITIVES

    public void setColor(DColor col) {

        col.impl = new Color(col.r, col.g, col.b);
        buffer.setColor((Color) col.impl);

    }

    public void drawString(int x, int y, String txt) {
        buffer.drawString(txt, x, y);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        buffer.drawLine(x1, y1, x2, y2);

    }

    public void drawRect(int x, int y, int w, int h) {
        if (!isOnScreen(x, y, w, h))
            return;

        buffer.drawRect(x, y, w, h);

    }

    public void drawFillRect(int x, int y, int w, int h) {
        if (isOnScreen(x, y, w, h)) {
            buffer.fillRect(x, y, w, h);
            drawcount += 1;
        }

    }

    public void drawOval(int x, int y, int r) {
        if (!isOnScreen(x, y, r, r))
            return;
        buffer.drawOval(x - (r / 2), y - (r / 2), r, r);
    }

    public void drawFillOval(int x, int y, int r) {
        if (!isOnScreen(x, y, r, r))
            return;
        buffer.fillOval(x - (r / 2), y - (r / 2), r, r);
    }

    public boolean GetTouch() {
        return MouseClik;
    }

    public int TouchX() {
        return touchx;
    }

    public int TouchY() {
        return touchy;
    }

    public int VideoX() {
        return VIDEOX;
    }

    public int VideoY() {
        return VIDEOY;
    }

    // ***************************************************************************

    public abstract void LoadGame();

    public abstract void OnUpdate(double time, double movetime);

    public abstract void OnDraw();

    public void Render(Graphics2D g) {
        buffer = g;
        OnDraw();
        g.setColor(Color.red);
        g.drawString("Fps:" + getFPS(), 20, 20);
    }

    public void tick() {
        currentFPS++;
        if (System.currentTimeMillis() - start >= 1000) {
            FPS = currentFPS;
            currentFPS = 0;
            start = System.currentTimeMillis();
        }
    }

    public int getFPS() {
        return FPS;
    }

    public void gameLoop() {
        LoadGame();
        long lastLoopTime = System.currentTimeMillis();
        while (gameRunning) {
            tick();
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            OnUpdate(lastLoopTime, delta);
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.blue);
            g.fillRect(0, 0, VIDEOX, VIDEOY);
            Render(g);
            g.dispose();
            strategy.show();
            try {
                Thread.sleep(1000 / fps);
            } catch (Exception e) {
            }
        }

    }

    private class MouseInputHandler extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            MouseClik = true;
            touchx = e.getX();
            touchy = e.getY();

        }

        public void mouseReleased(MouseEvent e) {
            MouseClik = false;
            touchx = e.getX();
            touchy = e.getY();

        }

    }

    private class KeyInputHandler extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }

}
