public class User
{
    private String name="GUEST";
    private SocketHandler handler;
    private Menu menu;
    private Server server;
    private boolean playing=false;
    public User(SocketHandler h, Server s) {
        handler=h;
        server=s;
    }
    
    public boolean isConnected() {
        return handler.isConnected();
    }
    
    public String getAddress() {
        return handler.getAddress();
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    public void playing(boolean b) {
        playing=b;
    }
    
    public Server getServer() {
        return server;
    }
    
    public void disconnect() {
        handler.close();
    }

    public SocketHandler getSocketHandler() {
        return handler;
    }

    public void setHandler(Menu m) {
        menu=m;
        handler.setUser(m);
    }

    public UserHandler getHandler() {
        return menu;
    }

    public void send(String s) {
        try{
            handler.send(s);
        }catch(Exception e) {

        }
    }

    public String getName() {
        return name;   
    }

    public void setName(String s) {
        name=s;   
    }
}