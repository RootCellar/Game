import java.io.*;
import java.util.Date;
public class Logger
{
    private File f;
    private FileWriter toLog;
    public boolean canWrite=true;
    public Logger() {
        new File("Logs").mkdir();
        f = new File( "Logs/" + getNameByDate( new Date() ) + ".txt" );
        try{
            toLog = new FileWriter(f);
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
        return date.getMonth()+"-"+date.getDate()+"-"+(date.getYear()+1900);
    }

    public static String getTimeAsString(Date date) {
        return date.getMonth()+"/"+date.getDate()+"/"+(date.getYear()+1900)+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    }
}