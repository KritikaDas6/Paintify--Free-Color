import java.awt.*;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.Toolkit;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.lang.reflect.Field;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.border.*;

/**
 * Displays a picture
 */

public class PictureExplorer implements MouseMotionListener, ActionListener, MouseListener {

    private static PictureExplorer single_instance = null;
    private Color c;
    private String text = "You have not selected a color yet!";
    //private Cursor mouse = new Cursor("click");
  //Cursor mouse = createCustomCursor(Image cursor, Point hotSpot, String name);
  //making the cursor

    Rectangle2D.Double selectedRect;
    Line2D selectedLine;
    Point2D startPoint, endPoint;
    boolean selectingRegion = true;
    boolean selectingLine = false;
    boolean firstClick = true;
    private Image image;
    private Cursor mouse;
    
    // current indicies
    /** row index */
    private int rowIndex = 0;
    /** column index */
    private int colIndex = 0;

    // main GUI
    /** window to hold GUI */
    private JFrame pictureFrame;
    /** window that allows the user to scroll to see a large picture */
    private JScrollPane scrollPane;

    // GUI components, potentially delete later
    /** text field to show column index */
    private JTextField colValue;
    /** text field to show row index */
    private JTextField rowValue;
    /** red value label */
    private JLabel rValue;
    /** green value label */
    private JLabel gValue;
    /** blue value label */
    private JLabel bValue;
    /** panel to show the color swatch */
    private JPanel colorPanel;

    // Buttons to choose color
    private JButton[] colorButtons;
    final private Color[] colors = {Color.red, Color.orange, Color.yellow, Color.green, Color.cyan, Color.blue, Color.magenta, Color.pink, Color.black, Color.gray, Color.white};

    // menu components
    /** menu bar */
    private JMenuBar menuBar;
    /** zoom menu */
    private JMenu zoomMenu;
    /** 25% zoom level */
    private JMenuItem twentyFive;
    /** 50% zoom level */
    private JMenuItem fifty;
    /** 75% zoom level */
    private JMenuItem seventyFive;
    /** 100% zoom level */
    private JMenuItem hundred;
    /** 150% zoom level */
    private JMenuItem hundredFifty;
    /** 200% zoom level */
    private JMenuItem twoHundred;
    /** 500% zoom level */
    private JMenuItem fiveHundred;

    /** The picture being explored */
    private DigitalPicture picture;

    /** The image icon used to display the picture */
    private ImageIcon scrollImageIcon;

    /** The image display */
    private ImageDisplay imageDisplay;

    /** the zoom factor (amount to zoom) */
    private double zoomFactor;

    /** the number system to use, 0 means starting at 0, 1 means starting at 1 */
    private int numberBase = 0;

    /**
     * Public constructor
     * 
     * @param picture the picture to explore
     */
    public PictureExplorer(DigitalPicture picture) {
        // set the fields
        this.picture = picture;
        zoomFactor = 1;

        // create the window and set things up
        createWindow();
    }

    public static PictureExplorer getInstance(DigitalPicture picture) {
        if (single_instance == null) {
            single_instance = new PictureExplorer(picture);

        }
        return single_instance;
    }

    /**
     * Changes the number system to start at one
     */
    public void changeToBaseOne() {
        numberBase = 1;
    }

    /**
     * Set the title of the frame
     * 
     * @param title the title to use in the JFrame
     */
    public void setTitle(String title) {
        pictureFrame.setTitle(title);
    }

    /**
     * Method to create and initialize the picture frame
     */
    private void createAndInitPictureFrame() {
        pictureFrame = new JFrame(); // create the JFrame
        pictureFrame.setResizable(true); // allow the user to resize it
        pictureFrame.getContentPane().setLayout(new BorderLayout()); // use border layout
        pictureFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // when close stop
        pictureFrame.setTitle("Paintify!");
    }

    /**
     * Method to create the menu bar, menus, and menu items
     */
    private void setUpMenuBar() {
        // create menu
        menuBar = new JMenuBar();

        zoomMenu = new JMenu("Zoom");
        twentyFive = new JMenuItem("25%");
        fifty = new JMenuItem("50%");
        seventyFive = new JMenuItem("75%");
        hundred = new JMenuItem("100%");
        hundred.setEnabled(false);
        hundredFifty = new JMenuItem("150%");
        twoHundred = new JMenuItem("200%");
        fiveHundred = new JMenuItem("500%");

        // add the action listeners
        twentyFive.addActionListener(this);
        fifty.addActionListener(this);
        seventyFive.addActionListener(this);
        hundred.addActionListener(this);
        hundredFifty.addActionListener(this);
        twoHundred.addActionListener(this);
        fiveHundred.addActionListener(this);

        // add the menu items to the menus
        zoomMenu.add(twentyFive);
        zoomMenu.add(fifty);
        zoomMenu.add(seventyFive);
        zoomMenu.add(hundred);
        zoomMenu.add(hundredFifty);
        zoomMenu.add(twoHundred);
        zoomMenu.add(fiveHundred);
        menuBar.add(zoomMenu);

        // setUpSelectType();

        // set the menu bar to this menu
        pictureFrame.setJMenuBar(menuBar);
    }

    /**
     * Create and initialize the scrolling image
     */
    private void createAndInitScrollingImage() {
        scrollPane = new JScrollPane();

        BufferedImage bimg = picture.getBufferedImage();
        imageDisplay = new ImageDisplay(bimg);
        imageDisplay.addMouseMotionListener(this);
        imageDisplay.addMouseListener(this);
      imageDisplay.setToolTipText(text);  
      //imageDisplay.setToolTipText("Click a mouse button on a pixel to see the pixel information");
        scrollPane.setViewportView(imageDisplay);
        pictureFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates the JFrame and sets everything up
     */
    private void createWindow() {
        // create the picture frame and initialize it
        createAndInitPictureFrame();

        // create the color panel
        createInfoPanel();

        // creates the scrollpane for the picture
        createAndInitScrollingImage();

        makeCursor();

        // show the picture in the frame at the size it needs to be
        pictureFrame.pack();
        pictureFrame.setVisible(true);

        

    }

    public void makeCursor() {
        // mouse = new CustomCursor()
      //System.out.print("hello");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
      //System.out.print("toolkit");
        Image image = toolkit.getImage("/home/runner/Free-Coloring-Butterfly/images/pizza.png");
        Cursor mouse = toolkit.createCustomCursor(image, new Point(27, 17), "mouse");
        imageDisplay.setCursor(mouse);
      //System.out.print("x" + image + " <- image");
    }

    // sets up the color buttons
    private void setUpButtons() {

        // dimensions of color option buttons
        Dimension d = new Dimension(50, 50);

        colorButtons = new JButton[colors.length];

        for (int i = 0; i < colorButtons.length; i++) {
            colorButtons[i] = new JButton(
                    new ImageIcon("/home/runner/Free-Coloring-Butterfly/images/" + colorName(colors[i]) + ".jpg"));
            colorButtons[i].setPreferredSize(d);
            setActionPerformed(colorButtons[i], colors[i]);
        }
    }

    public void setActionPerformed(JButton b, Color co) {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setColor(co);
            };
        });
    }

   

    /**
     * Create the color information panel
     * 
     * @param labelFont the font to use for labels
     * @return the color information panel
     */
    private JPanel createColorInfoPanel() {
        // create a color info panel
        JPanel colorInfoPanel = new JPanel();
        colorInfoPanel.setLayout(new FlowLayout());

        // get the pixel at the x and y
        setUpButtons();
        for (int i = 0; i < colorButtons.length; i++) {
            colorInfoPanel.add(colorButtons[i]);
        }

        return colorInfoPanel;
    }

    /**
     * Creates the North JPanel with all the pixel location and color information
     */
    private void createInfoPanel() {
        // create the info panel and set the layout
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());

        // create the color information panel
        JPanel colorInfoPanel = createColorInfoPanel();

        // add the panels to the info panel
        infoPanel.add(colorInfoPanel);

        // add the info panel
        pictureFrame.getContentPane().add(BorderLayout.SOUTH, infoPanel);
    }

    /**
     * Method to check that the current position is in the viewing area and if not
     * scroll to center the current position if possible
     */
    public void checkScroll() {
        // get the x and y position in pixels
        int xPos = (int) (colIndex * zoomFactor);
        int yPos = (int) (rowIndex * zoomFactor);

        // only do this if the image is larger than normal
        if (zoomFactor > 1) {

            // get the rectangle that defines the current view
            JViewport viewport = scrollPane.getViewport();
            Rectangle rect = viewport.getViewRect();
            int rectMinX = (int) rect.getX();
            int rectWidth = (int) rect.getWidth();
            int rectMaxX = rectMinX + rectWidth - 1;
            int rectMinY = (int) rect.getY();
            int rectHeight = (int) rect.getHeight();
            int rectMaxY = rectMinY + rectHeight - 1;

            // get the maximum possible x and y index
            int macolIndexX = (int) (picture.getWidth() * zoomFactor) - rectWidth - 1;
            int macolIndexY = (int) (picture.getHeight() * zoomFactor) - rectHeight - 1;

            // calculate how to position the current position in the middle of the viewing
            // area
            int viewX = xPos - (int) (rectWidth / 2);
            int viewY = yPos - (int) (rectHeight / 2);

            // reposition the viewX and viewY if outside allowed values
            if (viewX < 0)
                viewX = 0;
            else if (viewX > macolIndexX)
                viewX = macolIndexX;
            if (viewY < 0)
                viewY = 0;
            else if (viewY > macolIndexY)
                viewY = macolIndexY;

            // move the viewport upper left point
            viewport.scrollRectToVisible(new Rectangle(viewX, viewY, rectWidth, rectHeight));
        }
    }

    /**
     * Zooms in the on picture by scaling the image. It is extremely memory
     * intensive.
     * 
     * @param factor the amount to zoom by
     */
    public void zoom(double factor) {
        // save the current zoom factor
        zoomFactor = factor;

        // calculate the new width and height and get an image that size
        int width = (int) (picture.getWidth() * zoomFactor);
        int height = (int) (picture.getHeight() * zoomFactor);
        BufferedImage bimg = picture.getBufferedImage();

        // set the scroll image icon to the new image
        imageDisplay.setImage(bimg.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        imageDisplay.setCurrentX((int) (colIndex * zoomFactor));
        imageDisplay.setCurrentY((int) (rowIndex * zoomFactor));
        imageDisplay.revalidate();
        checkScroll(); // check if need to reposition scroll
    }

    /**
     * Repaints the image on the scrollpane.
     */
    public void repaint() {

        // imageDisplay.repaint();
        Graphics2D g2 = (Graphics2D) imageDisplay.getGraphics();
        imageDisplay.paintComponent(g2);
        // g2.setColor(Color.YELLOW);
        // if((startPoint == null || endPoint == null)&&selectedRect==null &&
        // selectedLine==null) {
        // System.out.println("Can't draw because start: "+startPoint +
        // " or end: "+endPoint +" and line "+selectedLine+" and rect "+
        // selectedRect+" are nulls ");
        // return;
        // }
        // int x1=0,y1=0,x2=0,y2=0;
        // try {
        // x1 = (int) startPoint.getX(); y1 = (int) startPoint.getY();
        // x2 =(int) endPoint.getX(); y2 = (int) endPoint.getY();
        // }catch(NullPointerException e) {
        // try {
        // x1 = (int) selectedLine.getX1(); y1 = (int) selectedLine.getY1();
        // x2 =(int)selectedLine.getX2(); y2 = (int)selectedLine.getY2();
        // }
        // catch(NullPointerException npe) {
        // try {
        // x1 = (int) selectedRect.getX(); y1 = (int) selectedRect.getY();
        // x2 =(int) selectedRect.getWidth()+x1; y2 = (int) selectedRect.getHeight()+y1;
        // }
        // catch(Exception ex) {
        // System.out.println("Should never get here...");
        // }
        // }
        // }
        // if(this.selectingLine) {
        // //Line2D x = this.selectedLine;
        // g2.drawLine(x1,y1,x2,y2);
        // }
        // else if(this.selectingRegion) {
        // //g2.setColor(Color.YELLOW);
        // //Rectangle x = this.selectedRect;
        // System.out.println("drawing rect: "+x1+" , "+y1+" w: "+(x2-x1)+" h:
        // "+(y2-y1));
        // g2.drawRect(x1,y1,x2-x1,y2-y1);
        // }
    }

    // ****************************************//
    // Event Listeners //
    // ****************************************//

    /**
     * Called when the mouse is dragged (button held down and moved)
     * 
     * @param e the mouse event
     */
    public void mouseDragged(MouseEvent e) {
        // System.out.println("dragging "+firstClick +" start: "+startPoint+" end:
        // "+endPoint);
        // if(this.selectingLine || this.selectingRegion) {
        // if(this.firstClick) {
        // System.out.println("first click and dragging");
        // firstClick = false;
        // startPoint = e.getPoint();
        // selectedLine = null;
        // selectedRect = null;
        // }
        // endPoint = e.getPoint();
        // }
        // this.repaint();
    }

    /**
     * Method to check if the given x and y are in the picture
     * 
     * @param column the horizontal value
     * @param row    the vertical value
     * @return true if the row and column are in the picture and false otherwise
     */
    private boolean isLocationInPicture(int column, int row) {
        boolean result = false; // the default is false
        if (column >= 0 && column < picture.getWidth() && row >= 0 && row < picture.getHeight())
            result = true;

        return result;
    }


    /**
     * Method to display pixel information for the passed x and y
     * 
     * @param pictureX the x value in the picture
     * @param pictureY the y value in the picture
     */
    private void displayPixelInformation(int pictureX, int pictureY) {
        // check that this x and y are in range
        if (isLocationInPicture(pictureX, pictureY)) {
            // save the current x and y index
            colIndex = pictureX;
            rowIndex = pictureY;

            // get the pixel at the x and y
            Pixel pixel = new Pixel(picture, colIndex, rowIndex);

        } else {
            clearInformation();
        }

        // notify the image display of the current x and y
        imageDisplay.setCurrentX((int) (colIndex * zoomFactor));
        imageDisplay.setCurrentY((int) (rowIndex * zoomFactor));
    }

    /**
     * Method to display pixel information based on a mouse event
     * 
     * @param e a mouse event
     */
    private void displayPixelInformation(MouseEvent e) {

        // get the cursor x and y
        int cursorX = e.getX();
        int cursorY = e.getY();

        Picture swan = Picture.getInstance();
        Pixel p = new Pixel(swan, cursorX, cursorY);
        Color pixel_c = p.getColor();
        try {
            swan.fill(swan.getBufferedImage(), cursorX, cursorY, pixel_c, getColor());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Click the image!");
        }
        // swan.explore();

        // get the x and y in the original (not scaled image)
        int pictureX = (int) (cursorX / zoomFactor + numberBase);
        int pictureY = (int) (cursorY / zoomFactor + numberBase);

        // display the information for this x and y
        displayPixelInformation(pictureX, pictureY);

    }

    /**
     * Method to clear the labels and current color and reset the current index to
     * -1
     */
    private void clearInformation() {
        colValue.setText("N/A");
        rowValue.setText("N/A");
        rValue.setText("R: N/A");
        gValue.setText("G: N/A");
        bValue.setText("B: N/A");
        colorPanel.setBackground(Color.black);
        colIndex = -1;
        rowIndex = -1;
    }

    /**
     * Method called when the mouse is moved with no buttons down
     * 
     * @param e the mouse event
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Method called when the mouse is clicked
     * 
     * @param e the mouse event
     */
    public void mouseClicked(MouseEvent e) {
        displayPixelInformation(e);
    }

    /**
     * Method called when the mouse button is pushed down
     * 
     * @param e the mouse event
     */
    public void mousePressed(MouseEvent e) {
        // displayPixelInformation(e);
    }

    /**
     * Method called when the mouse button is released
     * 
     * @param e the mouse event
     */
    public void mouseReleased(MouseEvent e) {
        // This is stuff that Mr. Hanson was adding to add a selecting line or
        // rectangle...
        // if(this.selectingLine) {
        // this.selectingLine = false;
        // this.selectedLine = new Line2D.Double(startPoint,endPoint);
        // System.out.println(this.selectedLine);
        // }
        // else if(this.selectingRegion) {
        // this.selectingRegion=false;
        // this.selectedRect = new Rectangle2D.Double(
        // startPoint.getX(), startPoint.getY(),endPoint.getX(),endPoint.getY());
        // System.out.println(this.selectedRect);
        // }
        // startPoint = null;
        // endPoint = null;
        // firstClick = true;
    }

    /**
     * Method called when the component is entered (mouse moves over it)
     * 
     * @param e the mouse event
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Method called when the mouse moves over the component
     * 
     * @param e the mouse event
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Method to enable all menu commands
     */
    private void enableZoomItems() {
        twentyFive.setEnabled(true);
        fifty.setEnabled(true);
        seventyFive.setEnabled(true);
        hundred.setEnabled(true);
        hundredFifty.setEnabled(true);
        twoHundred.setEnabled(true);
        fiveHundred.setEnabled(true);
    }

    /**
     * Controls the zoom menu bar
     *
     * @param a the ActionEvent
     */
    public void actionPerformed(ActionEvent a) {
        if (a.getActionCommand().equals("Update")) {
            this.repaint();
        }

        if (a.getActionCommand().equals("25%")) {
            this.zoom(.25);
            enableZoomItems();
            twentyFive.setEnabled(false);
        }

        if (a.getActionCommand().equals("50%")) {
            this.zoom(.50);
            enableZoomItems();
            fifty.setEnabled(false);
        }

        if (a.getActionCommand().equals("75%")) {
            this.zoom(.75);
            enableZoomItems();
            seventyFive.setEnabled(false);
        }

        if (a.getActionCommand().equals("100%")) {
            this.zoom(1.0);
            enableZoomItems();
            hundred.setEnabled(false);
        }

        if (a.getActionCommand().equals("150%")) {
            this.zoom(1.5);
            enableZoomItems();
            hundredFifty.setEnabled(false);
        }

        if (a.getActionCommand().equals("200%")) {
            this.zoom(2.0);
            enableZoomItems();
            twoHundred.setEnabled(false);
        }

        if (a.getActionCommand().equals("500%")) {
            this.zoom(5.0);
            enableZoomItems();
            fiveHundred.setEnabled(false);
        }
    }


    private void setColor(Color c) {
        this.c = c;
        text = "You have selected " + colorName(c);
        imageDisplay.setToolTipText(text);
    }

    private Color getColor() {
        return this.c;
    }

    public static String colorName(Color c) {
        for (Field f : Color.class.getDeclaredFields()) {
            // we want to test only fields of type Color
            if (f.getType().equals(Color.class)) {
                try {
                    if (f.get(null).equals(c))
                        return f.getName().toLowerCase();
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // shouldn't not be thrown, but just in case print its stacktrace
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * Test Main. It will explore the beach
     */
    public static void main(String args[]) {
        // Picture pix = new Picture("beach.jpg");
        // pix.explore();
    }

}
