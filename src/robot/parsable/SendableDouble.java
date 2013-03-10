package robot.parsable;

import RobotCLI.WebServer.JSONStringBuilder;
import java.util.Hashtable;

public class SendableDouble implements JSONPrintable {

    public static Hashtable sendableDoubles = new Hashtable();
    String name;
    double value;

    public SendableDouble(String name, double value) {
        this.name = name;
        this.value = value;
        sendableDoubles.put(name, this);
    }

    public void jsonPrint(String name, JSONStringBuilder response) {
        response.append(name, value);
    }

    public void set(double value) {
        this.value = value;
    }

    public double get() {
        return value;
    }
}
