package robot.parsable;

import RobotCLI.WebServer.JSONStringBuilder;
import java.util.Hashtable;

public class SendableInt implements JSONPrintable {

    public static Hashtable sendableInts = new Hashtable();
    String name;
    int value;

    public SendableInt(String name, int value) {
        this.name = name;
        this.value = value;
        sendableInts.put(name, this);
    }

    public void jsonPrint(String name, JSONStringBuilder response) {
        response.append(name, value);
    }

    public void set(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }
}
