import java.io.*;
import java.util.Date;
public class Logger
{
    private File f;
    private FileWriter toLog;
    public boolean canWrite=true;
    public Logger() {
        //open();
    }
    
    /**
     * Close and reopen the log file
     */
    public void reopen() {
        close();
        open();
    }
    
    /**
     * Opens a log file
     * The name of the file is the current date
     */
    public void open() {
        new File("Logs").mkdir();
        f = new File( "Logs/" + getNameByDate( new Date() ) + ".txt" );
        try{
            toLog = new FileWriter(f,true);
        }catch(Exception e) {
            canWrite=false;
        }
        log("Opened Log!");
    }

    public void close() {
        try{
            toLog.close();
        }catch(Exception e) {

        }
    }

    /**
     * Logs a message and writes a new line
     */
    public void log(String s) {
        try{
            toLog.write(getTimeAsString( new Date() )+" "+s);
            toLog.write(System.getProperty("line.separator"));
            toLog.flush();
        }catch(Exception e) {
            canWrite=false;
        }
    }

    public static String getNameByDate(Date date) {
        return (date.getMonth()+1)+"-"+date.getDate()+"-"+(date.getYear()+1900);
    }

    public static String getTimeAsString(Date date) {
        String ind = "AM";
        int h = date.getHours();
        if(h>12) {
            h-=12;
            ind= "PM";
        }
        
        int m2 = date.getMinutes();
        String m = m2+"";
        if(m2<10) m="0"+m2;
        
        int s2 = date.getSeconds();
        String s = s2+"";
        if(s2<10) s="0"+s2;
        
        return (date.getMonth()+1)+"/"+date.getDate()+"/"+(date.getYear()+1900)+" "+h+":"+m+":"+s+" "+ind;
    }
}