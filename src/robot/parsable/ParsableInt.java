package robot.parsable;

import RobotCLI.WebServer.JSONStringBuilder;

public class ParsableInt extends Parsable implements JSONPrintable {

    int value;

    public ParsableInt(String key, int value) {
        super(key);
        this.value = value;
    }

    public void jsonPrint(String name, JSONStringBuilder response) {
        response.append(name, value);
    }

    public int get() {
        return value;
    }

    public void parse(String str) {
        try {
            this.value = Integer.parseInt(str);
        } catch (Exception ex) {
        }
    }

    public String toString() {
        return "\"" + name + "\":" + value;
    }
}