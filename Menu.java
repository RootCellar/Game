public class Menu implements UserHandler
{
    public User user;
    public Server server;
    public void setUser(User u) {
        user=u;
        server = u.getServer();
        intro();
    }
    
    public void inputText(String i) {
        
    }
    
    public void inputObject(Object o) {
        
    }
    
    public void intro() {
        
    }
}