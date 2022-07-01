import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
// resolves problem with java.awt.List and java.util.List

/**
 * A class that represents a picture. This class inherits from SimplePicture and
 * allows the student to add functionality to the Picture class.
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture {

    private int x;
    private int y;
    // Static variable reference of single_instance
    // of type Picture
    private static Picture single_instance = null;

    ///////////////////// constructors //////////////////////////////////

    /**
     * Constructor that takes no arguments
     */
    public Picture() {
        /*
         * not needed but use it to show students the implicit call to super() child
         * constructors always call a parent constructor
         */
        super();
    }

    /**
     * Constructor that takes a file name and creates the picture
     * 
     * @param fileName the name of the file to create the picture from
     */
    private Picture(String fileName) {
        // let the parent class handle this fileName
        super(fileName);
    }

    
    // if need multiple, try doing if filename = instance variable filename and then if not reassign single_instance
    public static Picture getInstance(String fileName) {
        if (single_instance == null)
            single_instance = new Picture(fileName);
        single_instance.makeBorder();
        return single_instance;
    }

    public static Picture getInstance() {
        return single_instance;
    }

    public static Picture setInstance(String filename) {
        single_instance = new Picture(filename);
        return single_instance;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Constructor that takes the width and height
     * 
     * @param height the height of the desired picture
     * @param width  the width of the desired picture
     */
    public Picture(int height, int width) {
        // let the parent class handle this width and height
        super(width, height);
    }

    /**
     * Constructor that takes a picture and creates a copy of that picture
     * 
     * @param copyPicture the picture to copy
     */
    public Picture(Picture copyPicture) {
        // let the parent class do the copy
        super(copyPicture);
    }

    /**
     * Constructor that takes a buffered image
     * 
     * @param image the buffered image to use
     */
    public Picture(BufferedImage image) {
        super(image);
    }

    ////////////////////// methods ///////////////////////////////////////

    /**
     * Method to return a string with information about this picture.
     * 
     * @return a string with information about the picture such as fileName, height
     *         and width.
     */
    public String toString() {
        String output = "Picture, filename " + getFileName() + " height " + getHeight() + " width " + getWidth();
        return output;

    }

    private void makeBorder() {
        Pixel[][] pixels = single_instance.getPixels2D();
        int height = pixels.length;
        int width = pixels[0].length;
        for (int i = 0; i < width; i++) {
            pixels[0][i].setColor(Color.BLACK);
        }
        for (int x = 0; x < height; x++) {
            pixels[x][0].setColor(Color.BLACK);
        }
        for (int i = 0; i < width; i++) {
            pixels[height - 1][i].setColor(Color.BLACK);
        }
        for (int x = 0; x < height; x++) {
            pixels[x][width - 1].setColor(Color.BLACK);
        }
    }

    /**
     * converts a color image into grayscale. There are many algorithms for this.
     * The most common is to find the mean of the red, green and blue components and
     * set each component to that average
     */
    public void grayScale() {

        Pixel[][] pixels = this.getPixels2D();
        for (Pixel[] rowArray : pixels) {
            for (Pixel pixelObj : rowArray) {
                int sum = ((pixelObj.getBlue() + pixelObj.getRed() + pixelObj.getGreen()) / 3);
                // int mean= (sum/3)
                pixelObj.setBlue(sum);
                pixelObj.setRed(sum);
                pixelObj.setGreen(sum);
            }
        }

    }

    /**
     * copy from the passed fromPic to the specified startRow and startCol in the
     * current picture
     * 
     * @param fromPic  the picture to copy from
     * @param startRow the start row to copy to
     * @param startCol the start col to copy to
     */
    public void copy(Picture fromPic, int startRow, int startCol) {
        Pixel fromPixel = null;
        Pixel toPixel = null;
        Pixel[][] toPixels = this.getPixels2D();
        Pixel[][] fromPixels = fromPic.getPixels2D();
        for (int fromRow = 0, toRow = startRow; fromRow < fromPixels.length
                && toRow < toPixels.length; fromRow++, toRow++) {
            for (int fromCol = 0, toCol = startCol; fromCol < fromPixels[0].length
                    && toCol < toPixels[0].length; fromCol++, toCol++) {
                fromPixel = fromPixels[fromRow][fromCol];
                toPixel = toPixels[toRow][toCol];
                toPixel.setColor(fromPixel.getColor());
            }
        }
    }

    /** Method to create a collage of several pictures */
    public void createCollage() {
        Picture flower1 = new Picture("flower1.jpg");
        Picture flower2 = new Picture("flower2.jpg");
        this.copy(flower1, 0, 0);
        this.copy(flower2, 100, 0);
        this.copy(flower1, 200, 0);
        Picture flowerNoBlue = new Picture(flower2);

        this.copy(flowerNoBlue, 300, 0);
        this.copy(flower1, 400, 0);
        this.copy(flower2, 500, 0);
        this.write("collage.jpg");
    }

    /**
     * Method to show large changes in color This method traverses this picture and
     * changes to pixels to black and white, depending on the color to each pixel's
     * right. If the color change is large, then the pixel on the left is set to
     * black, otherwise, if the color is close, then the pixel is set to white. The
     * result is an image with black pixels where it detected a significant change
     * in color.
     * 
     * @param edgeDist the distance for finding edges
     */

    // comments: // gets the 2D array of Pixel
    // Big hint, the Pixel class has a method called colorDistance(Color) which
    // returns the distance the input Color is from this Pixel's Color
    // c<pixels[0].length bc that willl j take column 0's length
    // this pixel will always be the one to
    // the left of rightPixel. If this Pixel
    // is far enough away (based on edgeDist), then
    // leftPixel is set to Color black, else, white
    //// this Pixel doesn't change value, it is just
    // used as a reference for comparing with leftPixel

    public void edgeDetection(int edgeDist) {
        Pixel leftPixel;

        Pixel rightPixel;
        // System.out.println(this.getImage());
        Pixel[][] pixels = this.getPixels2D();
        // System.out.println(pixels);
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c + 1 < pixels[0].length; c++) {
                leftPixel = pixels[r][c];
                rightPixel = pixels[r][c + 1];
                if (leftPixel.colorDistance(rightPixel.getColor()) > edgeDist) {
                    leftPixel.setBlue(0);
                    leftPixel.setRed(200);
                    leftPixel.setGreen(25); // sets to black
                } else {
                    leftPixel.setBlue(225);
                    leftPixel.setRed(225);
                    leftPixel.setGreen(225);
                }
            }

        }

    }

    public void clickedAt(int x, int y) {
        this.x = x;
        this.y = y;
        System.out.println("x: " + getX() + ", y: " + getY());
    }

    public void fill(BufferedImage i, int x1, int y1, Color c, Color c1) {
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

    // public void fillSection(Pixel pixel, Color color){

    // Pixel leftPixel;
    // Pixel rightPixel;
    // Pixel[][] pixels = this.getPixels2D();
    // //need to go up
    // for (int r = 0; r < pixels.length; r++){
    // for (int c = 0; c + 1 < pixels[0].length; c++){
    // leftPixel = pixels[r][c];
    // rightPixel = pixels[r][c + 1];
    // if(leftPixel.getRed() == 225 && leftPixel.getGreen() == 225 &&
    // leftPixel.getBlue() == 225){
    // leftPixel.setBlue(color.getBlue());
    // leftPixel.setRed(color.getRed());
    // leftPixel.setGreen(color.getGreen());
    // //sets to black
    // }
    // else{
    // //quit the loop
    // }
    // }

    // }

    // }

    /*
     * Main method for testing - each class in Java can have a main method
     */
    public static void main(String[] args) {
        PictureTester.main(args);
        // Picture beach = new Picture("flower.jpeg");
        // beach.explore();
        // Picture pic = new Picture();
    }

} // this } is the end of class Picture, put all new methods before this
