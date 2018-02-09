
import java.util.ArrayList;
public class MatchMaker
{
    public ArrayList<User> users = new ArrayList<User>();
    public Server server;
    public MatchMaker(Server s) {
        server = s;
    }
    
    public int getQueueSize() {
        return users.size();
    }

    public void run() {
        try{
            clearUnneeded();
            if(users.size()>=2) {
                //server.addMatch(new Match(users.get(0), users.get(1) ) );
                users.remove(0);
                users.remove(0);
                server.out("Created a match!");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void clearUnneeded() {
        for(int i=0; i<users.size(); i++) {
            if(!users.get(i).isConnected()) {
                users.remove(i);
            }
        }
    }

    public void addUser(User u) {
        try{
            if(users.size()<1000) {
                remUser(u);
                users.add(u);
                u.send("Added to matchmaker");
            }
            else u.send("Can't enter MatchMaker: MatchMaker is full");
        }catch(Exception e) {

        }
    }

    public void remUser(User u) {
        for(int i=0; i<users.size(); i++) {
            if(users.get(i).equals(u)) {
                users.remove(i);
            }
        }
    }
}