package robot.parsable;

import RobotCLI.WebServer.JSONStringBuilder;

public class ParsableDouble extends Parsable implements JSONPrintable {

    double value;

    public ParsableDouble(String key, double value) {
        super(key);
        this.value = value;
    }

    public void jsonPrint(String name, JSONStringBuilder response) {
        response.append(name, (double)(((int) (value * 100000)) * 0.00001));
    }

    public double get() {
        return value;
    }

    public void parse(String str) {
        this.value = Double.parseDouble(str);
    }

    public String toString() {
        return "\"" + name + "\":" + value;
    }
}