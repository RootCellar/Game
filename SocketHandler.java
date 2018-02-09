import java.net.*;
import java.io.*;
public class SocketHandler implements Runnable
{
    private static int waitTime=100;
    private boolean going;
    private boolean connected=true;
    private static boolean globalGoing=true;
    private static int maxMessageLength=100;
    private Socket client;
    private InputUser user;
    private Thread thread;
    DataOutputStream send;
    public SocketHandler(Socket s) {
        client=s;
        connected=true;
        thread=new Thread(this);
        thread.start();
        going=true;
        try{
            send = new DataOutputStream(client.getOutputStream());
        }catch(Exception e) {
            close();
        }
    }

    public int getSize() {
        return send.size();
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
        send.writeUTF(s);
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
        ObjectInputStream in2=null;
        try{
            in = new DataInputStream(client.getInputStream());
            in2 = new ObjectInputStream(in);
        }catch(Exception e) {

        }
        while(going && globalGoing) {
            try{
                Thread.sleep(waitTime);
            }catch(Exception e) {

            }
            try{

                Object o = in2.readObject(); //Read the object

                if(o instanceof MessagePacket) { //If read object is messagepacket
                    MessagePacket o2 = (MessagePacket)o;

                    String s = o2.message;
                    if(going && globalGoing) {
                        if(s.length()>maxMessageLength) {
                            send("That message is too long");
                            return;
                        }
                        user.inputText(s);
                    }
                }

                
            }catch(IOException e) {
                connected=false;
                going=false;
            }catch(NullPointerException e) {
                
            }catch(Exception e) {
                e.printStackTrace();
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