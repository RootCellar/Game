import java.net.*;
import java.awt.*;
public class Game implements Runnable
{
    public PixelCanvas screen = new PixelCanvas();
    public InputListener input = new InputListener(this);
    public int x = 0;
    public int y = 0;
    public Socket server;
    public boolean going=false;
    public Mob[] mobs = new Mob[10000];
    
    public static void main(String args[]) {
        new Game().start();
    }
    
    public Game() {
        //new Thread(this).start();
        for(int i=0; i<mobs.length; i++) {
            mobs[i] = new Mob();
            mobs[i].findNewSpot();
        }
        screen.setSize(750,750);
        
    }

    public void setServer(Socket s) {
        server=s;
    }

    public void start() {
        if(!going) {
            going=true;
            new Thread(this).start();
            System.out.println("Starting game...");
            screen.setVisible(true);
        }
    }

    public void stop() {
        going = false;
        //screen.setVisible(false);
    }

    public void run() {
        long unprocessed = 0;
        long timeSinceLast=System.nanoTime();
        int targetTps=100;
        int targetFps=60;
        int time = 1000000000 / targetTps;
        
        
        while(going) {
            try{
                Thread.sleep(1);
                unprocessed += ( System.nanoTime() - timeSinceLast);
                timeSinceLast = System.nanoTime();
                while(unprocessed>=time) {
                    tick();
                    unprocessed -= time;
                }
                //timeSinceLast = System.nanoTime();
                //screen.draw();
            }catch(Exception e) {
                
            }
        }
        going=false;
    }

    public void tick() {
        try{
            //Thread.sleep(10);
            if(input.down.down) y=y+1;
            if(input.up.down) y=y-1;
            if(input.left.down) x=x-1;
            if(input.right.down) x=x+1;
            if(x<10) x=10;
            if(y<10) y=10;
            if(x>screen.WIDTH-10) x = screen.WIDTH-10;
            if(y>screen.HEIGHT-10) y = screen.HEIGHT-10;
            screen.clear();
            screen.setPixel(x,y,255,255,255);
            screen.setPixel(x+1,y,255,255,255);
            screen.setPixel(x,y+1,255,255,255);
            screen.setPixel(x+1,y+1,255,255,255);

            
            //ai();
            
            renderMobs();

            screen.draw();
        }catch(Exception e) {

        }   
    }

    public void ai() {
        for(int i=0; i<mobs.length; i++) {
            int r = (int)(Math.random()*4.0);
            if(r==0) mobs[i].x++;
            if(r==1) mobs[i].x--;
            if(r==2) mobs[i].y++;
            if(r==3) mobs[i].y--;
        }
    }

    public void renderMobs() {
        for(int i=0; i<mobs.length; i++) {
            mobs[i].ai();
            mobs[i].collision();
            screen.setPixel(mobs[i].x, mobs[i].y,100,100,100);
        }
    }
    
}