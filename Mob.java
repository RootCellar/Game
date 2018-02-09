public class Mob
{
    int x = 0;
    int y = 0;
    int tx = 50;
    int ty = 50;
    int waittime = 10;
    
    public void collision() {
        if(x<10) x=10;
        if(x>690) x=690;
        if(y<10) y=10;
        if(y>690) y=690;
    }
    
    public void ai() {
        /*
        int r = (int)(Math.random()*4.0);
        if(r==0) x++;
        if(r==1) x--;
        if(r==2) y++;
        if(r==3) y--;
        */
        /*
        if(tx==x) tx = (int)((Math.random()*490) + 10);
        if(ty==y) ty = (int)((Math.random()*490) + 10);
        */
       
        if(waittime>0) {
            waittime--;
            return;
        }
        
        if(tx==x && ty==y) {
            waittime=400;
            findNewSpot();
            return;
        }
       
        if(tx>x) x++;
        if(ty>y) y++;
        if(tx<x) x--;
        if(ty<y) y--;
    }
    
    public void findNewSpot() {
        tx = (int)((Math.random()*680) + 10);
        ty = (int)((Math.random()*680) + 10);
    }
}