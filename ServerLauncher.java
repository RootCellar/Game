import java.util.ArrayList;
public class ServerLauncher implements InputUser
{
    private ArrayList<Server> servers= new ArrayList<Server>();
    private Terminal term = new Terminal();
    boolean going = true;
    public void launch(int n) {
        term.setUser(this);
        if(servers.size()>0) {
            return;
        }
        term.write("Launching Servers...");
        for(int i=0; i<n; i++) {
            Server s = new Server();
            s.start();
            servers.add(s);
        }
        term.write("Done Launching Servers");
        new Thread() {
            public void run() {
                monitor();
            }
        }.start();
    }

    public void monitor() {
        term.write("Making display...");
        DataDisplay d = new DataDisplay(servers.size());
        term.write("Done making display");
        while(going) {
            for(int i=0; i<servers.size(); i++) {
                d.setText(i,i+"  "+servers.get(i).getAddress()+"  "+servers.get(i).getUserCount());
            }
            try{
                Thread.sleep(100);
            }catch(Exception e) {
                
            }
        }
    }
    
    public void inputText(String i) {
        if(i.equals("/stop")) {
            term.write("Stopping Servers...");
            for(int c=0; c<servers.size(); c++) {
                servers.get(c).stop();
            }
            term.write("Stopped");
        }
        term.write("Waiting...");
        try{
            Thread.sleep(1000);
        }catch(Exception e) {
            
        }
        term.write("Done");
    }
    
    public void inputObject(Object o) {
        
    }
}