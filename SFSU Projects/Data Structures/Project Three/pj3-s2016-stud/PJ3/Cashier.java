// DO NOT ADD NEW METHODS OR NEW DATA FIELDS!

package PJ3;

class Cashier {

   // cashier id and current customer which is served by this cashier 
   private int cashierID;
   private Customer serveCustomer;

   // start time and end time of current interval
   private int startTime;
   private int endTime;

   // for keeping statistical data
   private int totalFreeTime;
   private int totalBusyTime;
   private int totalCustomers;

   // Constructor
   Cashier()
   {
    cashierID = 0;
    startTime = 0;
    endTime = 0;
    totalFreeTime = 0;
    totalBusyTime = 0;
    totalCustomers = 0;
   }


   // Constructor with cashier id
   Cashier(int cashierId)
   {
    cashierID = cashierId;
   }

   // accessor methods

   int getCashierID() 
   {
    return cashierID;
   }

   Customer getCurrentCustomer() 
   {
        return serveCustomer;
   }

   // Transition from free interval to busy interval
   void freeToBusy (Customer serveCustomer, int currentTime)
   {
  	// goal  : switch from free interval to busy interval
  	//i.e. end free interval, start busy interval 
    //to serve a new customer
    //
  	// steps : update totalFreeTime
  	// 	   set startTime, endTime, serveCustomer, 
  	// 	   update totalCustomers

    // add statements
    this.serveCustomer = serveCustomer;
    
    //Free time interval start in busyToFree() via startTime variable, so the difference between current time (which is now busy)
    //and the startTime of the time the cashier was free is the free time, which we add to the totalFreeTime.
    totalFreeTime += currentTime - startTime;

    //Update start time of being busy to the current time (since cashier is now busy)
    startTime = currentTime;

    //End time will be the time that being busy ends. We use this to set the start time of being free in busyToFree()
    endTime = currentTime + serveCustomer.getServiceTime();

    //Incremement the total amount of customers since we have assisted a customer when this method is called
    totalCustomers += 1;
   }

   // Transition from busy interval to free interval
   Customer busyToFree ()
   {
  	// goal  : switch from busy interval to free interval
  	//         i.e. end busy interval to return served customer, 
        //              start free interval 
   	// 
  	// steps     : update totalBusyTime 
  	// 	       set startTime 
  	//             return serveCustomer
    // add statements

    //Add to total busy time the time it takes to serve a customer
    totalBusyTime += serveCustomer.getServiceTime();

    //End time is the time that the busy interval is over. This is when free time starts
    startTime = endTime;

    return serveCustomer;
   }

   // Return end busy clock time, use in priority queue
   int getEndBusyTime() 
   {
    return endTime + totalBusyTime;
   }

   // For free interval at the end of simulation, 
   // update totalFreeTime 
   void setEndFreeTime (int endsimulationtime)
   {
  	// for free interval at the end of simulation:
  	// set endTime, update totalFreeTime

    // add statements
    totalFreeTime += endsimulationtime - startTime;
    endTime = endsimulationtime;
   }

   // For busy interval at the end of simulation, 
   // update totalBusyTime 
   void setEndBusyTime (int endsimulationtime)
   {
  	// for busy interval at the end of simulation:
  	// set endTime, update totalBusyTime

    // add statements
    totalBusyTime += endsimulationtime - endTime;
    endTime = endsimulationtime;
   }

   // functions for printing statistics :
   void printStatistics () 
   {
  	// print cashier statistics, see project statement

  	System.out.println("\t\tCashier ID             : "+cashierID);
  	System.out.println("\t\tTotal free time        : "+totalFreeTime);
  	System.out.println("\t\tTotal busy time        : "+totalBusyTime);
  	System.out.println("\t\tTotal # of customers   : "+totalCustomers);
  	if (totalCustomers > 0)
  	    System.out.format("\t\tAverage checkout time  : %.2f%n\n",(totalBusyTime*1.0)/totalCustomers);
   }

   public String toString()
   {
	return "CashierID="+cashierID+":startTime="+startTime+
               ":endTime="+endTime+">>serveCustomer:"+serveCustomer;
   }

   public static void main(String[] args) {
        // quick check
      Customer mycustomer = new Customer(1,15,5);
      Cashier mycashier = new Cashier(5);
      mycashier.freeToBusy (mycustomer, 12);
      System.out.println(mycashier);
   }

};

