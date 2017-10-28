import java.net.*;
import java.io.*;
import java.util.ArrayList;
public class ServerSocketHandler implements Runnable
{
    private ServerSocket socket;
    private boolean setup;
    private boolean done;
    private boolean finished;
    private int port;
    private int waitTime=100;
    private Server server;
    public ServerSocketHandler(Server s) throws Exception {
        server=s;
        setup();
    }

    public ServerSocket getSocket() {
        return socket;
    }

    public String getAddress() {
        try{
            return socket.getInetAddress().getLocalHost()+":"+port;
        }catch(Exception e) {
            return "";
        }
    }

    public void setup() throws Exception{
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
                return;
            }catch(Exception e) {
                socket=null;
            }
        }
        throw new Exception("Could not find a valid port");
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
                if(!server.listIsFull()) {
                    new DataOutputStream(client.getOutputStream()).writeUTF("Joining Server...");
                    server.addUser(new SocketHandler(client));
                }
                else {
                    new DataOutputStream(client.getOutputStream()).writeUTF("Sorry, the server is full. Disconnecting...");
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