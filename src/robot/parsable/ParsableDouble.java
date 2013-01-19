package robot.parsable;

public class ParsableDouble extends Parsable implements Parsable.JSONPrintable {
    double value;

    public ParsableDouble(String key, double value) {
        super(key);
        this.value = value;
    }
    
    public void jsonPrint(String name, JSONStringBuilder response) {
        response.append(name, value);
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