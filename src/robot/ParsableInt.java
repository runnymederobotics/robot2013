package robot;

public class ParsableInt extends Parsable {
    int value;

    public ParsableInt(String key, int value) {
        super(key);
        this.value = value;
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