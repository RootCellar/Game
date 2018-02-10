/**
 * Screen class
 * Rendering is done by accessing the pixel array of the displayed image
 * 
 */

import java.awt.*;
import java.awt.image.*;
import javax.swing.JFrame;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
public class PixelCanvas extends Canvas implements Runnable
{
    int WIDTH = 700;
    int HEIGHT = 700;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private JFrame frame;
    BufferStrategy bs = getBufferStrategy();
    int targetfps=30;
    boolean going = false;
    double Pfps = 0;
    long timeSinceLast = System.nanoTime();
    int frames = 0;
    int fps = 0;

    /**
     * Sets the size of the window
     */
    public void setSize(int w, int h) {
        frame.setMinimumSize( new Dimension( w , h ) );
        frame.setMaximumSize( new Dimension( w , h ) );
        frame.setPreferredSize( new Dimension( w , h ) );
    }
    
    public void setTargetFps(int f) {
        targetfps=f;
    }

    public void start() {
        new Thread(this).start();
        going=true;
    }

    public void stop() {
        going=false;
    }

    /**
     * This method can be used to render the screen at the desired frame rate
     */
    public void run() {
        going = true;
        long time = System.nanoTime();
        while(going) {
            try{
                Thread.sleep(1);
            }catch(Exception e) {

            }

            try{
                if(System.nanoTime() - time >= 1000000000/targetfps) {
                    draw();
                    time = System.nanoTime();
                }
            }catch(Exception e) {

            }
        }
        going=false;
    }

    /**
     * Renders and find fps
     */
    public void draw() {
        long time1 = System.nanoTime();
        render();
        long time2 = System.nanoTime();
        if(System.nanoTime() - timeSinceLast > 1000000000) {
            Pfps = 1000000000.0 / ( (double)( time2 - time1 ) );
            fps = frames;
            frames = 0;
            timeSinceLast = System.nanoTime();
        }
        //fps = Math.random();
    }   

    /**
     * Draws fps debug info on the screen
     */
    public void drawFps(Graphics g) {
        g.setColor(Color.RED);
        g.drawString("PFPS: "+Pfps,10,10);
        g.drawString("FPS: "+fps,10,20);
    }

    /**
     * Draws the image on the screen
     */
    public void render() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLUE);
        g.fillRect(0,0,getWidth(),getHeight());

        g.drawImage(image,0,0,WIDTH,HEIGHT,null);

        drawFps(g);

        g.dispose();
        bs.show();
        frames++;
    }

    /**
     * Clears the image, making it entirely black
     */
    public void clear() {
        for(int y=0; y< HEIGHT; y++) {
            for(int x=0; x < WIDTH; x++) {
                pixels[x+y*WIDTH]=0;
            }
        }
    }

    /**
     * Set the pixel in the desired location to the desired color
     */
    public void setPixel(int x, int y, int r, int g, int b) {
        try{
            int i = x + y * WIDTH;
            pixels[i]=getColor(r,g,b);
        }catch(Exception e) {

        }
    }

    /**
     * Used to find the number that represents the desired amount of red, green, and blue
     */
    public static int getColor(int r, int g, int b) {
        int r2=r<<16;
        int g2=g<<8;
        int b2=b;

        /*
        System.out.println(r2+"\n");
        System.out.println(g2+"\n");
        System.out.println(b2+"\n");
         */

        //System.out.println((r2+g2+b2)+"\n");
        try{
            return r2+g2+b2;
        }catch(Exception e) {
            return 0;
        }
    }

    /**
     * Sets each pixel to a random color
     */
    public void randomize() {
        for(int y=0; y< HEIGHT; y++) {
            for(int x=0; x < WIDTH; x++) {
                pixels[x+y*WIDTH]=(int)(Math.random()*16777215.0);
                //pixels[x+y*WIDTH]=0;
            }
        }
    }

    public PixelCanvas() {
        frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        //frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        PixelCanvas p = new PixelCanvas();
        int frames=0;
        long start=System.nanoTime();
        Terminal term = new Terminal();
        while(true) {
            try{
                p.clear();
                p.randomize();
                p.render();
                //Thread.sleep(1000 / 10);
                frames++;
            }catch(Exception e) {
                e.printStackTrace();
            }
            if(System.nanoTime()-start >= 1000000000) {
                term.write(frames+"");
                frames=0;
                start=System.nanoTime();
            }
        }
    }
}