import java.util.ArrayList;
public class Server implements Runnable,InputUser
{
    private ServerSocketHandler connectHandler;
    private Terminal term;
    private DataDisplay disp;
    private boolean usingTerm = false;
    private ArrayList<User> Users = new ArrayList<User>();
    private ArrayList<Match> matches = new ArrayList<Match>();
    private int maxUsers = 50;
    private boolean going = false;
    private boolean halted = false;
    private boolean mmon = true;
    private MatchMaker mm = new MatchMaker(this);
    private int tps = 0;
    private Logger logger = new Logger();
    public static void main(String args[]) {
        Server s = new Server();
        s.openTerm();
        s.start();
    }

    public void endMatches() {
        for(int i=0; i<matches.size(); i++) {
            if(matches.get(i).going==false) {
                matches.remove(i);
            }
        }
    }

    public void endAllMatches() {
        for(int i=0; i<matches.size(); i++) {
            matches.get(i).end();
            matches.remove(i);
        }
    }

    public void addMatch(Match m) {
        matches.add(m);
    }

    public void putInMatchMaker(User u) {
        mm.addUser(u);
    }

    public void start() {
        new Thread(this).start();
    }

    public void setMaxPlayers(int x) {
        maxUsers=x;
    }

    public void stop() {
        going=false;
    }

    public boolean listIsFull() {
        return Users.size()>=maxUsers;
    }

    public void addUser(SocketHandler s) {
        User p = new User(s,this);
        JoinMenu m = new JoinMenu();
        m.setUser(p);
        p.setHandler(m);
        Users.add(p);
        out("New User Connected from "+s.getAddress());
    }

    public void run() {
        out("Starting server...");
        try{
            long time1 = System.nanoTime();
            connectHandler= new ServerSocketHandler(this);
            out("Took "+((double)(System.nanoTime()-time1)/1000000000)+" Seconds to start server socket handler.");
        }catch(Exception e) {
            out(e.getStackTrace()+"");
            return;
        }
        try{
            connectHandler.setTimeout(0);
        }catch(Exception e) {

        }
        connectHandler.setWaitTime(100);
        out("Server is being hosted on "+getAddress());
        going=true;
        long time1=System.nanoTime();
        int tps2=0;
        while(going) {
            try{
                if(!halted) Thread.sleep(1000 / 10);
                else Thread.sleep(1000 / 1);
            }catch(Exception e) {

            }
            removeUnconnected();
            if(usingTerm) {
                disp.setText(0,"Users: "+Users.size());
                disp.setText(1,"Host Address: "+getAddress());
                disp.setText(2,"TPS: "+tps);
                disp.setText(3, "Halted: "+halted);
            }

            if(mmon && !halted) {
                mm.run();
                endMatches();
            }

            tps2++;
            if(System.nanoTime() - time1 >= 1000000000) {
                tps=tps2;
                time1=System.nanoTime();
                tps2=0;
            }
        }
        out("Closing Server...");
        endAllMatches();
        endMatches();
        connectHandler.stop();
        for(int i=0; i<Users.size(); i++) {
            Users.get(i).disconnect();
        }
        removeUnconnected();

        logger.close();
        
        out("Server Closed");
    }

    public void removeUnconnected() {
        for(int i=0; i<Users.size(); i++) {
            SocketHandler p = Users.get(i).getSocketHandler();
            if(p.isConnected()==false) {
                Users.remove(i);
            }
        }
    }

    public void inputText(String i) {
        if(i.equals("/help")) {
            out("/halt - halt the server");
            out("/mmon - turn the match maker on and off");
            out("/stop - stop the server");
            
        }
        else if(i.equals("/stop")) {
            stop();
        }
        else if(i.equals("/halt")) {
            halted=!halted;
            if(halted) out("Server activity halted!");
            else out("Server activity no longer halted");
        }
        else if(i.equals("/mmon")) {
            mmon=!mmon;
            out("MatchMaker on: "+mmon);
        }
        else sendAll(i);
        logger.log("SERVER ADMIN: "+i);
    }

    public void sendAll(User u, String s) {
        out("["+u.getName()+"] "+s);
        for(int i=0; i<Users.size(); i++) {
            Users.get(i).send("["+u.getName()+"] "+s);
        }
    }

    public void sendAll(String s) {
        out("[SERVER] "+s);
        for(int i=0; i<Users.size(); i++) {
            Users.get(i).send("[SERVER] "+s);
        }
    }

    public void out(String o) {
        if(usingTerm) {
            term.write(o+"\n");
        }
        logger.log(o);
    }

    public void openTerm() {
        term=new Terminal();
        term.setUser(this);
        term.setTitle("Server Terminal");
        disp = new DataDisplay(10);
        usingTerm=true;
    }

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