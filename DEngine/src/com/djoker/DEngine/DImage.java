package com.djoker.DEngine;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class DImage {
    private Image image;
    public int width;
    public int height;

    public DImage(String ref) {
        image = getImage(ref);
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public Image getBuffer() {
        return image;
    }

    private Image getImage(String ref) {
        BufferedImage sourceImage = null;
        try {
            URL url = this.getClass().getClassLoader().getResource(ref);
            if (url == null) {
            }

            sourceImage = ImageIO.read(url);
        } catch (IOException e) {

        }

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image;
        image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);
        /*
         * switch (type) { case 0: image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
         * break; case 1: image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.OPAQUE); break; case
         * 2: image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.TRANSLUCENT); break; default:
         * image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK); break; }
         */

        image.getGraphics().drawImage(sourceImage, 0, 0, null);

        return image;
    }

}
