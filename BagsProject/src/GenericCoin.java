/**
 *	Ian Moreno
 *	08/31/2017
 *	Java Refresher Project
 *	GenericCoin class implements CoinInterface and allows creation of
 * 	a two-sided coin
 */

public class GenericCoin implements CoinInterface {
	// if upSide == 0 the coin is heads-side-up, else coin is tails-side-up
	private int upSide = 0;
	
	public GenericCoin() {
		upSide = 0;
	}

	/**
	 * method returns whether coin is heads-side-up or not
	 * @return
	 */
	public boolean isHeadsUp() {
		return ((upSide == 0) ? true : false);
	}

	/**
	 * method allows user to flip the coin and randomly sets the upside to 0 (heads) or 1 (tails)
	 */
	public void toss() {
		upSide = (int) Math.round((1 - Math.random()));
	}
}
