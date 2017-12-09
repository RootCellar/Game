import java.net.*;
import java.io.*;
public class SocketHandler implements Runnable
{
    private static int waitTime=100;
    private boolean going;
    private boolean connected=true;
    private static boolean globalGoing=true;
    private static int maxMessageLength=500;
    private Socket client;
    private InputUser user;
    private Thread thread;
    public SocketHandler(Socket s) {
        client=s;
        connected=true;
        thread=new Thread(this);
        thread.start();
        going=true;
    }
    
    public String getAddress() {
        return client.getRemoteSocketAddress().toString();
    }
    
    public void check() {
        thread.interrupt();
    }
    
    public boolean isGoing() {
        return going;
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public void setUser(InputUser i) {
        user=i;
    }

    public void send(String s) throws IOException {
        new DataOutputStream(client.getOutputStream()).writeUTF(s);
    }

    public void close() {
        try{
            client.close();
        }catch(Exception e) {

        }
        going=false;
        connected=false;
    }

    public void run() {
        DataInputStream in=null;
        try{
            in = new DataInputStream(client.getInputStream());
        }catch(Exception e) {
            
        }
        while(going && globalGoing) {
            try{
                Thread.sleep(waitTime);
            }catch(Exception e) {

            }
            try{
                String s = in.readUTF();
                if(going && globalGoing) {
                    if(s.length()>maxMessageLength) {
                        send("That message is too long");
                        return;
                    }
                    user.inputText(s);
                }
            }catch(IOException e) {
                connected=false;
                going=false;
            }catch(Exception e) {
                
            }

        }
        going=false;
        connected=false;
    }

    public static void setWaitTime(int x) {
        waitTime=x;
    }

    public static void setGoing(boolean b) {
        globalGoing=b;
    }
    
    public static void setMaxMessageLength(int i) {
        maxMessageLength=i;
    }
}