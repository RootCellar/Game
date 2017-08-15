import java.net.*;
import java.io.*;
import java.util.ArrayList;
public class ServerSocketHandler implements Runnable
{
    private ServerSocket socket;
    private boolean setup;
    private boolean done;
    private boolean finished;
    private int threshold=20;
    private int port;
    private int waitTime=0;
    private ArrayList<SocketHandler> connections;
    private Server server;
    public ServerSocketHandler(Server s) {
        server=s;
        setup();
    }

    public ServerSocket getSocket() {
        return socket;
    }

    public void setup() {
        setup=false;
        done=true;
        port=-1;
        socket=null;
        for(int i=10000; i<65535; i++) {
            try{
                socket = new ServerSocket(i);
                setup=true;
                port=i;
                startThread();
                break;
            }catch(Exception e) {
                socket=null;
            }
        }
        try{
            server.out("Server Socket listening on "+socket.getInetAddress().getLocalHost()+":"+port);
        }catch(Exception e) {

        }
    }

    public int getPort() {
        return port;
    }

    public boolean isSetup() {
        return setup;
    }

    public boolean isDone() {
        return done;
    }

    public SocketHandler getNext() {
        return connections.remove(0);
    }

    public boolean hasNext() {
        return connections.size()>0;
    }

    public void setThreshold(int x) {
        threshold=x;
    }

    public void setTimeout(int x) throws SocketException {
        socket.setSoTimeout(x);
    }

    public void startThread() {
        done=false;
        new Thread(this).start();
    }

    public void setWaitTime(int x) {
        waitTime=x;
    }

    public void run() {
        Socket client;
        finished=false;
        while(!done) {
            try{
                client=socket.accept();
                new DataOutputStream(client.getOutputStream()).writeUTF("Connection Accepted.");
                if(connections.size()<threshold) {
                    new DataOutputStream(client.getOutputStream()).writeUTF("Entering join list...");
                    connections.add(new SocketHandler(client));
                }
                else {
                    new DataOutputStream(client.getOutputStream()).writeUTF("Sorry, the list has reached it's threshold. Disconnecting...");
                    client.close();
                }
            }catch(Exception e) {

            }
            try{
                Thread.sleep(waitTime);
            }catch(Exception e) {

            }
        }
        done=true;
        finished=true;
    }

    public void stop() {
        done=true;
    }
}