import java.util.ArrayList;
public class Match
{
    private User one;
    private User two;
    private ArrayList<Creature> ones = new ArrayList<Creature>();
    private ArrayList<Creature> twos = new ArrayList<Creature>();
    private User turn;
    private double lifeone = MAX_LIFE;
    private double lifetwo = MAX_LIFE;
    private int powone = 100;
    private int powtwo = 100;
    public boolean going = true;
    public static int MAX_CREATURES = 20;
    public static int MAX_LEVEL = 100;
    public static double LIFE_ARMOR = 1000;
    public static double MAX_LIFE = 1000;
    public static int MAX_POWER = 1000;
    public static int POW_GAIN = 20;

    public Match(User o, User t) {
        one=o;
        two=t;
        one.playing(true);
        two.playing(true);
        turn=one;
        one.setHandler(new UserGameHandler(o,this));
        two.setHandler(new UserGameHandler(t,this));
        sendBoth("Game Started!");
        turn.send("This game will start off with your turn");
    }

    public void sendBoth(String s) {
        one.send(s);
        two.send(s);
    }

    public void end() {
        sendBoth("Game has ended.");
        resetUsers();

        going=false;
    }

    private void resetUsers() {
        one.playing(false);
        two.playing(false);
        MainMenu m = new MainMenu();
        m.setUser(one);
        one.setHandler(m);
        m = new MainMenu();
        m.setUser(two);
        two.setHandler(m);
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
        if(turn.equals(two)) {
            powone+=POW_GAIN;
            powtwo+=POW_GAIN;
        }
        if(powone>=MAX_POWER) powone=MAX_POWER;
        if(powtwo>=MAX_POWER) powtwo=MAX_POWER;

        winCon();
        turn.send("Your turn has ended");
        turn = getOtherUser(turn);
        turn.send("It is now your turn");
    }

    public void inputText(User u, String s) {
        if(u.getServer().isHalted()) {
            u.send("Server is halted, command rejected");
            return;
        }
        
        User other = getOtherUser(u);
        ArrayList<Creature> users = getCreatures(u);
        ArrayList<Creature> others = getOtherCreatures(u);

        if(Command.is("/help",s)) {
            u.send("/summon <level> - summon a monster");
            u.send("/say <message> - send a message to your opponent");
            u.send("/attack <num1> <num2> - make your num1 monster attack your opponent's num2 monster. If opponent has no monsters, num2 is not needed.");
            u.send("/list - lists monsters and life");
            u.send("/end - end your turn");
            u.send("/quit - end the match");
            return;
        }

        if(Command.is("/say",s)) {
            u.send("You say \""+s.substring(5)+"\"");
            other.send("Your opponent, "+u.getName()+", says \""+s.substring(5)+"\"");
            return;
        }

        if(s.equals("/list")) {
            try{
                String use = "";
                for(int i=0; i<MAX_CREATURES; i++) {
                    use=i+". ";
                    if(users.size()>i) use+=users.get(i);
                    else use+="No Monster";
                    use+="\t\t";
                    if(others.size()>i) use+=others.get(i);
                    else use+="No monster";
                    u.send(use);
                }

                u.send(getLife(u)+"/"+MAX_LIFE+"\t\t"+getLife(other)+"/"+MAX_LIFE);
                u.send(getPow(u)+"\t\t"+getPow(other));
            }catch(Exception e) {
                e.printStackTrace();
            }

            return;

        }

        if(s.equals("/quit")) {
            other.send("Your opponent has quit.");
            end();
            return;
        }

        if(!turn.equals(u)) {
            u.send("Not your turn!");
            return;
        }

        if(Command.is("/end",s)) {
            turn();
        }

        if(Command.is("/summon",s)) {
            ArrayList<String> args = Command.getArgs(s);
            if(args.size()<2) {
                u.send("Not enough arguments (integer level)");
                return;
            }
            int lvl;

            try{
                lvl = Integer.parseInt(args.get(1));
            }catch(Exception e) {
                u.send("That is not an integer");
                return;
            }

            if(lvl>MAX_LEVEL || lvl<1) {
                u.send("That level is too high or invalid!");
                return;
            }

            if(getPow(u)<lvl) {
                u.send("Not enough power! You have "+getPow(u)+", but you need "+lvl);
                return;
            }

            if(users.size()>=MAX_CREATURES) {
                u.send("You have too many creatures!");
                return;
            }

            users.add(new Creature(lvl));
            usePow(u, lvl);
            u.send("You summoned a level "+lvl+" creature!");
            other.send("Your opponent summoned a level "+lvl+" creature!");

        }

        if(Command.is("/attack",s)) {
            ArrayList<String> args = Command.getArgs(s);

            if(args.size()<2) {
                u.send("Not enough arguments!");
                return;
            }

            if(args.size()==2 && others.size()>0) {
                u.send("You can't attack directly, your opponent still has creatures!");
                return;
            }

            int w;
            try{
                w = Integer.parseInt(args.get(1));
            }catch(Exception e) {
                u.send("Please enter an integer!");
                return;
            }
            
            if(getPow(u)<users.get(w).level) {
                u.send("You don't have enough power! (Power must equal the level of the creature)");
                return;
            }
            

            if(w>users.size() || w<0) {
                u.send("Invalid creature!");
                return;
            }

            if(others.size()==0) {
                double d = calcDamage(users.get(w).atk, LIFE_ARMOR);

                usePow(u,users.get(w).level);
                damagePlayer(other,d);
                winCon();
                return;
            }

            int w2;
            try{
                w2 = Integer.parseInt(args.get(2));
            }catch(Exception e) {
                u.send("Please enter an integer!");
                return;
            }

            if(w2<0 || w2>others.size()) {
                u.send("Invalid target");
                return;
            }

            usePow(u,users.get(w).level);
            damage(users.get(w), others.get(w2), u, other);

        }


        //System.out.println(Command.getArgs(s));
    }

    public void usePow(User u, int p) {
        if(u.equals(one)) {
            powone-=p;
            u.send("You have used "+p+" power");
        }
        if(u.equals(two)) {
            powtwo-=p;
            u.send("You have used "+p+" power");
        }
    }

    public void remCreature(User u, Creature c) {
        for(int i=0; i<getCreatures(u).size(); i++) {
            if(getCreatures(u).get(i).equals(c)) {
                getCreatures(u).remove(i);
            }
        }
    }

    public void damage(Creature attacker, Creature victim, User atk, User def) {
        double d = calcDamage(attacker.atk, victim.def);
        victim.hp-=d;
        atk.send("Your creature did "+d+" damage to target monster!");
        def.send("Enemy creature did "+d+" damage to your monster!");
        if(victim.hp<=0) {
            remCreature(def, victim);
            atk.send("You killed an enemy creature!");
            def.send("Your enemy killed one of your creatures!");
        }
    }

    public void damagePlayer(User u, double damage) {
        if(u.equals(one)) {
            lifeone-=damage;
            one.send("You have taken "+damage+" damage!");
            two.send("You have dealt "+damage+" damage!");
        }
        if(u.equals(two)) {
            lifetwo-=damage;
            two.send("You have taken "+damage+" damage!");
            one.send("You have dealt "+damage+ "damage!");
        }
    }

    public static double calcDamage(double damage, double armor) {
        double mult = damage/armor;
        damage*=mult;
        return damage;
    }

    public int getPow(User u) {
        if(u.equals(one)) return powone;
        else return powtwo;
    }

    public double getLife(User u) {
        if(u.equals(one)) return lifeone;
        else return lifetwo;
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