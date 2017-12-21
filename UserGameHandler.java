public class UserGameHandler extends UserHandler
{
    private User user;
    private Match match;
    public UserGameHandler(User u, Match m) {
        user = u;
        match = m;
        u.send("Welcome to the game. Type /help for a list of commands. Have Fun!");
    }

    public void inputText(String s) {
        match.inputText(user,s);
    }
}