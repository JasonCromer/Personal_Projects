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

    //Iterate through initial parameters
    iterateThroughParameters(dataFile);

    setDataSource(dataFile);

    if(isDataInFile(dataSource)){
      parseDataFile();
    }
    else{
      generateRandomArrivalChanceAndServiceTime();
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
              "Enter customer queue limit     : "};
  }


  private void iterateThroughParameters(Scanner scanner){

    try{
      System.out.println("*** Simulation Parameters ***" + "\n");

      //Get arrays containing the output strings, and the variables we wish to assign user input to
      String[] argumentsStringArray = getArgumentStringsArray();

      //Iterate through our argumentsStringArray and assign each input to each integer variable in our
      //parameterVariablesArray
      for(int i = 0; i < argumentsStringArray.length; i++){

        //Print parameter argumenth
        System.out.print(argumentsStringArray[i]);

        String userInput = scanner.nextLine();

        if(userInput.equals("exit"))
          break;

        //Assign user input to variable in parameterVariablesArray
        setParameter(i, Integer.parseInt(userInput));
      }

    }
    catch(NumberFormatException e){
      System.out.println("\n\n" + "ERROR >>> Input can only contain Integers!" + "\n\n");
      iterateThroughParameters(scanner);
    }
  }


  //This methods sets the corresponding argument in getArgumentsStringArray to constants
  private void setParameter(int correspondingIndexInArgArray, int userInput){
    switch(correspondingIndexInArgArray){

      case 0: simulationTime = userInput;
              break;
      case 1: numCashiers = userInput;
              break;
      case 2: chancesOfArrival = userInput;
              break;
      case 3: maxServiceTime = userInput;
              break;
      case 4: customerQLimit = userInput;
              break; 
    }
  }


  private void setDataSource(Scanner scanner){
      System.out.print("Enter 0/1 to get data from random/file     : ");
      dataSource = Integer.parseInt(dataFile.nextLine());
  }

  //If the input is 1, the data contained for the simulation is contained in an external file
  private boolean isDataInFile(int input){
    return input == 1;
  }


  private void parseDataFile(){
    //TODO: get path to file and parse it (See README)
  }


  private void generateRandomArrivalChanceAndServiceTime(){
    Random randomInteger = new Random();

    //Assign random value to whether or not there is a new arrival of a customer
    anyNewArrival = ((randomInteger.nextInt(100)+1) <= chancesOfArrival);

    //Assign random value to the service time of customers
    serviceTime = randomInteger.nextInt(maxServiceTime)+1;
  }


  // *** main method to run simulation ****

  public static void main(String[] args) {
   	SuperMart runSuperMart=new SuperMart();
   	runSuperMart.setupParameters();
   	runSuperMart.doSimulation();
   	runSuperMart.printStatistics();
  }

}
