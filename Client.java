 
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class Client implements InputUser,Runnable {
    private Socket server=null;
    private Terminal term;
    private boolean connected=false;
    public static void main(String args[]) {
        new Thread(new Client()).start();
    }
    
    public void run() {
        //sendThread t = new sendThread();
        //Thread t2=new Thread(t);
        //t2.start();
        term = new Terminal();
        term.setUser(this);
        term.setVisible(false);
        Scanner in2 = new Scanner(System.in);
        while(true) {
            term.setVisible(false);
            String in="";
            String addr="";
            String port="";
            System.out.println("Type in this window when joining server/hosting one.");
            System.out.println("To send message to server, use terminal GUI");
            System.out.println("When hosting a server, you can type into the terminal to use server command.");
            System.out.println("Host or Join? (h or j)");
            in=in2.next();
            if(in.equals("j")) {
                System.out.println("Server address?");
                addr=in2.next();
                System.out.println("Server port?");
                port=in2.next();
                try{
                    System.out.println("Trying to connect to the server...");
                    server = new Socket(addr,Integer.parseInt(port));
                    System.out.println("Connected.");
                    //t2.start();
                    term.setVisible(true);
                    while(true) {
                        in=new DataInputStream(server.getInputStream()).readUTF();
                        term.write(in);
                        //System.out.println("DEBUG: "+in.getBytes());
                    }
                }catch(IOException e) {
                    System.out.println("\n\nCan't connect to server");
                }catch(Exception e) {
                    System.out.println("Couldn't launch server");
                }
            }
            if(in.equals("h")) {
                System.out.println("Creating private server...");
                Server s = new Server();
                Thread s2 = new Thread(s);
                s2.start();
                //s.notServer();
                System.out.println("Waiting for port...");
                while(s.getPort()<1) {
                    try{
                        Thread.sleep(100);
                    }catch(Exception e) {

                    }
                }
                try{
                    System.out.println("Trying to connect to the server...");
                    server = new Socket("localhost",s.getPort());
                    System.out.println("Connected.");
                    term.setVisible(true);
                    term.write("Connected.");
                    //t2.start();
                    while(true) {
                        in=new DataInputStream(server.getInputStream()).readUTF();
                        term.write(in);
                        //System.out.println("DEBUG: "+in.getBytes());
                    }
                }catch(IOException e) {
                    System.out.println("\n\nCan't connect to server");
                    s.inputText("/stop");
                }   
            }
        }
    }
    
    public void inputText(String i) {
        send(i);
    }

    public void send(String m) {
        if(server==null) {
            return;
        }
        try{
            new DataOutputStream(server.getOutputStream()).writeUTF(m);
        }catch(IOException e) {
            System.out.println("Failed to send message to server");
        }
    }
}