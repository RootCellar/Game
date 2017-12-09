import java.util.ArrayList;
public class Match
{
    private User one;
    private User two;
    private ArrayList<Creature> ones = new ArrayList<Creature>();
    private ArrayList<Creature> twos = new ArrayList<Creature>();
    private User turn;
    private double lifeone=1000;
    private double lifetwo=1000;
    public boolean going=true;
    
    public Match(User o, User t) {
        one=o;
        two=t;
        one.playing(true);
        two.playing(true);
        turn=one;
        one.setHandler(new UserGameHandler(o,this));
        two.setHandler(new UserGameHandler(t,this));
        one.send("Game Started!");
        two.send("Game Started!");
        turn.send("This game will start off with your turn");
    }
    
    public void sendBoth(String s) {
        one.send(s);
        two.send(s);
    }
    
    public void end() {
        sendBoth("Game has ended.");
        one.setHandler(new JoinMenu());
        two.setHandler(new JoinMenu());
        going=false;
    }
    
    private void win(User u) {
        u.send("You have won the game!");
        getOtherUser(u).send("You have lost the game!");
        end();
    }
    
    public void winCon() {
        if(lifeone<=0) {
            win(two);
        }
        if(lifetwo<=0) {
            win(one);
        }
    }
    
    public void turn() {
        winCon();
        turn = getOtherUser(turn);
    }
        
    public void inputText(User u, String s) {
        if(!turn.equals(u)) {
            u.send("Not your turn!");
            return;
        }
        User other = getOtherUser(u);
        ArrayList<Creature> users = getCreatures(u);
        ArrayList<Creature> others = getOtherCreatures(u);
        if(Command.is("/help",s)) {
            u.send("/summon <level> - summon a monster");
            u.send("/say <message> - send a message to your opponent");
            u.send("/attack <num1> <num2> - make your num1 monster attack your opponent's num2 monster");
            u.send("/list - lists monsters and life");
            u.send("/end - end your turn");
            u.send("/quit - end the match");
        }
        
        if(Command.is("/say",s)) {
            u.send("You say "+s.substring(5));
            other.send("Your opponent says "+s.substring(5));
        }
        if(s.equals("/quit")) {
            end();
        }
    }
    
    public User getOtherUser(User u) {
        if(u.equals(one)) return two;
        else return one;
    }
    
    public ArrayList<Creature> getCreatures(User u) {
        if(u.equals(one)) return ones;
        else return twos;
    }
    
    public ArrayList<Creature> getOtherCreatures(User u) {
        if(!u.equals(one)) return ones;
        else return twos;
    }
}