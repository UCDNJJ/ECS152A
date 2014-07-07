/**
 * @author Nelson Johansen, 997799699
 */
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class Project1ECS152a {
    /**
     * Global Variables
     */
    //max length of queue
    private static int MAXBUFFER;
    //current length of the queue
    private static int length;
    //number of packets dropped
    private static int dropped;
    //total server working time
    private static double serverBusy;
    //current time set by event time
    private static double time;
    //time of previous event used for riemann sum
    private static double prevTime;
    //riemann sum for area under curve
    private static double riemannSum;
    //Service rate
    private static double mu;
    //Arrival rate
    private static double lambda;
    //Rate variable for Negative Exponential Distribution
    private static double rate;
    
    public static double negative_exponentially_diistributed_time(double rate) {
        double u = Math.random();
        return ((-1/rate) * Math.log(1-u));
    }
    
    public static void main(String[] args) {       
        //Get command line arguments
        try {
            MAXBUFFER = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Arg" + args[0] + "must be an integer.");
            System.exit(1);
        }
        
        length = 0; time = 0; dropped = 0; serverBusy = 0; prevTime = 0;
        riemannSum = 0; DNode event;
        
        //mu = negative_exponentially_diistributed_time(rate);
        //Set to 1 as stated in the Experimentation section
        mu = 1;
        lambda = rate = 0.1;
        
        DoublyLinkedList list = new DoublyLinkedList();
        CircularFifoQueue<DNode> Q = new CircularFifoQueue<DNode>(MAXBUFFER);
        
        //First event to go into GEL
        DNode first = new DNode();
        first.eventTime = time + negative_exponentially_diistributed_time(lambda);
        first.eventServiceTime = negative_exponentially_diistributed_time(mu);
        serverBusy = serverBusy + first.eventServiceTime;
        first.eventType = 'A';
        
        list.add(first);
        
        //Main code
        for(int i = 0; i < 100000; i++) {

            //Remove the first event from the GEL
            if(!list.isEmpty()) {
                event = list.remove();
                //System.out.println(event);
            }
            else {
                System.err.println("No more elements in GEL to remove");
                break;
            }
            
            //Set current time to be event time
            time = event.eventTime;

            //riemann sum computation
            if(i == 0)
            {
                prevTime = event.eventTime;
            } else {
                riemannSum = riemannSum + (length * (time - prevTime));
            }
                        
            if(event.eventType == 'A') {
                //put the event in Queue to be processed by server.
                
                /*
                * Since we generate one arriave at a time, we first schedule the
                * next arrival event.
                */
            
                //creation of new packet 
                DNode nextEvent = new DNode();
                nextEvent.eventTime = time + negative_exponentially_diistributed_time(lambda);
                nextEvent.eventServiceTime = negative_exponentially_diistributed_time(mu);
                serverBusy = serverBusy + nextEvent.eventServiceTime;
                nextEvent.eventType = 'A';
            
                //add new packet to GEL in sorted order!
                list.add(nextEvent);
                
                /*
                * Process the arrival event
                */
                
                if(length == 0)
                {
                    //queue is currently empty therefore the
                    //packet can be immediatly scheduled for transmission.
                    length++;
                    
                    //create a new depature event
                    DNode departure = new DNode();
                    departure.eventTime = event.eventServiceTime + time;
                    departure.eventType = 'D';
                    
                    //put departure event in GEL
                    list.add(departure);
                } else {
                    //queue has one or more elements in it.
                    //the minus one on the length is to account for the 
                    //packet that is currently in the processor.
                    if(length - 1 < MAXBUFFER)
                    {
                        //put packet in queue.
                        Q.add(event);
                        length++;
                    } else {
                        //queue is full and the packet will now be dropped
                        dropped++;
                    }
                }
                
                //update statistics that maintain the mean queue length and the
                //server busy time are done above.
                
            } else if(event.eventType == 'D') {
                
                //update statistics that maintain the mean queue length and the
                //server busy time.
                
                length--;
                if(length == 0) {
                    //if the queue is empty we do nothing.
                    
                } else {
                    //queue is not full, dequeue the first packet.
                    DNode leave = Q.remove();
                    
                    //Create new departure event, instead of making a new node
                    //use the current node;
                    leave.eventTime = time + leave.eventServiceTime;
                    leave.eventServiceTime = 0;
                    leave.eventType = 'D';
                    
                    //put new departure event in GEL
                    list.add(leave);
                }
            } else {
                //This should never happen but in case...
                System.err.println("event was neither a Departure or Arrival");
            } 
            
            if(i != 0)
            {
                prevTime = event.eventTime;
            }
        }//end main for loop
        
        //Statistics
        System.out.println(time);
        System.out.println(riemannSum/time);
        System.out.println(dropped);
        System.out.println(serverBusy);
        System.out.println((serverBusy/time)*100 + "% utilization of server.");
        
    }//end main
    
}//end program
