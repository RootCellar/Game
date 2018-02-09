import java.io.*;
public class Packet implements Serializable
{
    String name = ""+hashCode();
    int id = hashCode();
    
    public void setName(String s) {
        name=s;
    }
}