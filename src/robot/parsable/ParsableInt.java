package robot.parsable;

public class ParsableInt extends Parsable implements Parsable.JSONPrintable {
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
        this.value = Integer.parseInt(str);
    }

    public String toString() {
        return "\"" + name + "\":" + value;
    }
}