package robot;

import RobotCLI.WebServer.Handler;
import RobotCLI.WebServer.JSONStringBuilder;
import RobotCLI.WebServer.Streamer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import robot.parsable.JSONPrintable;

public class StreamerHandler extends Streamer {

    public static Hashtable allVariables = new Hashtable();
    final Hashtable streamVariables = new Hashtable();

    // /stream/list
    // /stream/select?leftPid&rightPid&shooterPid
    // /stream/deselect?shooterPid&rightPid
    // /stream?frequency=2
    protected void buildChunk(JSONStringBuilder response, Hashtable params) {
        synchronized (streamVariables) {
            Enumeration keys = streamVariables.keys();
            //For each variable specified in the params
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                ((JSONPrintable) streamVariables.get(key)).jsonPrint(key, response);
            }
        }
    }

    public static void addVariable(String name, JSONPrintable value) {
        allVariables.put(name, value);
    }

    public Handler getListHandler() {
        return new ListHandler();
    }

    public Handler getSelectHandler() {
        return new SelectHandler();
    }

    public Handler getDeselectHandler() {
        return new DeselectHandler();
    }

    private static String listVariables(Hashtable variables) {
        Enumeration keys = variables.keys();
        StringBuffer builder = new StringBuffer();
        builder.append("{\"vars\":[");
        boolean needComma = false;
        while (keys.hasMoreElements()) {
            if (needComma) {
                builder.append(",");
            }
            String key = (String) keys.nextElement();
            builder.append("\"");
            builder.append(key);
            builder.append("\"");
            needComma = true;
        }
        builder.append("]}");
        return builder.toString();
    }

    private class ListHandler implements Handler {

        public String handle(Hashtable params) {
            return listVariables(allVariables);
        }
    }

    private class SelectHandler implements Handler {

        public String handle(Hashtable params) {
            synchronized (streamVariables) {
                Enumeration keys = params.keys();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    JSONPrintable printable = (JSONPrintable) allVariables.get(key);
                    if (printable != null && (streamVariables.get(key) == null)) {
                        streamVariables.put(key, printable);
                    }
                }
                return listVariables(streamVariables);
            }
        }
    }

    private class DeselectHandler implements Handler {

        public String handle(Hashtable params) {
            synchronized (streamVariables) {
                Enumeration keys = params.keys();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    JSONPrintable printable = (JSONPrintable) allVariables.get(key);
                    if (printable != null && (streamVariables.get(key) != null)) {
                        streamVariables.remove(key);
                    }
                }
                return listVariables(streamVariables);
            }
        }
    }
}