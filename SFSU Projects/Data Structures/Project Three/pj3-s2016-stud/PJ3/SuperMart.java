package PJ3;

import java.util.*;
import java.io.*;

// You may add new functions or data fields in this class 
// You may modify any functions or data members here
// You must use Customer, Cashier and CheckoutArea classes
// to implement SuperMart simulator

class SuperMart {

  // input parameters
  private int numCashiers, customerQLimit;
  private int chancesOfArrival, maxServiceTime;
  private int simulationTime, dataSource;

  // statistical data
  private int numGoaway, numServed, totalWaitingTime;

  // internal data
  private int counter;	             // customer ID counter
  private CheckoutArea checkoutarea; // checkout area object
  private Scanner dataFile;	     // get customer data from file
  private Random dataRandom;	     // get customer data using random function

  // most recent customer arrival info, see getCustomerData()
  private boolean anyNewArrival;  
  private int serviceTime;

  // initialize data fields
  private SuperMart(){
  }

  private void setupParameters(){
    // read input parameters from user
    // setup dataFile or dataRandom
    // add statements
    dataFile = new Scanner(System.in);

    try{
      System.out.println("*** Simulation Parameters ***" + "\n");

      String[] argumentsArray = getArgumentStringsArray();
      int[] parameterVariablesArray = getParameterVariablesArray();

      for(int i = 0; i < argumentsArray.length; i++){
        System.out.print(argumentsArray[i]);
        int userInput = Integer.parseInt(dataFile.nextLine());
        parameterVariablesArray[i] = userInput;
      }
    }
    catch(Exception e){
      System.out.println("Something happened");
    }
  }

  // use by step 1 in doSimulation()
  private void getCustomerData()
  {
	// get next customer data : from file or random number generator
	// set anyNewArrival and serviceTime
        // add statements
  }

  private void doSimulation()
  {
        // add statements

	// Initialize CheckoutArea

	// Time driver simulation loop
  	for (int currentTime = 0; currentTime < simulationTime; currentTime++) {


    		// Step 1: any new customer enters the checkout area?
    		getCustomerData();

    		if (anyNewArrival) {

      		    // Step 1.1: setup customer data
      		    // Step 1.2: check customer waiting queue too long?
    		} else {
      		    System.out.println("\tNo new customer!");
    		}

    		// Step 2: free busy cashiers, add to free cashierQ
    		// Step 3: get free cashiers to serve waiting customers 
  	} // end simulation loop

  }

  private void printStatistics()
  {
	// add statements into this method!
	// print out simulation results
	// see the given example in project statement
  // you need to display all free and busy cashiers 
  }


  private String[] getArgumentStringsArray(){
    return new String[] {"Enter simulation time (positive integer)     : ", "Enter the number of cashiers     : ", 
            "Enter chances (0% < & <= 100%) of new customer     : ", "Enter maximum service time of customers     : ",
              "Enter customer queue limit     : ", "Enter 0/1 to get data from random/file     : ",
                "Enter filename "};
  }

  private int[] getParameterVariablesArray(){
    return new int[] {simulationTime, numCashiers, chancesOfArrival, maxServiceTime, customerQLimit, dataSource};
  }

  // *** main method to run simulation ****

  public static void main(String[] args) {
   	SuperMart runSuperMart=new SuperMart();
   	runSuperMart.setupParameters();
   	runSuperMart.doSimulation();
   	runSuperMart.printStatistics();
  }

}
