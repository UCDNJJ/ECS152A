/**
 *
 * @author NelsonJ
 */

//Basic implementation of a DLL with only the functions required for simulation.

public class DoublyLinkedList {
    
    private int N = 0;    // number of elements on list
    private Node pre;     // sentinel before first item
    private Node post;    // sentinel after last item

    public DoublyLinkedList() {
        pre  = new Node();
        post = new Node();
        //connect intial DLL
        pre.next = post;
        post.prev = pre;
    }

    //linked list node helper data type
    public class Node {
        private DNode item = null;
        private Node next = null;
        private Node prev = null;
    }

    public boolean isEmpty()    { return N == 0; }
    public int size()           { return N;      }
        
    //Add the item to the list
    public void add(DNode item) {
        Node node = pre.next;            
        //Handle the very first node
        if(node == post) {
            Node add = new Node();
            add.item = item;
            add.next = post;
            add.prev = pre;
            post.prev = add;
            pre.next = add;
            N++;
            return;
        }
            
        //Handle insertion of nodes in the correct sorted order based
        //on the eventTime that is associated with the node.
        if(item.eventTime < node.item.eventTime) {
            addHelp(node, item);          
        } else {
        //search for the correct spot to insert node
            while(node != post && item.eventTime > node.item.eventTime)
            {
                node = node.next;
            }               
            addHelp(node, item);
        }            
    }//end add
        
    //helper function
    public void addHelp(Node node, DNode item) {
        Node last = node.prev;
        Node add = new Node();
        add.item = item;
        add.next = node;
        add.prev = last;
        node.prev = add;
        last.next = add;
        N++;
    }
        
    //remove an element from the first element in the DLL
    public DNode remove() {
        Node node = pre.next;
        Node next = node.next;     
        pre.next = next;
        next.prev = pre;
        N--;
            
        return node.item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        Node x = pre.next;
        for(int i = 0; i < N; i++)
        {
            s.append(x.item + System.getProperty("line.separator"));
            x = x.next;                
        }           
        return s.toString();
    } 
    
}//end DLL
