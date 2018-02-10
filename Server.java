/**
 * Main Server Class
 */

import java.util.ArrayList;
public class Server implements Runnable,InputUser
{
    private ServerSocketHandler connectHandler;
    private Terminal term;
    private DataDisplay disp;
    private boolean usingTerm = false;
    private ArrayList<User> Users = new ArrayList<User>();
    private int maxUsers = 50;
    private boolean going = false;
    private boolean halted = false;
    private boolean mmon = true;
    private int tps = 0;
    private double tickTime = 0;
    private Logger logger = new Logger();
    private boolean debug = false;
    private int dispLabelCount = 13;
    private int totalBytesSent = 0;
    private long startTime = System.nanoTime();
    private int targetTps = 20;
    
    public static void main(String args[]) {
        Server s = new Server();
        if(args.length>0) {
            if(args[0].equals("debug")) {
                s.inputText("/debug");
            }
        }
        s.openTerm();
        s.start();
    }
    
    public void calcTotalBytesSent() {
        totalBytesSent = 0;
        for(int i=0; i<Users.size(); i++) {
            SocketHandler s = Users.get(i).getSocketHandler();
            totalBytesSent+=s.getSize();
        }
    }
    
    public boolean isHalted() {
        return halted;
    }

    public void start() {
        new Thread(this).start();
    }

    public void setMaxPlayers(int x) {
        maxUsers=x;
    }

    /**
     * Stops the server thread and shuts down the server
     */
    public void stop() {
        going=false;
    }

    public boolean listIsFull() {
        return Users.size()>=maxUsers;
    }

    /**
     * Sets up a user that connected to the server
     */
    public void addUser(SocketHandler s) {
        User p = new User(s,this);
        MainMenu m = new MainMenu();
        m.setUser(p);
        p.setHandler(m);
        Users.add(p);
        out("New User Joined From "+s.getAddress());
    }
    
    public int getUserCount() {
        return Users.size();
    }
    
    /**
     * Starts the server socket handler.
     * If the server is in debug mode, this method will also output the time
     * it took to open the server socket handler.
     */
    public void startSocketHandler() {
        if(!connectHandler.isFinished()) {
            out("Could not start server socket handler: It's already started!");
            return;
        }
        try{
            long time2 = System.nanoTime();
            connectHandler.setup();
            debug("Took "+((double)(System.nanoTime()-time2)/1000000000)+" Seconds to start the server socket handler.");
            connectHandler.setTimeout(1);
            connectHandler.setWaitTime(100);
        }catch(Exception e) {
            out("Could not start server socket handler");
            debug(e.getStackTrace()+"");
            return;
        }
    }
    
    public void createSocketHandler() {
        try{
            connectHandler = new ServerSocketHandler(this);
        }catch(Exception e) {
            out("Could not create ServerSocketHandler");
            debug(e.getStackTrace()+"");
        }
    }

    /**
     * Method containing setup and main server loop
     */
    public void run() {
        logger.open(); // opens a file for logging server output
        out("Starting server...");
        long time1 = System.nanoTime();
        
        createSocketHandler();
        
        startSocketHandler();
        
        out("Server is being hosted on "+getAddress());
        going=true;
        debug("Took "+((double)(System.nanoTime()-time1)/1000000000)+" Seconds to start the server.");
        time1=System.nanoTime();
        int tps2=0;
        while(going) {
            
            long tickStart = System.nanoTime();
            
            try{ // Waits to make the server run at the desired ticks per second
                //if(!halted) Thread.sleep(0);
                if(!halted) Thread.sleep(1000 / targetTps);
                else Thread.sleep(1000 / 1); //Makes server slow to 1 tps when halted
            }catch(Exception e) {

            }
            
            tickStart = System.nanoTime();
            
            //System.gc();
            
            removeUnconnected();
            
            if(usingTerm) { //Give the server statistics to the display GUI
                disp.setText(0, "Users: " + Users.size() );
                disp.setText(1, "Host Address: " + getAddress() );
                disp.setText(2, "TPS: " + tps );
                disp.setText(3, "Halted: " + halted );
                disp.setText(4, "Tick Time: " + tickTime + " ms" );
                //disp.setText(5, "Matches: " + matches.size() );
                //disp.setText(6, "Players in MatchMaker: "+ mm.getQueueSize() );
                disp.setText(7, "Bytes Sent: " + totalBytesSent );
                disp.setText(8, "Free Mem: " + Runtime.getRuntime().freeMemory() );
                disp.setText(9, "Total Mem: " + Runtime.getRuntime().totalMemory() );
                disp.setText(10, "Used Mem: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() ) );
                disp.setText(11, "Used %: " + (100 * (double)(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) / (double)Runtime.getRuntime().totalMemory()));
                disp.setText(12, "Uptime: "+( (double) ( System.nanoTime() - startTime ) / 1000000000.0 ) + "s");
            }

            if(mmon && !halted) {
                //mm.run();
                //endMatches();
            }
            
            if(!logger.canWrite) logger.reopen(); //Reopens log file if it is currently not open/working

            tickTime = ( ( System.nanoTime() - (double)tickStart ) ) / 1000000;
            
            tps2++;
            if(System.nanoTime() - time1 >= 1000000000) { //Tracks server tps
                tps=tps2;
                time1=System.nanoTime();
                tps2=0;
                calcTotalBytesSent();
            }
        }
        //Code to close the server when main loop is done
        out("Closing Server...");
        sendAll("SERVER","Closing Server...");
        //endAllMatches();
        //endMatches();
        connectHandler.stop();
        
        for(int i=0; i<Users.size(); i++) {
            Users.get(i).disconnect();
        }
        
        removeUnconnected();

        logger.close();
        
        closeTerm();
        
        out("Server Closed");
    }

    /**
     * Find users that are no longer connected and remove them from the user list
     */
    public void removeUnconnected() {
        for(int i=0; i<Users.size(); i++) {
            SocketHandler p = Users.get(i).getSocketHandler();
            if(p.isConnected()==false) {
                out("Removing a user: "+Users.get(i).getName());
                Users.remove(i);
                
            }
        }
    }
    
    public void inputObject(Object o) {
        
    }

    /**
     * Method to handle server commands
     */
    public void inputText(String i) {
        logger.log("SERVER ADMIN: "+i);
        if(i.equals("/help")) {
            out("/halt - halt the server");
            out("/mmon - turn the match maker on and off");
            out("/stop - stop the server");
            out("/debug - enable debug mode");
            out("/open - open server socket, if it is not already open");
            out("/close - close server socket, stop accepting connections");
            out("/ttps <int> - set target tps for server");
        }
        else if(i.equals("/stop")) {
            stop();
        }
        else if(i.equals("/halt")) { //halt the server, if there is intensive activity
            halted=!halted;
            if(halted) {
                out("Server activity halted!");
                sendAll("SERVER","Server activity halted!");
            }
            else {
                out("Server activity no longer halted");
                sendAll("SERVER","Server activity no longer halted");
            }
        }
        else if(i.equals("/mmon")) {
            mmon=!mmon;
            out("MatchMaker on: "+mmon);
            if(mmon) sendAll("SERVER","MatchMaker Enabled");
            else sendAll("SERVER","MatchMaker Disabled");
        }
        else if(i.equals("/debug")) { //enable/disable debug mode
            debug=!debug;
            debug(debug+"");
        }
        else if(i.equals("/close")) { //close server socket handler
            out("Stopping server socket handler...");
            connectHandler.stop();
        }
        else if(i.equals("/open")) { //open server socket handler
            out("Opening server socket handler...");
            startSocketHandler();
        }
        else if(Command.is("/ttps",i)) { //Set desired ticks per second
            int x;
            try{
                x = Integer.parseInt( Command.getArgs(i).get(1));
            }catch(Exception e) {
                out("Please enter an integer!");
                return;
            }
            targetTps=x;
            out("Target tps set to "+x);
        }
        else sendAll("SERVER ADMIN",i); //Send a message to all connected users
    }

    /**
     * Used when users send messages
     */
    public void sendAll(User u, String s) {
        out("["+u.getName()+"] "+s);
        for(int i=0; i<Users.size(); i++) {
            Users.get(i).send("["+u.getName()+"] "+s);
        }
    }

    /**
     * Sends the desired message to everyone, and outputs it to the server terminal
     */
    public void sendAll(String s) {
        //out("[SERVER] "+s);
        out(s);
        for(int i=0; i<Users.size(); i++) {
            //Users.get(i).send("[SERVER] "+s);
            Users.get(i).send(s);
        }
    }
    
    /**
     * Send a message to everyone, but with a title
     */
    public void sendAll(String t, String m) {
        //out("["+t+"] "+m);
        sendAll("["+t+"] "+m);
    }
    
    /**
     * Writes debug output if debug mode is enabled
     */
    public void debug(String o) {
        if(debug) {
            out("[DEBUG] "+o);
        }
    }

    /**
     * Writes output to server terminal and to logs.
     */
    public void out(String o) {
        if(usingTerm) {
            term.write(o);
        }
        logger.log(o);
    }

    /**
     * Opens terminal and data gui
     */
    public void openTerm() {
        term=new Terminal();
        term.setUser(this);
        term.setTitle("Server Terminal");
        disp = new DataDisplay(dispLabelCount);
        usingTerm=true;
    }

    /**
     * Close terminal and gui
     */
    public void closeTerm() {
        term.setVisible(false);
        disp.setVisible(false);
        usingTerm=false;
    }

    public int getPort() {
        if(connectHandler==null) {
            return -1;
        }
        return connectHandler.getPort();
    }

    public String getAddress() {
        try{ 
            return ""+connectHandler.getSocket().getInetAddress().getLocalHost()+":"+getPort();
        }catch(Exception e) {
            return null;
        }
    }

    public ServerSocketHandler getServerSocketHandler() {
        return connectHandler;
    }
}