package PJ4;
import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. Jacks or Better: a pair pays out only if the cards in the pair are Jacks, 
 * 	Queens, Kings, or Aces. Lower pairs do not pay out. 
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */


/* This is the main poker game class.
 * It uses Decks and Card objects to implement poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */


public class VideoPoker {

    // default constant values
    private static final int startingBalance=100;
    private static final int numberOfCards=5;

    // default constant payout value and playerHand types
    private static final int[] multipliers={1,2,3,5,6,9,25,50,250};
    private static final String[] goodHandTypes={ 
	  "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush 	", 
	  "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

    // must use only one deck
    private static final Decks oneDeck = new Decks(1);

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private HashMap<Integer,Card> playerCardsToKeepMap;
    private int playerBalance;
    private int playerBet;

    //Scanner for user input
    private Scanner scanner;

    //boolean to check if user wants to see checkout table
    boolean showPayoutTable = true;



    /** default constructor, set balance = startingBalance */
    public VideoPoker(){
		this(startingBalance);
		playerBalance = startingBalance;
		scanner = new Scanner(System.in);
		playerCardsToKeepMap = new HashMap<>();
    }

    /** constructor, set given balance */
    public VideoPoker(int balance){
		this.playerBalance = balance;
		scanner = new Scanner(System.in);
		playerCardsToKeepMap = new HashMap<>();
    }

    /** This display the payout table based on multipliers and goodHandTypes arrays */
    private void showPayoutTable(){ 
		System.out.println("\n\n");
		System.out.println("Payout Table   	      Multiplier   ");
		System.out.println("=======================================");
		int size = multipliers.length;
		for (int i=size-1; i >= 0; i--) {
			System.out.println(goodHandTypes[i]+"\t|\t"+multipliers[i]);
		}
		System.out.println("\n\n");
    }

    /** Check current playerHand using multipliers and goodHandTypes arrays
     *  Must print yourHandType (default is "Sorry, you lost") at the end of function.
     *  This can be checked by testCheckHands() and main() method.
     */
    private void checkHands(){
    	if(isRoyalFlush()){
    		System.out.println("Royal Flush!");
    		playerBalance += multipliers[8] * playerBet;
    	}
    	else if(isStraightFlush()){
    		System.out.println("Straight Flush!");
    		playerBalance += multipliers[7] * playerBet;	
    	}
    	else if(isStraight()){
    		System.out.println("Straight!");
    		playerBalance += multipliers[3] * playerBet;
    	}
    	else if(isFlush()){
    		System.out.println("Flush!");
    		playerBalance += multipliers[4] * playerBet;
    	}
    	else if(isFullHouse()){
    		System.out.println("Full House");
    		playerBalance += multipliers[5] * playerBet;
    	}
    	else if(isOfAKind(4)){
    		System.out.println("Four of a Kind!");
    		playerBalance += multipliers[6] * playerBet;
    	}
    	else if(isOfAKind(3)){
    		System.out.println("Three of a Kind!");
    		playerBalance += multipliers[2] * playerBet;
    	}
    	else if(isTwoPair()){
    		System.out.println("Two Pair!");
    		playerBalance += multipliers[1] * playerBet;
    	}
    	else if(isRoyalPair()){
    		System.out.println("Royal Pair!");
    		playerBalance += multipliers[0] * playerBet;
    	}
    	else
    		System.out.println("Sorry, you lost!");
    }


     /*************************************************
     *   add other private methods here ....
     *
     *************************************************/


    //Consective cards of same suit of rank: A, 10, J, Q, K
    private boolean isRoyalFlush(){
    	int firstCardSuit = playerHand.get(0).getSuit();
    	List<Integer> royalFlushRankList = Arrays.asList(1,10,11,12,13);

    	for(Card card : playerHand){
    		if(card.getSuit() != firstCardSuit || !royalFlushRankList.contains(card.getRank()))
    			return false;
    	}

    	return true;
    }


    //Consecutive card ranks of same suit
    private boolean isStraightFlush(){
    	int firstCardSuit = playerHand.get(0).getSuit();
    	List<Integer> sortedCardRanks = new ArrayList<>();

    	//Create an Integer list containing all the player's ranks
    	for(Card card : playerHand){
    		sortedCardRanks.add(card.getRank());
    	}

    	//Sort the ranks
    	Collections.sort(sortedCardRanks);

    	//Check to see that all card suits are identical
    	for(Card card : playerHand){
    		if(card.getSuit() != firstCardSuit)
    			return false;
    	}

    	//Go Step by step to see if the next card's rank is only 1 more than it
    	for(int i = 0; i < 4; i++){
    		if(!(sortedCardRanks.get(i) == (sortedCardRanks.get(i+1) - 1)))
    			return false;
    	}

    	return true;
    }


    //Consective cards of different suits
	private boolean isStraight(){
    	int firstCardSuit = playerHand.get(0).getSuit();
    	List<Integer> sortedCardRanks = new ArrayList<>();
    	List<Integer> cardSuits = new ArrayList<>();

    	//Create an Integer list containing all the player's ranks
    	for(Card card : playerHand){
    		sortedCardRanks.add(card.getRank());
    		cardSuits.add(card.getSuit());
    	}

    	//Sort the ranks
    	Collections.sort(sortedCardRanks);

    	//Check to see that all card suits are not identical
    	Set<Integer> suitSet = new HashSet<>(cardSuits);

    	//If set size is smaller, there are duplicates, meaning more than one suit, which a Straight requires
    	if(suitSet.size() > cardSuits.size())
    		return false;

    	//Go Step by step to see if the next card's rank is only 1 more than it
    	for(int i = 0; i < 4; i++){
    		if(!(sortedCardRanks.get(i) == (sortedCardRanks.get(i+1) - 1)))
    			return false;
    	}

    	return true;
    }


    //Hand of entirely identical suits
    private boolean isFlush(){
    	int firstCardSuit = playerHand.get(0).getSuit();

    	//Check to see that all card suits are identical
    	for(Card card : playerHand){
    		if(card.getSuit() != firstCardSuit)
    			return false;
    	}

    	return true;
    }


    //Hand consisting of 3 same ranks, and 2 other same ranks
    private boolean isFullHouse(){
    	HashMap<Integer,Integer> rankMap = new HashMap<>();

    	for(Card card : playerHand){
    		if(!rankMap.containsKey(card.getRank())){
    			rankMap.put(card.getRank(), 1);
    		}
    		else{
    			int value = rankMap.get(card.getRank());
    			rankMap.put(card.getRank(), value+1);
    		}
    	}

    	return rankMap.containsValue(3) && rankMap.containsValue(2);
    }


    //Hand consisting of either 3, 4 or N of same kinds of ranks
    private boolean isOfAKind(int kindType){
    	HashMap<Integer,Integer> rankMap = new HashMap<>();

    	for(Card card : playerHand){
    		if(!rankMap.containsKey(card.getRank())){
    			rankMap.put(card.getRank(), 1);
    		}
    		else{
    			int value = rankMap.get(card.getRank());
    			rankMap.put(card.getRank(), value+1);
    		}
    	}

    	return rankMap.containsValue(kindType);
    }


    //Hand consisting of two pairs of identical ranks, and 1 different rank
    private boolean isTwoPair(){
    	HashMap<Integer,Integer> rankMap = new HashMap<>();
    	int pairCounter = 0;

    	for(Card card : playerHand){
    		if(!rankMap.containsKey(card.getRank())){
    			rankMap.put(card.getRank(), 1);
    		}
    		else{
    			int value = rankMap.get(card.getRank());
    			rankMap.put(card.getRank(), value+1);
    			pairCounter++;
    		}
    	}

    	return pairCounter == 2 && rankMap.containsValue(1);
    }


    //Hand consisting of one pair of identical rank, and 3 cards of different ranks
    private boolean isRoyalPair(){
    	HashMap<Integer,Integer> rankMap = new HashMap<>();
    	int pairCounter = 0;

    	for(Card card : playerHand){
    		if(!rankMap.containsKey(card.getRank())){
    			rankMap.put(card.getRank(), 1);
    			pairCounter = 1;
    		}
    		else{
    			int value = rankMap.get(card.getRank());
    			rankMap.put(card.getRank(), value+1);
    			pairCounter++;
    		}
    	}

    	return pairCounter == 2 && rankMap.containsValue(1);
    }



    private void getPlayerBet(){
    	System.out.print("Enter bet: ");
    	try{
    		playerBet = scanner.nextInt();

    		if(playerBet > playerBalance){
    			System.out.println("\nBet is larger than balance, try again");
    			getPlayerBet();
    		}
    	}
    	catch(InputMismatchException e){
    		System.out.println("\nPlease input integers only. Try again");
    		getPlayerBet();
    	}
    }


    private void updateBalance(){
    	playerBalance -= playerBet;
    }


    private void dealHand(){
    	try{
    		playerHand = oneDeck.deal(numberOfCards);
    	}
    	catch(PlayingCardException e){
    		System.out.println("PlayingCardException: " + e.getMessage());
    	}
    }


    private void getPlayerCardRetainingPositions(){
    	System.out.print("Enter positions of cards to keep (e.g. 1 4 5 ): ");

    	//Create new instance of scanner and get input line
    	scanner = new Scanner(System.in);
    	String input = scanner.nextLine();

    	if(input.isEmpty()){
    		return;
    	}

    	//Strip preceding and trailing whitespace, and split input by space delimeter
    	String[] positionsToKeep = input.trim().split("\\s+");

    	try{
    		for(int i = 0; i < positionsToKeep.length; i++){

    			//Subtract one to account for offset (we assume first position is 1)
    			int position = Integer.parseInt(positionsToKeep[i]) - 1;
    			Card card = playerHand.get(position);
    			playerCardsToKeepMap.put(position, card);
    		}
    	}
    	catch(Exception e){
    		System.out.println("\nPlease input integers 1-5 only. Try again");
    		getPlayerCardRetainingPositions();
    	}
    }


    private void setAndDisplayNewPlayerHand(){
    	//Deal new hand
    	dealHand();

    	//Iterate through hashmap and add in the cards the player chose to keep
    	for(Map.Entry<Integer, Card> card : playerCardsToKeepMap.entrySet()){
    		playerHand.set(card.getKey(), card.getValue());
    	}
		
		System.out.println(playerHand.toString());
    }


    private void checkToPlayNewGame(){
    	System.out.println("\nDo you want to play a new game? (y or n)");
    	scanner = new Scanner(System.in);

    	String input = scanner.nextLine();
    	if(input.equals("y")){
    		checkAndDisplayIfPlayerWantsCheckoutTable();
    		play();
    	}
    	else if(input.equals("n")){
    		exitGame();
    	}
    	else{
    		System.out.println("Please enter (y or n)");
    		checkToPlayNewGame();
    	}
    }


    private void checkAndDisplayIfPlayerWantsCheckoutTable(){
    	System.out.println("\nWant to see payout table (y or n)");
    	String input = scanner.nextLine();

    	if(input.equals("n")){
    		showPayoutTable = false;
    	}
    }


    private void exitGame(){
    	showBalance();
    	System.out.println("\nBye!");
		System.exit(0);
    }


    private void showBalance(){
    	System.out.println("\nBalance: $" + playerBalance);
    }

    public void play(){

    	if(showPayoutTable){
    		showPayoutTable();
    	}

    	System.out.println("\n\n-----------------------------------");
    	showBalance();

    	//Get user bet input
    	getPlayerBet();

    	//Update user balance to reflect bet
    	updateBalance();

    	//Reset and shuffle Deck
    	oneDeck.reset();
    	oneDeck.shuffle();

    	//Deal cards and display them
    	dealHand();
    	System.out.println(playerHand.toString());

    	//Get the positions of cards players want to keep
    	getPlayerCardRetainingPositions();

    	//Deal new cards after user specifies which to keep
    	setAndDisplayNewPlayerHand();

    	//Check what hand type player has and adjust balance
    	checkHands();

    	//Show Balance
    	showBalance();

    	if(playerBalance == 0){
    		exitGame();
    	}
    	else{
    		checkToPlayNewGame();
    	}
    }



    /*************************************************
     *   Do not modify methods below
    /*************************************************


    /** testCheckHands() is used to test checkHands() method 
     *  checkHands() should print your current hand type
     */ 

    private void testCheckHands()
    {
      	try {
    		playerHand = new ArrayList<Card>();

		// set Royal Flush
		playerHand.add(new Card(4,1));
		playerHand.add(new Card(4,10));
		playerHand.add(new Card(4,12));
		playerHand.add(new Card(4,11));
		playerHand.add(new Card(4,13));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight Flush
		playerHand.set(0,new Card(4,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight
		playerHand.set(4, new Card(2,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Flush 
		playerHand.set(4, new Card(4,5));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	 	// "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

		// set Four of a Kind
		playerHand.clear();
		playerHand.add(new Card(4,8));
		playerHand.add(new Card(1,8));
		playerHand.add(new Card(4,12));
		playerHand.add(new Card(2,8));
		playerHand.add(new Card(3,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Three of a Kind
		playerHand.set(4, new Card(4,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Full House
		playerHand.set(2, new Card(2,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Two Pairs
		playerHand.set(1, new Card(2,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Royal Pair
		playerHand.set(0, new Card(2,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// non Royal Pair
		playerHand.set(2, new Card(4,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");
      	}
      	catch (Exception e)
      	{
		System.out.println(e.getMessage());
      	}
    }


    /* Run testCheckHands() */
    public static void main(String args[]) 
    {
	VideoPoker pokergame = new VideoPoker();
	pokergame.testCheckHands();
    }
}
