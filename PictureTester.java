import java.awt.Color;
import java.io.File;
import java.awt.*;
import java.awt.Graphics;
import javax.swing.*;
import java.awt.image.*;

/**
 * This class contains class (static) methods that will help you test the
 * Picture class methods. Uncomment the methods and the code in the main to
 * test. This is a great lesson for learning about 2D arrays and the Color
 * class.
 * 
 * @author Barbara Ericson and altered by Richard Hanson
 */
public class PictureTester {
    private static JPanel panel;
    public static final String ROOT_PICS_INPUT = "/home/runner/Picture-Lab/images/";
    public static final String ROOT_PICS_OUTPUT = "/home/runner/Picture-Lab/processed/";

    /**
     * Main method for testing. Every class can have a main method in Java
     */
    public static void main(String[] args) {
        // original();
        testFill();

    }

    public static void original() {
        Picture swan = Picture.getInstance("flower.jpeg");
        swan.explore();
    }

    public static void testFill() {
        Picture swan = Picture.getInstance("butt.jpg");
        swan.explore();
    }

    
    /** Method to test edgeDetection */
    public static void testEdgeDetection() {
        
        Picture swan = Picture.getInstance("flower.jpeg");

        // written in Picture class
        swan.edgeDetection(225);

        swan.write("outside outline.png");// writes the new picture to a new file
    }

    /**
     * Checks to see if row and col are within the Picture pic
     * 
     * @param pic Picture we are checking
     * @param row Row in pic
     * @param col Col in pic
     * @return true if row and col are valid for pic, false otherwise
     */
    public boolean inbounds(Picture pic, int row, int col) {
        return false;
    }

}