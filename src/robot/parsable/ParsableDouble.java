package robot.parsable;

import RobotCLI.WebServer.JSONStringBuilder;

public class ParsableDouble extends Parsable implements JSONPrintable {

    double value;

    public ParsableDouble(String key, double value) {
        super(key);
        this.value = value;
    }

    public void jsonPrint(String name, JSONStringBuilder response) {
        response.append(name, (double) (((int) (value * 1000)) * 0.001)); //Format to 3 decimal places
    }

    public double get() {
        return value;
    }
    
    public void set(double value) {
        this.value = value;
    }

    public void parse(String str) {
        try {
            this.value = Double.parseDouble(str);
        } catch (Exception ex) {
        }
    }

    public String toString() {
        return "\"" + name + "\":" + value;
    }
}