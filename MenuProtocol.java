public class MenuProtocol extends Protocol
{
    Player user;
    public void MenuProtocol() {
        
    }
    
    public void inputText(String i) {
        if(i.equals("/help")) {
            
        }
    }

    public void setUser(Player p) {
        user=p;
        intro();
    }
    
    public void intro() {
        user.send("Welcome!");
        user.send("use /help for a list of commands");
    }
}