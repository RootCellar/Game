/**
 * The handler for users after they join the server
 */

public class MainMenu extends Menu implements UserHandler
{
    //User user;
    //Server server;
    
    public void inputText(String i) {
        if(i.equals("/help")) {
            user.send("/joingame - join the game");
            user.send("/quit - disconnect");
            user.send("/setname <string> - set your name");
            user.send("/say <string> - chat, send a message");
        }
        
        if(i.equals("/quit")) {
            user.send("Bye!");
            user.disconnect();
        }
        
        if(i.equals("/joingame")) {
            //server.putInMatchMaker(user);
        }
        
        if(Command.is("/setname",i)) {
            if(i.length()>30) {
                user.send("That name is too long (Max 30 chars)");
                return;
            }
            user.setName("GUEST - "+i.substring(9));
            user.send("Name set to "+i.substring(9));
        }
        
        if(Command.is("/say",i)) {
            server.sendAll(user,i.substring(5));
        }
    }
    
    public void intro() {
        user.send("Welcome!");
        user.send("Use /help for a list of commands");
    }
}