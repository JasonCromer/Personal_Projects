/*  This program is used to test PJ1.Fracition class
 *  More info are given in Readme file
 */

import java.util.*;
import PJ1.*;

class PJ1_Test
{
    static private Scanner scanner;

    private static SimpleFraction readFraction() {
        System.out.print( "\nTry to read a fraction x/y, please enter x y : " );
        int numerator = scanner.nextInt();                            
        int denominator = scanner.nextInt();                          
        SimpleFraction f = new SimpleFraction(numerator, denominator);
        System.out.println( "\t\tRead OK:"+f);
	return f;
    }

    private static void printOperations() {
	System.out.println("==============================================");
        System.out.println("\nOperations:");
        System.out.println("  0) exit \t1) add \t\t2) subtract \t3) multiply \t4) divide");
	System.out.println("  5) compareTo \t6) equals \t7) recipocal \t8) toDouble \t9) setSimpleFraction (x/y) ");  
        System.out.print( "\nEnter an operation number: ");
    }

    public static void main( String args[] )
    {
       	scanner = new Scanner( System.in ); // scanner for input
       	boolean continueLoop = true; // determines if more input is needed
	SimpleFraction n1=null;
	SimpleFraction n2=null;
	int op,x,y;
  
        do                                                                  
        {                                                                   
           try // read two numbers and calculate quotient                   
           {                                                                
    	      printOperations();
              op= scanner.nextInt();                            

	      if (op == 0) {
		break;
	      } else if ((op >0) && (op <7)) {
		n1 = readFraction();
		n2 = readFraction();
	      } else if ((op > 6) && (op < 9)) {
		n1 = readFraction();
              } else if (op == 9) {
                n1 = new SimpleFraction();
	      } else {
                  System.out.print( "\nInvalid input... try again\n" );
		  continue;
	      } 

	      System.out.println("\nTests:\n");
	      switch (op) {
            	case 1:  
			System.out.println("\t" + n1 + " + " + n1 + " = " + n1.add(n1));
            		System.out.println("\t" + n2 + " + " + n2 + " = " + n2.add(n2));
            		System.out.println("\t" + n1 + " + " + n2 + " = " + n1.add(n2));
            		System.out.println("\t" + n2 + " + " + n1 + " = " + n2.add(n1));
		        break;
            	case 2: 
			System.out.println("\t" + n1 + " - " + n1 + " = " + n1.subtract(n1));
            		System.out.println("\t" + n2 + " - " + n2 + " = " + n2.subtract(n2));
            		System.out.println("\t" + n1 + " - " + n2 + " = " + n1.subtract(n2));
            		System.out.println("\t" + n2 + " - " + n1 + " = " + n2.subtract(n1));
		        break;
            	case 3:  
			System.out.println("\t" + n1 + " * " + n1 + " = " + n1.multiply(n1));
            		System.out.println("\t" + n2 + " * " + n2 + " = " + n2.multiply(n2));
            		System.out.println("\t" + n1 + " * " + n2 + " = " + n1.multiply(n2));
            		System.out.println("\t" + n2 + " * " + n1 + " = " + n2.multiply(n1));
		        break;
            	case 4:  
			System.out.println("\t" + n1 + " / " + n1 + " = " + n1.divide(n1));
            		System.out.println("\t" + n2 + " / " + n2 + " = " + n2.divide(n2));
            		System.out.println("\t" + n1 + " / " + n2 + " = " + n1.divide(n2));
            		System.out.println("\t" + n2 + " / " + n1 + " = " + n2.divide(n1));
		        break;
            	case 5:  
			System.out.println("\t" + n1 + " ct " + n1 + " = " + n1.compareTo(n1));
            		System.out.println("\t" + n2 + " ct " + n2 + " = " + n2.compareTo(n2));
            		System.out.println("\t" + n1 + " ct " + n2 + " = " + n1.compareTo(n2));
            		System.out.println("\t" + n2 + " ct " + n1 + " = " + n2.compareTo(n1));
		        break;
            	case 6:  
			System.out.println("\t" + n1 + " eq "+ n1 + " = " + n1.equals(n1));
            		System.out.println("\t" + n2 + " eq "+ n2 + " = " + n2.equals(n2));
            		System.out.println("\t" + n1 + " eq "+ n2 + " = " + n1.equals(n2));
            		System.out.println("\t" + n2 + " eq "+ n1 + " = " + n2.equals(n1));
		        break;
             	case 7:  
			System.out.println("\t" + n1 + " getReciprocal= " + n1.getReciprocal());
		        break;
            	case 8: 
			System.out.println("\t" + n1 + " toDouble = " + n1.toDouble());
		        break;
                case 9:
                        System.out.print( "read a fraction x/y, please enter x y : " );
                        x = scanner.nextInt();
                        y = scanner.nextInt();
                        System.out.print("\t" + x +"/"+ y + " setSimpleFraction = ");
                        n1.setSimpleFraction(x,y);
                        System.out.println(n1);
                        break;
	      }

           } // end try                                                     
           catch ( SimpleFractionException fracitonException )                
           {                                                                
              System.err.printf( "\nFraction Exception: %s\n", fracitonException );
           } // end catch                                                   
        } while ( continueLoop ); // end do...while                         
    } // end main
} // end class 






