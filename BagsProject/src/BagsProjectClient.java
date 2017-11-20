/**
 *	Ian Moreno
 *	09/17/2017
 *	Bag Programming Project
 *	purpose: BagsProjectClient class demonstrates the ResizableArrayBag and Coin classes
 *	Create 5 bags each full of a random number of randomly chosen coin types: penny, nickel, dime, or quarter
 *  Choose three coins from bag at random and then calculate the probability of choosing those coins, in that order
 *  without replacement. Then choose three new coins and calculate the probability of choosing those coins in that
 *  order with replacement.
 */

import java.util.*; 

public class BagsProjectClient {
	private final static int NUMBER_OF_CHOSEN_COINS = 3; //number of coins to be picked from bag
	private static ResizableArrayBag<Coin> coinBag = new ResizableArrayBag<>(0); // bag to hold random number of coins
	private static List<Coin> chosenCoins = new ArrayList<>(0); // bag to hold list of the three chosen coins
	private static final Random rand = new Random();

	// static int variables to keep track of frequency of each type of coin in coinBag
	private static int numberOfPennies = 0;
	private static int numberOfNickels = 0;
	private static int numberOfDimes = 0;
	private static int numberOfQuarters = 0;
	private static int numberOfCoins = 0;

	public static void main(String[] args) {
		final int MIN_NUMBER_OF_COINS= 8; // minimum amount of coins to be chosen
		final int MAX_NUMBER_OF_COINS = 20; // maximum amount of coins to be chosen
		float probability = 0.0f; // float variable for the probability of selecting coin sequence
		
		// create five different bags of coins
		for (int i = 0; i < 5; i++) {
			initializeCoinBag(MIN_NUMBER_OF_COINS, MAX_NUMBER_OF_COINS); // create the new coinBag with random capacity
			fillCoinBag(); // fill the coinBag with randomly chosen coin types
			calculateCoinFrequency(); // calculate the frequency of each coin type in coinBag
			
			System.out.println("**New Coin Bag Created**");
			printCoinFrequency();


			// begin probability analysis
			probability = calculateProbabilityWithoutReplacement();
			printChosenCoins();
			System.out.printf("Probability of choosing ( " + listOfChosenCoins() + ") without replacement : %,.3f%n", probability);

			probability = calculateProbabilityWithReplacement();
			printChosenCoins();
			System.out.printf("Probability of choosing ( " + listOfChosenCoins() + ") with replacement : %,.3f%n", probability);
			System.out.println("\n");
		}
	}
	
	/**
	 * purpose: private helper method creates new coinBag with a random capacity
	 * @param minNumberOfCoins int variable representing the minimum number of coins that can be in the coinBag
	 * @param maxNumberOfCoins int variable representing the maximum number of coins that can be in the coinBag
	 */
	private static void initializeCoinBag(int minNumberOfCoins, int maxNumberOfCoins) {
		numberOfCoins = (minNumberOfCoins + rand.nextInt(maxNumberOfCoins - minNumberOfCoins + 1)); // calculate capacity between min-max inclusive
		coinBag = new ResizableArrayBag<>(numberOfCoins); // initialize coinBag with randomly chosen capacity
	}
	
	/**
	 * purpose: private helper method prints formatted String representing the coin types frequencies
	 */
	private static void printCoinFrequency() {
		System.out.printf("Total Number of Coins: %d%nNumber of Pennies: %d%nNumber of Nickels: %d%nNumber of Dimes: %d%nNumber of Quarters: %d%n",
			numberOfCoins, numberOfPennies, numberOfNickels, numberOfDimes, numberOfQuarters);
	}
	
	/**
	 * purpose: private helper method prints list of three randomly chosen coins
	 */
	private static void printChosenCoins() {
		System.out.println("The following coins were randomly chosen: ");
		for (Coin aCoin : chosenCoins) {
			System.out.println(aCoin.getName() + " ");
		}
	}
	
	/**
	 * purpose: private helper method returns formatted String representing the three randomly chosen coins
	 * @return String representing the formatted list of the three randomly chosen coins
	 */
	private static String listOfChosenCoins() {
		StringBuilder listOfCoins = new StringBuilder(); 
		for (Coin aCoin : chosenCoins) {
			listOfCoins.append(aCoin.getName() + " ");
		}
		return listOfCoins.toString();
	}
	
	
	/**
	 * purpose: private helper method fills coinBag with the randomly chosen coin types
	 */
	private static void fillCoinBag() {
		CoinTypes[] arrOfCoinTypes = CoinTypes.values();
		// add random number of randomly selected coins to bag
		for (int i = 0; i < numberOfCoins; i++) {
			// choose random type of coin: PENNY, NICKEL, DIME, or QUARTER
			CoinTypes randomCoinType = arrOfCoinTypes[rand.nextInt(arrOfCoinTypes.length)];
			// add randomly chosen type of coin to the bag
			coinBag.add(new Coin(randomCoinType.toString(), randomCoinType.getValue()));
		}
	}
	
	/**
	 * purpose: private helper method calculates the frequencies of each type of coin
	 */
	private static void calculateCoinFrequency() {
		numberOfPennies = coinBag.getFrequencyOf(new Coin("PENNY", 0.01f)); 
		numberOfNickels = coinBag.getFrequencyOf(new Coin("NICKEL", 0.05f));
		numberOfDimes = coinBag.getFrequencyOf(new Coin("DIME", 0.10f));
		numberOfQuarters = coinBag.getFrequencyOf(new Coin("QUARTER", 0.25f));
	}
	
	/**
	 * purpose: private helper method picks coins from the coinBag and removes them from subsequent selection
	 * adds removed coin to the list of chosenCoins
	 */
	private static void pickAndRemoveCoinsFromBag() {
		chosenCoins.clear(); // clear list of previously chosen coins
		// choose random coins
		for (int j = 0; j < NUMBER_OF_CHOSEN_COINS; j++) {
			chosenCoins.add(coinBag.remove()); // remove coin from coinBag and add it to chosenCoins
		}

	}
	
	/**
	 * purpose: private helper method picks coins from the coinBag and puts them back for subsequent selection
	 * adds chosen coin to the list of chosenCoins
	 */
	private static void pickAndReplaceCoinsFromBag() {
		chosenCoins.clear(); // clear list of previously chosen coins
		Object[] coinArr = coinBag.toArray(); // create Object array copy of coinBag
		// choose random coins
		for (int j = 0; j < NUMBER_OF_CHOSEN_COINS; j++) {
			// choose random Object from coinArr and cast it to Coin object and then add it to chosenCoins list
			chosenCoins.add((Coin)coinArr[rand.nextInt(coinArr.length)]); 
		}

	}

	/**
	 * purpose: private helper method calculates the probability of selecting the sequence of the three chosen coins
	 * without replacement
	 * @return float variable representing the probability of chosing the sequence of coins without replacement
	 */
	private static float calculateProbabilityWithoutReplacement() {
		pickAndRemoveCoinsFromBag(); 
		// determine frequency of each type of coin

		float probability = 1.0f; // initialize probability variable

		// iterate through each of the Coins in chosenCoins
		for (Coin aCoin : chosenCoins) {
			// calculate the probability of choosing each type of coin 
			// remove 1 corresponding coin type from the coinBag and decrement numberOfCoins in coinBag by 1
			switch (aCoin.getName()) {
			case "PENNY" :
				probability = (float)(probability * numberOfPennies-- / numberOfCoins--);
				break;
			case "NICKEL" :
				probability = (float)(probability * numberOfNickels-- / numberOfCoins--);
				break;
			case "DIME" :
				probability = (float)(probability * numberOfDimes-- / numberOfCoins--);
				break;
			case "QUARTER" :
				probability = (float)(probability * numberOfQuarters-- / numberOfCoins--);
				break;
			default :
				break;
			}
		}
		return probability;
	}

	/**
	 * purpose: private helper method calculates the probability of selecting the sequence of the three chosen coins
	 * with replacement
	 * @return float variable representing the probability of choosing the sequence of coins with replacement
	 */
	private static float calculateProbabilityWithReplacement() {
		pickAndReplaceCoinsFromBag();
		// determine frequency of each type of coin

		float probability = 1.0f; // initialize probability variable

		// iterate through each of the Coins in chosenCoins
		for (Coin aCoin : chosenCoins) {
			// calculate the probability of choosing each type of coin 
			switch (aCoin.getName()) {
			case "PENNY" :
				probability = (float)(probability * numberOfPennies / numberOfCoins);
				break;
			case "NICKEL" :
				probability = (float)(probability * numberOfNickels / numberOfCoins);
				break;
			case "DIME" :
				probability = (float)(probability * numberOfDimes / numberOfCoins);
				break;
			case "QUARTER" :
				probability = (float)(probability * numberOfQuarters / numberOfCoins);
				break;
			default :
				break;
			}
		}
		return probability;
	}

	/**
	 * purpose: private enum CoinTypes used to create named constants of each type of coin to potentially be randomly added
	 * to coinBag. Creates a name:value pair for each coin type for easier iteration.
	 */
	private enum CoinTypes {
		// initialize coin types with corresponding dollar values
		PENNY(0.01f), NICKEL(0.05f), DIME(0.10f), QUARTER(0.25f);
		
		private final float value;
		
		/**
		 * purpose: constructor to initialize coin types and their corresponding values
		 * @param val float variable representing the coin's dollar value
		 */
		CoinTypes(float val) {
			this.value = val;
		}

		/**
		 * purpose: getter method returns corresponding dollar value of coin type
		 * @return float variable representing coin type's corresponding dollar value
		 */
		public float getValue() {
			return this.value;
		}
	}
}

/** OUTPUT
	**New Coin Bag Created**
	Total Number of Coins: 13
	Number of Pennies: 3
	Number of Nickels: 2
	Number of Dimes: 5
	Number of Quarters: 3
	The following coins were randomly chosen: 
	NICKEL 
	PENNY 
	DIME 
	Probability of choosing ( NICKEL PENNY DIME ) without replacement : 0.017
	The following coins were randomly chosen: 
	NICKEL 
	PENNY 
	PENNY 
	Probability of choosing ( NICKEL PENNY PENNY ) with replacement : 0.004


	**New Coin Bag Created**
	Total Number of Coins: 11
	Number of Pennies: 5
	Number of Nickels: 0
	Number of Dimes: 1
	Number of Quarters: 5
	The following coins were randomly chosen: 
	DIME 
	PENNY 
	QUARTER 
	Probability of choosing ( DIME PENNY QUARTER ) without replacement : 0.025
	The following coins were randomly chosen: 
	QUARTER 
	PENNY 
	PENNY 
	Probability of choosing ( QUARTER PENNY PENNY ) with replacement : 0.125


	**New Coin Bag Created**
	Total Number of Coins: 20
	Number of Pennies: 4
	Number of Nickels: 1
	Number of Dimes: 6
	Number of Quarters: 9
	The following coins were randomly chosen: 
	DIME 
	QUARTER 
	QUARTER 
	Probability of choosing ( DIME QUARTER QUARTER ) without replacement : 0.063
	The following coins were randomly chosen: 
	DIME 
	PENNY 
	QUARTER 
	Probability of choosing ( DIME PENNY QUARTER ) with replacement : 0.028


	**New Coin Bag Created**
	Total Number of Coins: 10
	Number of Pennies: 5
	Number of Nickels: 2
	Number of Dimes: 2
	Number of Quarters: 1
	The following coins were randomly chosen: 
	QUARTER 
	PENNY 
	NICKEL 
	Probability of choosing ( QUARTER PENNY NICKEL ) without replacement : 0.014
	The following coins were randomly chosen: 
	PENNY 
	PENNY 
	PENNY 
	Probability of choosing ( PENNY PENNY PENNY ) with replacement : 0.187


	**New Coin Bag Created**
	Total Number of Coins: 10
	Number of Pennies: 2
	Number of Nickels: 4
	Number of Dimes: 1
	Number of Quarters: 3
	The following coins were randomly chosen: 
	NICKEL 
	NICKEL 
	PENNY 
	Probability of choosing ( NICKEL NICKEL PENNY ) without replacement : 0.033
	The following coins were randomly chosen: 
	DIME 
	PENNY 
	PENNY 
	Probability of choosing ( DIME PENNY PENNY ) with replacement : 0.003 
*/