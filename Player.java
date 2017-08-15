public class Player
{
    private SocketHandler handler;
    public Player(SocketHandler h) {
        handler=h;
    }
    
    public SocketHandler getSocketHandler() {
        return handler;
    }
}