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
    
    /**
     * Returns the ip address of a user
     */
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
    
    /**
     * Disconnects a user
     */
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

    /**
     * Sends a message to the user
     */
    public void send(String s) {
        try{
            handler.send(s);
        }catch(Exception e) {

        }
    }

    public String getName() {
        return name;   
    }

    /**
     * Sets a user's username/nickname
     */
    public void setName(String s) {
        name=s;   
    }
}