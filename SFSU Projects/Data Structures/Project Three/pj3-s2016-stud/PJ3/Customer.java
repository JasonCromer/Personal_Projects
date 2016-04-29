// DO NOT ADD NEW METHODS OR NEW DATA FIELDS!

package PJ3;

class Customer
{
    private int customerID;
    private int serviceTime;
    private int arrivalTime;

    Customer(){
    	customerID = 0;
    	serviceTime = 0;
    	arrivalTime = 0;
    }


    Customer(int customerid, int servicetime, int arrivaltime){
        customerID = customerid;
        serviceTime = servicetime;
        arrivalTime = arrivaltime;
    }


    int getServiceTime(){
        return serviceTime;
    }


    int getArrivalTime(){
  	     return arrivalTime;
    }


    int getCustomerID(){
        return customerID;
    }


    public String toString(){
    	return "customerID="+customerID+":serviceTime="+
               serviceTime+":arrivalTime="+arrivalTime;

    }


    public static void main(String[] args){
        // quick check!
	Customer mycustomer = new Customer(1,35,5);
	System.out.println("Customer Info --> "+mycustomer);

    }
}
