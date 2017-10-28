public class Player
{
    private String name="GUEST";
    private SocketHandler handler;
    private Protocol col;
    public Player(SocketHandler h) {
        handler=h;
    }

    public SocketHandler getSocketHandler() {
        return handler;
    }

    public void setProtocol(Protocol p) {
        col=p;
        handler.setUser(p);
    }

    public Protocol getProtocol() {
        return col;
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