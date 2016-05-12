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
	  "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush", 
	  "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

    // must use only one deck
    private static final Decks oneDeck = new Decks(1);

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;

    /** default constructor, set balance = startingBalance */
    public VideoPoker(){
	this(startingBalance);
	playerBalance = startingBalance;
    }

    /** constructor, set given balance */
    public VideoPoker(int balance)
    {
	this.playerBalance = balance;
    }

    /** This display the payout table based on multipliers and goodHandTypes arrays */
    private void showPayoutTable()
    { 
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
    	if(isRoyalFlush())
    		System.out.println("Royal Flush!");
    	else if(isStraightFlush())
    		System.out.println("Straight Flush!");	
    	else if(isStraight())
    		System.out.println("Straight!");
    	else if(isFlush())
    		System.out.println("Flush!");
    	else if(isFullHouse())
    		System.out.println("Full House");
    	else if(isOfAKind(4))
    		System.out.println("Four of a Kind!");
    	else if(isOfAKind(3))
    		System.out.println("Three of a Kind!");
    	else if(isTwoPair())
    		System.out.println("Two Pair!");
    	else if(isRoyalPair())
    		System.out.println("Royal Pair!");
    	else
    		System.out.println("Sorry, you lost!");
    }

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


    private boolean isFlush(){
    	int firstCardSuit = playerHand.get(0).getSuit();

    	//Check to see that all card suits are identical
    	for(Card card : playerHand){
    		if(card.getSuit() != firstCardSuit)
    			return false;
    	}

    	return true;
    }


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

    /*************************************************
     *   add other private methods here ....
     *
     *************************************************/


    public void play() 
    {
    /** The main algorithm for single player poker game 
     *
     * Steps:
     * 		showPayoutTable()
     *
     * 		++	
     * 		show balance, get bet 
     *		verify bet value, update balance
     *		reset deck, shuffle deck, 
     *		deal cards and display cards
     *		ask for positions of cards to keep  
     *          get positions in one input line
     *		update cards
     *		check hands, display proper messages
     *		update balance if there is a payout
     *		if balance = O:
     *			end of program 
     *		else
     *			ask if the player wants to play a new game
     *			if the answer is "no" : end of program
     *			else : showPayoutTable() if user wants to see it
     *			goto ++
     */


        // implement this method!
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
