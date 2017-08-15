import java.util.ArrayList;
public class Server implements Runnable,InputUser
{
    private ServerSocketHandler connectHandler;
    private Terminal term;
    private boolean usingTerm=false;
    private ArrayList<Player> players=new ArrayList<Player>();;
    private int maxPlayers=50;
    private boolean going;
    public static void main(String args[]) {
        Server s = new Server();
        s.openTerm();
        new Thread(s).start();
    }

    public void setMaxPlayers(int x) {
        maxPlayers=x;
    }

    public void stop() {
        going=false;
    }

    public void run() {
        connectHandler= new ServerSocketHandler(this);
        try{
            connectHandler.setTimeout(1);
        }catch(Exception e) {

        }
        connectHandler.setWaitTime(100);
        going=true;
        while(going) {
            try{
                Thread.sleep(1000 / 10);
            }catch(Exception e) {

            }
            if(players.size()<maxPlayers) {

            }
        }
    }

    public void inputText(String i) {

    }

    public void out(String o) {
        if(usingTerm) {
            term.write(o+"\n");
        }
    }

    public void openTerm() {
        term=new Terminal();
        term.setUser(this);
        term.setTitle("Server Terminal");
        usingTerm=true;
    }

    public void closeTerm() {
        term.setVisible(false);
        usingTerm=false;
    }

    public int getPort() {
        if(connectHandler==null) {
            return -1;
        }
        return connectHandler.getPort();
    }

    public String getAddress() {
        try{ 
            return ""+connectHandler.getSocket().getInetAddress().getLocalHost()+":"+getPort();
        }catch(Exception e) {
            return null;
        }
    }

    public ServerSocketHandler getServerSocketHandler() {
        return connectHandler;
    }
}