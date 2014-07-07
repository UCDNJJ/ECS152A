/**
 *
 * @author NelsonJ
 */

import java.util.*;

//Data structure to be used with the DLL

public class DNode {
    public double eventTime;
    public double eventServiceTime;
    public char eventType;
    
    public DNode(){
        eventTime = 0;
        eventServiceTime = 0;
        eventType = 'X';
    }
    
    public String toString() {
        return "Event time is " + this.eventTime + " For event: " + this.eventType
                + " with service timme: " + this.eventServiceTime;
    }
    
    public int compareTo(DNode node) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        
        if (this == node) 
            return EQUAL;
        
        if (this.eventTime < node.eventTime) 
            return BEFORE;
        
        if (this.eventTime > node.eventTime) 
            return AFTER;
        
        return 0;
    }
}
