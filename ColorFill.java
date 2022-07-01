
// Java Program to implement floodfill algorithm
// in Java Applet(using queue)
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class ColorFill extends JFrame {

    public ColorFill() {
    }

    // public ColorFill(Graphics g){

    // }

    // paint function
    public void paint(Graphics g) {
        BufferedImage i = null;
        try {
            // Input the image to be used for FloodFill
            // The output is shown for 3 images
            // image1, image2 and image2
            i = ImageIO.read(new File("flower.jpeg"));

            // floodfill with color red at point 1, 1
            // get color of image at 1, 1
            // if 35, 35 point is floodfilled it will floodfill
            // the smaller area
            Color c = new Color(i.getRGB(1, 1));
            flood(i, g, 1, 1, c, Color.red);

            // draw the image after floodfill
            g.drawImage(i, 100, 100, this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        // draw the image after floodfill
        g.drawImage(i, 100, 100, this);
    }

    // function to floodfill the image using queue
    public void flood(BufferedImage i, Graphics g, int x1, int y1, Color c, Color c1) {
        // create a stack using array
        int stx[] = new int[100000];
        int sty[] = new int[100000], f, r, x, y;

        // create a front and rear
        f = r = 0;

        // initialize them
        stx[0] = x1;
        sty[0] = y1;

        // while front is greater than rear
        while (f >= r) {
            // pop element out
            x = stx[r];
            y = sty[r++];
            if (x >= 1 && y >= 1 && x < i.getWidth() && y < i.getHeight()) {
                // find the color at point x, y
                Color c2 = new Color(i.getRGB(x, y));

                // if there is no boundary (the color is almost
                // same as the color of the point where
                // floodfill is to be applied

                if (Math.abs(c2.getGreen() - c.getGreen()) < 30 && Math.abs(c2.getRed() - c.getRed()) < 30
                        && Math.abs(c2.getBlue() - c.getBlue()) < 30) {

                    // change the color of the pixel of image
                    i.setRGB(x, y, c1.getRGB());

                    g.drawImage(i, 100, 100, this);

                    // floodfill in all possible directions
                    // store them in queue
                    stx[f] = x;
                    sty[f++] = y + 1;
                    stx[f] = x;
                    sty[f++] = y - 1;
                    stx[f] = x + 1;
                    sty[f++] = y;
                    stx[f] = x - 1;
                    sty[f++] = y;
                }
            }
        }
    }
}