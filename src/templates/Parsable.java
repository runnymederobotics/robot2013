package templates;

import java.util.Hashtable;

public abstract class Parsable {
    public static Hashtable parsables = new Hashtable();
    
    String name;
    
    public Parsable(String key) {
        this.name = key.toLowerCase();
        
        if(!parsables.contains(key)) {
            Parsable.parsables.put(name, this);
        }
    }
    
    public static Parsable getParsable(String name) {
        return (Parsable)parsables.get(name);
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String toString();
    
    public abstract void parse(String str);
}

