/**
 *	Ian Moreno
 *	08/31/2017
 *	Java Refresher Project
 *	CoinInterface interface specifies the signatures of the methods
 *  for a GenericCoin class
 */

public interface CoinInterface {
	/**
	 *	method returns boolean value representing whether the coin is
	 * heads-side-up or not
	 * @return true if coin is heads-side-up, otherwise returns false
	 */
	public boolean isHeadsUp();

	/**
	 * method allows user to flip the coin
	 */
	public void toss();
}
