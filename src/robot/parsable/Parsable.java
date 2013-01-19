package robot.parsable;

import RobotCLI.WebServer;
import java.util.Enumeration;
import java.util.Hashtable;

public abstract class Parsable {
    
    public interface JSONPrintable {
        public void jsonPrint(String name, JSONStringBuilder response);
    }
    
    public static Hashtable parsables = new Hashtable();
    
    String name;
    
    public Parsable(String key) {
        this.name = key.toLowerCase();
        
        if(!parsables.contains(key)) {
            Parsable.parsables.put(name, this);
        }
    }
    
    public static Parsable getParsable(String name) {
        return (Parsable)parsables.get(name);
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String toString();
    
    public abstract void parse(String str);
    
    public static class ParsablesHandler implements WebServer.Handler {
        public String handle(Hashtable params) {
            //This handles value changing using the params
            Enumeration keys = params.keys();
            //For each variable specified in the params
            while(keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                //Find the constant associated with the name
                Parsable parsable = Parsable.getParsable(key.toLowerCase());
                //If we found one
                if(parsable != null) {
                    //Change the value of the constant
                    parsable.parse((String)params.get(key));
                    if(key.startsWith("pid")) {
                        ParsablePIDController parsablePIDController = (ParsablePIDController)ParsablePIDController.parsablePIDControllers.get(key);
                        parsablePIDController.updatePID();
                    }
                }
            }
            
            //This lists all constants
            String ret = "{";
            Enumeration parsables = Parsable.parsables.elements();
            boolean firstRun = true;
            //For each constant ("parsable")
            while(parsables.hasMoreElements()) {
                Parsable parsable = (Parsable)parsables.nextElement();
                if(!firstRun) {
                    ret += ",";
                }
                //Add the parsable to the list
                ret += parsable.toString();
                firstRun = false;
            }
            ret += "}";
            //The "{", ",", and "}" are used for easier parsing later on from our GUI
            return ret;
        }
    }
}

