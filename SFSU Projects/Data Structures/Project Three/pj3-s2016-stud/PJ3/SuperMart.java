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
  private Scanner dataFile;	         // get customer data from file
  private Scanner inputScanner;    //Get user input from console
  private Random dataRandom;	     // get customer data using random function

  // most recent customer arrival info, see getCustomerData()
  private boolean anyNewArrival; 
  private boolean shouldGetCashier; 
  private int serviceTime;


  // initialize data fields
  private SuperMart(){
    counter = 1;
    inputScanner = new Scanner(System.in);
    shouldGetCashier = false;
  }


  private void setupParameters(){
    //Iterate through initial parameters
    iterateThroughParameters();

    //Open data file for streaming (if it exists)
    if(isDataInFile(dataSource)){
      final String fileName = getDataSourceFileName();
      openDataFile(fileName);
    }
  }


  /*
    This method sets the anyNewArrival and serviceTime variables either
    through the DataFile given by the user, or through a Randomly generated number.
  */
  private void getCustomerData(){

    if(isDataInFile(dataSource)){
      setNewArrivalAndServiceTimeFromDataFile();
    }
    else{
      generateRandomArrivalChanceAndServiceTime();
    }
  }


  private void doSimulation(){ 
    System.out.println("\n\n" + "*** Start Simulation ***" + "\n\n");
    // Initialize CheckoutArea
    checkoutarea = new CheckoutArea(numCashiers, customerQLimit);

    System.out.println("Cashiers #1 to #" + numCashiers + " are ready...");

    // Time driver simulation loop
  	for (int currentTime = 0; currentTime < simulationTime; currentTime++) {
        System.out.println("---------------------------------------------------------");
        System.out.println("Time : " + currentTime);

    		// Step 1: any new customer enters the checkout area?
    		getCustomerData();

    		if (anyNewArrival) {
      		// Step 1.1: setup customer data
          Customer customer = new Customer(counter,serviceTime,currentTime);

          System.out.println("\tCustomer #" + counter + " arrives with service time " + serviceTime + " units");

      		// Step 1.2: check customer waiting queue too long?
          if(!checkoutarea.isCustomerQTooLong()){
            checkoutarea.insertCustomerQ(customer);
            System.out.println("\tCustomer #" + counter + " waits in the customer queue");
            
          }
          else{
            System.out.println("\tCustomer #" + counter + " leaves, as the customer queue is full");
          }

    		} 
        else {
      		    System.out.println("\tNo new customer!");
    		}

    		// Step 2: free busy cashiers, add to freeCashierQ
        while(!checkoutarea.emptyBusyCashierQ()){
          Cashier busyCashier = checkoutarea.peekBusyCashierQ();
          Customer busyCustomer = busyCashier.getCurrentCustomer();
          int arrivalTime = busyCustomer.getArrivalTime();
          int serviceTime = busyCustomer.getServiceTime();

          if((arrivalTime + serviceTime) <= currentTime){
            busyCashier = checkoutarea.removeBusyCashierQ();
            System.out.println("\tCashier #" + busyCashier.getCashierID() + " is free");
            Customer customer = busyCashier.busyToFree();
            System.out.println("\tCustomer #" + customer.getCustomerID() + " is done");
            checkoutarea.insertFreeCashierQ(busyCashier);
          }
          else{
            break;
          }
        }

        // Step 3: get free cashiers to serve waiting customers 
        if(!checkoutarea.emptyFreeCashierQ() && !checkoutarea.emptyCustomerQ()){
          System.out.println("\tCustomer #" + counter + " gets a cashier");

          Cashier cashier = checkoutarea.removeFreeCashierQ();
          Customer customer = checkoutarea.removeCustomerQ();
          cashier.freeToBusy(customer, currentTime);
          cashier.setEndBusyTime(currentTime + serviceTime);
          checkoutarea.insertBusyCashierQ(cashier);

          System.out.println("\tCashier #" + cashier.getCashierID() + " starts serving customer #" +
                                  counter + " for " + serviceTime + " units");

          counter ++;
        }
  	} // end simulation loop

  }

  private void printStatistics()
  {
	// add statements into this method!
	// print out simulation results
	// see the given example in project statement
  // you need to display all free and busy cashiers 
  }


  /*
    A string array containing the arguments that we prompt to the user
  */
  private String[] getArgumentStringsArray(){
    return new String[] {"Enter simulation time (positive integer)     : ", "Enter the number of cashiers     : ", 
            "Enter chances (0% < & <= 100%) of new customer     : ", "Enter maximum service time of customers     : ",
              "Enter customer queue limit     : ","Enter 0/1 to get data from random/file     : "};
  }


  /*
    In this method we iterate trough the getArgumentsStringArray and apply the user input returned
    from each of their prompts to their respective constants (defined at the top of the page).
    i.e user reponse from the prompt "Enter simulation time (positive integer" will be assigned to simulationTime
  */
  private void iterateThroughParameters(){

    try{
      System.out.println("*** Simulation Parameters ***" + "\n");

      //Get arrays containing the output strings, and the variables we wish to assign user input to
      String[] argumentsStringArray = getArgumentStringsArray();

      //Iterate through our argumentsStringArray and assign each input to each integer variable in our
      //parameterVariablesArray
      for(int i = 0; i < argumentsStringArray.length; i++){

        //Print parameter argumenth
        System.out.print(argumentsStringArray[i]);

        int userInput = Integer.parseInt(inputScanner.nextLine());

        //Assign user input to corresponding prompt in argumentsStringArray using ith index
        setParameter(i, userInput);
      }
    }
    catch(NumberFormatException e){
      printNumberFormatExceptionError();

      //reprompt
      iterateThroughParameters();
    }
  }


  /*
    This methods sets the corresponding argument in getArgumentsStringArray to constants
  */
  private void setParameter(int correspondingIndexInArgArray, int userInput){
    simulationTime = (correspondingIndexInArgArray == 0) ? userInput: simulationTime;
    numCashiers = (correspondingIndexInArgArray == 1) ? userInput : numCashiers;
    chancesOfArrival = (correspondingIndexInArgArray == 2) ? userInput : chancesOfArrival;
    maxServiceTime = (correspondingIndexInArgArray == 3) ? userInput : maxServiceTime;
    customerQLimit = (correspondingIndexInArgArray == 4) ? userInput : customerQLimit;
    dataSource = (correspondingIndexInArgArray == 5) ? userInput : dataSource;
  }


  //If the input is 1, the data contained for the simulation is contained in an external file
  private boolean isDataInFile(int input){
    return input == 1;
  }


  /*
    This method retrieves the filename that the user enters
  */
  private String getDataSourceFileName(){
    //Prompt user for file name
    System.out.print("Enter filename     : ");

    return inputScanner.nextLine();
  }


  private void openDataFile(String fileName){
    try{
      dataFile = new Scanner(new File(fileName));
    }
    catch(FileNotFoundException e){
      System.out.println("File not found!");
      openDataFile(fileName);
    }
  }


  /*
    This method generates a random Int and uses it to produce random results for
    anyNewArrival boolean and the serviceTime constant for each customer
  */
  private void generateRandomArrivalChanceAndServiceTime(){
    Random randomInteger = new Random();

    //Assign random value to whether or not there is a new arrival of a customer
    anyNewArrival = ((randomInteger.nextInt(100)+1) <= chancesOfArrival);

    //Assign random value to the service time of customers
    serviceTime = randomInteger.nextInt(maxServiceTime)+1;
  }


  private void setNewArrivalAndServiceTimeFromDataFile(){
    try{
      anyNewArrival = (((dataFile.nextInt() % 100) + 1) <= chancesOfArrival);
      serviceTime = (dataFile.nextInt() % maxServiceTime) + 1;
    }
    catch(NoSuchElementException e){
      System.out.println("ERROR: given simulation time exceeds the amount of lines in the data file!");
    }
  }


  private void printNumberFormatExceptionError(){
    System.out.println("\n\n" + "ERROR:   Input can only contain Integers!" + "\n\n");
  }

  // *** main method to run simulation ****

  public static void main(String[] args) {
   	SuperMart runSuperMart=new SuperMart();
   	runSuperMart.setupParameters();
   	runSuperMart.doSimulation();
   	runSuperMart.printStatistics();
  }

}
