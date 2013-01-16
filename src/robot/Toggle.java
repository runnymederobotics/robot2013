package robot;

public class Toggle {
    boolean state;
    boolean lastToggle;
    
    public Toggle(boolean defaultState) {
        this.state = defaultState;
        lastToggle = false;
    }
    
    public boolean update(boolean toggle) {
        //You are pressing the button currently, and you weren't pressing the button before
        if(toggle && !lastToggle) {
            //Change states
            state = !state;
        }
        
        //Current toggleValue becomes lastToggleValue
        lastToggle = toggle;
        
        return state;
    }
    
    public boolean get() {
        return state;
    }
    
    public void set(boolean state) {
        this.state = state;
    }
}
