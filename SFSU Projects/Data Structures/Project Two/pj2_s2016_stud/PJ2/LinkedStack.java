/**
    A class of stacks whose entries are stored in a chain of nodes.
    Implement all methods in LinkedStack class using the given 
    Node class. 
    Main Reference : text book or class notes
    Do not change or add data fields
*/

package PJ2;

public class LinkedStack<T> implements StackInterface<T>
{
   
   // Data fields
   private Node<T> topNode; // references the first node in the chain
   private int count;  	    // number of data in this stack
  
   public LinkedStack()
   {
      // add stataments
      topNode = null;
      count=0;
   } // end default constructor
  
   public void push(T newData)
   {
      // add stataments
      Node<T> newNode = new Node<T>(newData, topNode);
      topNode = newNode;
      count++;
   } // end push

   public T peek()
   {
      // add stataments
      T top = null;
  
      if (topNode != null)
         top = topNode.getData();
    
      return top;
   } // end peek

   public T pop()
   {
      // add stataments
      T top = peek();
  
      if (topNode != null)
      {
         topNode = topNode.getNextNode();
         count--;
      }
    
      return top;
   } // end pop

   public boolean empty()
   {
      // add stataments
      return topNode == null;
   } // end empty

   public int size()
   {
      // add stataments
      return count;
   } // end isEmpty

   public void clear()
   {
      // add stataments
      topNode = null;
      count=0;
   } // end clear

   public String toString()
   {
      // add stataments
      // note: data class in stack must implement toString() method
      //       return a list of data in Stack, separate them with ','
      String result = "[";
      Node<T> currentNode=topNode; // references the first node in the chain
      while (currentNode != null) {
	result = result + currentNode.getData() + ",";
	currentNode = currentNode.getNextNode();
      }
      result = result + "]";
      return result;
   }


   

   /****************************************************
      Do not modify: Stack test

      Expected output:

        OK: stack is empty
        Push 3 data: 10, 30, 50
        Print stack [50,30,10,]
        OK: stack size is 3
        OK: peek stack top is 50
        OK: stack is not empty
        Pop 2 data: 50, 30
        Print stack [30,10,]
        Print stack [10,]
        OK: stack pop data is 30
        Clear stack
        Print stack []

   ******************************************************/

   public static void main (String args[])
   {
	LinkedStack<Integer> s = new LinkedStack<Integer>();
	if (s.empty()) 
            System.out.println("OK: stack is empty");
	else
            System.out.println("Error: stack is not empty");

	s.push(10);
	s.push(30);
	s.push(50);
        System.out.println("Push 3 data: 10, 30, 50");
        System.out.println("Print stack " + s);

	if (s.size() == 3) 
            System.out.println("OK: stack size is 3");
	else
            System.out.println("Error: stack size is " + s.size());

	if (s.peek() == 50) 
            System.out.println("OK: peek stack top is 50");
	else
            System.out.println("Error: peek stack top is " + s.size());

	if (!s.empty()) 
            System.out.println("OK: stack is not empty");
	else
            System.out.println("Error: stack is empty");

        System.out.println("Pop 2 data: 50, 30");
        s.pop();
        System.out.println("Print stack " + s);
	int data=s.pop();
        System.out.println("Print stack " + s);
	if (data == 30) 
            System.out.println("OK: stack pop data is 30");
	else
            System.out.println("Error: stack pop data is " + data);

        System.out.println("Clear stack");
        s.clear();
        System.out.println("Print stack " + s);
   }

} // end Stack
