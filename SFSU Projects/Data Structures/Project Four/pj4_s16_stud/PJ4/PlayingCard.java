package PJ4;

import java.util.*;


//=================================================================================

/** class PlayingCardException: It is used for errors related to Card and Deck objects
 *  This is a checked exception! 
 *  Do not modify this class!
 */
class PlayingCardException extends Exception {

    /* Constructor to create a PlayingCardException object */
    PlayingCardException (){
		super ();
    }

    PlayingCardException ( String reason ){
		super ( reason );
    }
}


//=================================================================================

/** class Card : for creating playing card objects
 *  it is an immutable class.
 *  Rank - valid values are 1 to 13
 *  Suit - valid values are 1 to 4
 *  Do not modify this class!
 */
class Card {
	
    /* constant suits and ranks */
    static final String[] Suit = {"","Clubs", "Diamonds", "Hearts", "Spades" };
    static final String[] Rank = {"","A","2","3","4","5","6","7","8","9","10","J","Q","K"};

    /* Data field of a card: rank and suit */
    private int cardRank;  	/* values: 1-13 (see Rank[] above) */
    private int cardSuit;  	/* values: 1-4  (see Suit[] above) */

    /* Constructor to create a card */
    /* throw PlayingCardException if rank or suit is invalid */
    public Card(int suit, int rank) throws PlayingCardException 
    { 
	if ((rank < 1) || (rank > 13))
		throw new PlayingCardException("Invalid rank:"+rank);
	else
        	cardRank = rank;
	if ((suit < 1) || (suit > 4))
		throw new PlayingCardException("Invalid suit:"+suit);
	else
        	cardSuit = suit;
    }

    /* Accessor and toString */
    /* You may impelemnt equals(), but it will not be used */
    public int getRank() { return cardRank; }
    public int getSuit() { return cardSuit; }
    public String toString() { return Rank[cardRank] + " " + Suit[cardSuit]; }

    
    /* Quick tests */
    public static void main(String args[])
    {
	try {
	    Card c1 = new Card(4,1);    // A Spades
	    System.out.println(c1);
	    c1 = new Card(1,10);	// 10 Clubs
	    System.out.println(c1);
	    c1 = new Card(5,10);        // generate exception here
	}
	catch (PlayingCardException e)
	{
	    System.out.println("PlayingCardException: "+e.getMessage());
	}
    }
}


//=================================================================================

/** class Decks represents : n decks of 52 playing cards
 *  Use class Card to construct n * 52 playing cards!
 *
 *  Do not add new data fields!
 *  Do not modify any methods
 *  You may add private methods 
 */

class Decks {

    /* this is used to keep track of original n*52 cards */
    private List<Card> saveDecks;   

    /* this starts by copying all cards from saveDecks   */
    /* it holds remaining cards during games             */
    private List<Card> playDecks;

    /* number of 52-card decks in this object */
    private int numberDecks;

    //constants
    public static final int firstSuit = 1;
    public static final int lastSuit = 4;
    public static final int firstRank = 1;
    public static final int lastRank = 13;


    /**
     * Constructor: Creates default one deck of 52 playing cards in saveDecks and
     * 		    copy them to playDecks.
     *              initialize numberDecks=1
     * Note: You need to catch PlayingCardException from Card constructor
     *	     Use ArrayList for both saveDecks & playDecks
     */
    public Decks()
    {
    	numberDecks = 1;
    	saveDecks = new ArrayList<>();
    	playDecks = new ArrayList<>();

        //Add a card of each type to both decks
        try{
            for(int suit = firstSuit; suit <= lastSuit; suit++){
                for(int rank = firstRank; rank <= lastRank; rank++){
                	Card newCard = new Card(suit, rank);
                    saveDecks.add(newCard);
                    playDecks.add(newCard);
                }
            }

        }
        catch(PlayingCardException e){
        	e.printStackTrace();
        	System.out.println("PlayingCardException: " + e.getMessage());
        }

    }


    /**
     * Constructor: Creates n 52-card decks of playing cards in
     *              saveDecks and copy them to playDecks.
     *              initialize numberDecks=n
     * Note: You need to catch PlayingCardException from Card constructor
     *	     Use ArrayList for both saveDecks & playDecks
     */
    public Decks(int n){
	   	numberDecks = n;
    	saveDecks = new ArrayList<>();
    	playDecks = new ArrayList<>();

        //Add a card of each type to both decks, n amount of times
        try{
        	for(int deckNumber = 0; deckNumber < n; deckNumber++){
	            for(int suit = firstSuit; suit <= lastSuit; suit++){
	                for(int rank = firstRank; rank <= lastRank; rank++){
	                	Card newCard = new Card(suit, rank);
	                    saveDecks.add(newCard);
	                    playDecks.add(newCard);
	                }
	            }
        	}
        }
        catch(PlayingCardException e){
        	System.out.println("PlayingCardException: " + e.getMessage());
        }
    }


    /**
     * Task: Shuffles cards in playDecks.
     * Hint: Look at java.util.Collections
     */
    public void shuffle(){
    	Collections.shuffle(playDecks);
    }


    /**
     * Task: Deals cards from the deal deck.
     *
     * @param numberCards number of cards to deal
     * @return a list containing cards that were dealt
     * @throw PlayingCardException if numberCards > number of remaining cards
     *
     * Note: You need to create ArrayList to stored dealt cards
     *       and remove dealt cards from playDecks
     *
     */
    public List<Card> deal(int numberCards) throws PlayingCardException{
        
        List<Card> dealtCards = new ArrayList<>();

        try{
        	if(numberCards > remain()){
        		throw new PlayingCardException("Not enough cards in deck");
        	}

		    //Iterate through playDecks, remove cards, and put them in the dealtCards List
		    for(int i = numberCards-1; i >= 0; i--){
		    	Card randomCard = playDecks.remove(i);
		    	dealtCards.add(randomCard);
        	}
		}
		catch(PlayingCardException e){
			System.out.println("PlayingCardException: " + e.getMessage());
		}


        return dealtCards;
    }


    /**
     * Task: Resets playedDeck by cpoying all cards from the saveDeck.
     */
    public void reset()
    {
        playDecks = saveDecks;

    }


    /**
     * Task: Return number of remaining cards in deal deck.
     */
    public int remain()
    {
	return playDecks.size();
    }

    /**
     * Task: Returns a string representing cards in the deal deck 
     */
    public String toString()
    {
	return ""+playDecks;
    }


    /* Quick test                   */
    /*                              */
    /* Do not modify these tests    */
    /* Generate 2 decks of cards    */
    /* Loop 2 times:                */
    /*   Deal 30 cards for 4 times  */
    /*   Expect exception last time */
    /*   reset()                    */

    public static void main(String args[]) {

        System.out.println("*******    Create 2 decks of cards      *********\n\n");
        Decks decks  = new Decks(2);
         
	for (int j=0; j < 2; j++)
	{
        	System.out.println("\n************************************************\n");
        	System.out.println("Loop # " + j + "\n");
		System.out.println("Before shuffle:"+decks.remain()+" cards");
		System.out.println("\n\t"+decks);
        	System.out.println("\n==============================================\n");

                int numHands = 4;
                int cardsPerHand = 30;

        	for (int i=0; i < numHands; i++)
	 	{
	    		decks.shuffle();
		        System.out.println("After shuffle:"+decks.remain()+" cards");
		        System.out.println("\n\t"+decks);
			try {
            		    System.out.println("\n\nHand "+i+":"+cardsPerHand+" cards");
            		    System.out.println("\n\t"+decks.deal(cardsPerHand));
            		    System.out.println("\n\nRemain:"+decks.remain()+" cards");
		            System.out.println("\n\t"+decks);
        	            System.out.println("\n==============================================\n");
			}
			catch (PlayingCardException e) 
			{
		 	        System.out.println("*** In catch block:PlayingCardException:Error Msg: "+e.getMessage());
			}
		}


		decks.reset();
	}
    }

}
