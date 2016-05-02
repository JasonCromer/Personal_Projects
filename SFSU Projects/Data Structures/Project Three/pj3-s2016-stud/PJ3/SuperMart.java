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
  private int customerLeftCounter;    //Counts how many customers left because of full queue
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
    customerLeftCounter = 0;
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
        printCurrentTimeInterval(currentTime);

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
            customerLeftCounter++;
          }

    		} 
        else {
      		    System.out.println("\tNo new customer!");
    		}

    		// Step 2: free busy cashiers, add to freeCashierQ
        freeBusyCashiers(checkoutarea, currentTime);

        // Step 3: get free cashiers to serve waiting customers 
        if(!checkoutarea.emptyFreeCashierQ() && !checkoutarea.emptyCustomerQ()){
          setFreeCashiersToBusy(checkoutarea, currentTime);
          counter ++;
        }
  	}

  }


  private void printStatistics(){
    System.out.println("\n\n=======================================================\n\n");
    System.out.println("End of simulation report\n");

    //Counter incremements at end of simulation loop, so account for final loop by subtracting one
    System.out.println("\t# of arrival customers    : " + (counter + customerLeftCounter -1));
    System.out.println("\t# customer gone-away      : " + customerLeftCounter);
    System.out.println("\t# customers served        : " + (counter - 1));

    System.out.println("\n\t*** Current Cashiers Info ***\n");
    System.out.println("\t# WaitingCustomers        : " + checkoutarea.sizeCustomerQ());
    System.out.println("\t# busy cashiers           : " + checkoutarea.sizeBusyCashierQ());
    System.out.println("\t# free cashiers           : " + checkoutarea.sizeFreeCashierQ());

    System.out.println("\n\tTotal waiting time       : 0");
    System.out.println("\tAverage waiting time      : 0.0");

    System.out.println("\n\n\tBusy Cashiers Info:");
    while(!checkoutarea.emptyBusyCashierQ()){
      Cashier busyCashier = checkoutarea.removeBusyCashierQ();
      busyCashier.printStatistics();
    }

    System.out.println("\n\nFree Cashiers Info: ");
    while(!checkoutarea.emptyFreeCashierQ()){
      Cashier freeCashier = checkoutarea.removeFreeCashierQ();
      freeCashier.printStatistics();
    }

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


  /*
    This method frees any busy cashier's who are done serving their customers
  */
  private void freeBusyCashiers(CheckoutArea checkoutarea, int currentTime){
    while(!checkoutarea.emptyBusyCashierQ()){
      //Peek at next cashier's customer to see if their service time is now done
      Cashier busyCashier = checkoutarea.peekBusyCashierQ();
      int busyCustomerEndServiceTime = getBusyCashiersCustomerEndServiceTime(busyCashier);

      if(busyCustomerEndServiceTime <= currentTime){
        //If busy cashier is done serving customer, remove cashier from Queue
        busyCashier = checkoutarea.removeBusyCashierQ();
        System.out.println("\tCashier #" + busyCashier.getCashierID() + " is free");

        //Update Cashier's stats by changing availability as free
        Customer customer = busyCashier.busyToFree();
        System.out.println("\tCustomer #" + customer.getCustomerID() + " is done");

        //Place the now free cashier in the freeCashierQ
        checkoutarea.insertFreeCashierQ(busyCashier);
      }
      else{
        //If top cashier is not free (priority queue), end check
        break;
      }
    } 
  }


  /*
    This method gets a cashiers customer and returns the time at which service should be complete
  */
  private int getBusyCashiersCustomerEndServiceTime(Cashier cashier){
      Customer busyCustomer = cashier.getCurrentCustomer();
      int arrivalTime = busyCustomer.getArrivalTime();
      int serviceTime = busyCustomer.getServiceTime();

      return arrivalTime + serviceTime;

  }


  /*
    This method gets the next cutomer in line and assigns it to a cashier,
    then making the necessary changes to set the cashier to busy
  */
  private void setFreeCashiersToBusy(CheckoutArea checkoutarea, int currentTime){
    System.out.println("\tCustomer #" + counter + " gets a cashier");

    //Get next free Cashier from freeCashierQ
    Cashier cashier = checkoutarea.removeFreeCashierQ();

    //Get next free custoemr from customerQ
    Customer customer = checkoutarea.removeCustomerQ();

    //Set Cashier as busy, and set its customer to the recently removed customer
    cashier.freeToBusy(customer, currentTime);

    //Set end busy time of cashier to the current time plus how long it takes to help the customer
    cashier.setEndBusyTime(currentTime + serviceTime);

    //Insert cashier into the busyCashierQ
    checkoutarea.insertBusyCashierQ(cashier);

    System.out.println("\tCashier #" + cashier.getCashierID() + " starts serving customer #" +
                            counter + " for " + serviceTime + " units");
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


  /*
    This method scans the local directory for the user inputted filename.
    If no file with the name is found, the method will call itself to 
    give the user a retry with entering the file name.
  */
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


  /*
    This method scans a previously declared input file and retrieves two integers at a time,
    using them to calculate the values of anyNewArrival and serviceTime
  */
  private void setNewArrivalAndServiceTimeFromDataFile(){
    try{
      anyNewArrival = (((dataFile.nextInt() % 100) + 1) <= chancesOfArrival);
      serviceTime = (dataFile.nextInt() % maxServiceTime) + 1;
    }
    catch(NoSuchElementException e){
      System.out.println("ERROR: given simulation time exceeds the amount of lines in the data file!");
    }
  }


  private void printCurrentTimeInterval(int currentTime){
    System.out.println("---------------------------------------------------------");
    System.out.println("Time : " + currentTime);
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
