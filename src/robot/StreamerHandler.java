package robot;

import RobotCLI.WebServer.JSONStringBuilder;
import RobotCLI.WebServer.Streamer;
import java.util.Enumeration;
import java.util.Hashtable;
import robot.parsable.JSONPrintable;

public class StreamerHandler extends Streamer {

    Hashtable variables = new Hashtable();
    
    protected void buildChunk(JSONStringBuilder response, Hashtable params) {          
        Enumeration keys = variables.keys();
        //For each variable specified in the params
        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            ((JSONPrintable)variables.get(key)).jsonPrint(key, response);
        }
    }
    
    public void addVariable(String name, JSONPrintable value) {
        variables.put(name, value);
    }
}