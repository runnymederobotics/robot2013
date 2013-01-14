package templates;

import RobotCLI.WebServer;
import java.util.Enumeration;
import java.util.Hashtable;

public abstract class Parsable {
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
            Enumeration keys = params.keys();
            while(keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                Parsable parsable = Parsable.getParsable(key.toLowerCase());
                if(parsable != null) {
                    parsable.parse((String)params.get(key));
                    if(key.startsWith("pid")) {
                        ParsablePIDController parsablePIDController = (ParsablePIDController)ParsablePIDController.parsablePIDControllers.get(key);
                        parsablePIDController.updatePID();
                    }
                }
            }
            
            String ret = "{";
            
            Enumeration parsables = Parsable.parsables.elements();
            boolean firstRun = true;
            while(parsables.hasMoreElements()) {
                Parsable parsable = (Parsable)parsables.nextElement();
                if(!firstRun) {
                    ret += ",";
                }
                ret += parsable.toString();
                firstRun = false;
            }
            ret += "}";
            return ret;
        }
    }
}

