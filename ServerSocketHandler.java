import java.net.*;
import java.io.*;
import java.util.ArrayList;
public class ServerSocketHandler implements Runnable
{
    private ServerSocket socket;
    private boolean setup;
    private boolean done;
    private boolean finished=true;
    private int port;
    private int waitTime=100;
    private Server server;
    public ServerSocketHandler(Server s) {
        server=s;
        //setup();
    }

    public boolean isFinished() {
        return finished;
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
        if(!finished) {
            return;
        }
        finished=false;
        setup=false;
        done=false;
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
        finished=true;
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
                if(client!=null) {
                    new DataOutputStream(client.getOutputStream()).writeUTF("Connection Accepted.");
                    if(!server.listIsFull()) {
                        new DataOutputStream(client.getOutputStream()).writeUTF("Joining Server...");
                        server.addUser(new SocketHandler(client));
                    }
                    else {
                        new DataOutputStream(client.getOutputStream()).writeUTF("Sorry, the server is full. Disconnecting...");
                        client.close();
                    }
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
    
    public void close() {
        try{
            socket.close();
            port=-1;
        }catch(Exception e) {
            
        }
    }

    public void stop() {
        close();
        done=true;
    }
}