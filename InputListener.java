import java.util.ArrayList;
import java.awt.event.*;
public class InputListener implements KeyListener
{
    public class Key
    {
        public boolean down;
        public void Key()
        {
            keys.add(this);
        }
        
        public void toggle(boolean pressed) {
            down = pressed;
        }
    }
    
    public ArrayList<Key> keys = new ArrayList<Key>();
    
    Key up = new Key();
    Key down = new Key();
    Key left = new Key();
    Key right = new Key();
    
    public InputListener(Game game) {
        game.screen.addKeyListener(this);
    }
    
    public void toggle(KeyEvent ke, boolean pressed) {
        System.out.println(ke);
        if(ke.getKeyCode() == KeyEvent.VK_W) up.toggle(pressed);
        if(ke.getKeyCode() == KeyEvent.VK_S) down.toggle(pressed);
        if(ke.getKeyCode() == KeyEvent.VK_A) left.toggle(pressed);
        if(ke.getKeyCode() == KeyEvent.VK_D) right.toggle(pressed);
    }
    
    public void keyPressed(KeyEvent ke) {
        toggle(ke,true);
    }
    
    public void keyReleased(KeyEvent ke) {
        toggle(ke,false);
    }
    
    public void keyTyped(KeyEvent ke) {
        
    }
}