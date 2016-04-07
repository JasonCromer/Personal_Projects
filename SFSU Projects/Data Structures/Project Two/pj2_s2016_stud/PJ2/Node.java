/**
    Do not modify Node class
*/

package PJ2;

class Node<T>
{
      private T       data; // entry in stack
      private Node<T> next; // link to next node

      Node(T aData)
      {
         this(aData, null);
      } // end constructor
      
      Node(T aData, Node<T> nextNode)
      {
         data = aData;
         next = nextNode;
      } // end constructor
      
      T getData()
      {
         return data;
      } // end getData
      
      void setData(T aData)
      {
         data = aData;
      } // end setData
      
      Node<T> getNextNode()
      {
         return next;
      } // end getNextNode
      
      void setNextNode(Node<T> nextNode)
      {
         next = nextNode;
      } // end setNextNode
} // end Node


