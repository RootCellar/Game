public class Command
{
    public static String[] getArgs(String s) {
        int spaces=0;
        for(int i=0; i<s.length(); i++) {
            if(s.substring(i, i+1).equals(" ")) spaces++; 

        }
        String[] s2 = new String[spaces];
        for(int i=0; i<spaces; i++) {
            s2[i]=s.substring(0,s.indexOf(" "));
            s=s.substring(s.indexOf(" "));
        }
        return s2;
    }
    
    public static boolean is(String c, String e) {
        if(e.length()<c.length()) return false;
        
        if(e.substring(0,c.length()).equals(c)) {
            return true;
        }
        
        return false;
    }
}