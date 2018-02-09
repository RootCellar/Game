import java.util.ArrayList;
import java.io.*;
public class SaveFile
{
    public ArrayList<Parameter> params = new ArrayList<Parameter>();
    public File file;
    public boolean canUse = true;
    public void SaveFile(String path) {
        File file = new File(path);
        if(!file.canRead()) {
            try{
                file.createNewFile();
            }catch(Exception e) {
                canUse=false;
            }
        }
    }
    
    
}