package robot;

public class ParsableDouble extends Parsable {
    double value;

    public ParsableDouble(String key, double value) {
        super(key);
        this.value = value;
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