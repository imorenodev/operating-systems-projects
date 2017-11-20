/**
 *	Ian Moreno
 *	08/31/2017
 *	Java Refresher Project
 *	Coin class extends Genericoin and allows creation of a coin
 *	object that has a monetary value and a name.
 */

public class Coin extends GenericCoin {
	// variable representing coin's dollar value
	private float value = 0.0f;
	// variable representing coin's name
	private String name = "";

	//default no-arg constructor
	public Coin() {
		super();
		value  = 0.01f;
		name = "penny";
	}

	/**
	 * constructor takes in two arguments
	 * @param aName is a String variable representing coin's name
	 * @param aValue is a float variable representing coin's dollar value
	 */
	public Coin(String aName, float aValue) {
		super();
		name = aName;
		value = aValue;
	}

	/**
	 *	getter method returns coin's value
	 * @return float representing coin's value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * getter method returns coin's name
	 * @return String representing coin's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter method sets coin's dollar value
	 * @param aValue is a float variable representing coin's dollar value
	 */
	public void setValue(float aValue) {
		value = aValue;
	}

	/**
	 * setter method sets coin's name
	 * @param aName is a String variable representing coin's name
	 */
	public void setName(String aName) {
		name = aName;
	}
	
	/**
	 * override equals method to compare two coin objects for equality
	 * @param aCoin of type Object to be cast as a Coin object to compare name and value.
	 * parameter must be of type Object in order to override the Object superclass's equals() method
	 * @return true if coins are equal, otherwise false
	 */
	@Override
	public boolean equals(Object aCoin) { 		
		Coin tempCoin = (Coin)aCoin;
		if (tempCoin instanceof Coin) {
			if (tempCoin.getName().equalsIgnoreCase(getName()) &&
			    tempCoin.getValue() == getValue()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * override toString method to provide more meaningful output
	 * @return a String value representing the coin's name and dollar value
	 */
	public String toString() {
		return String.format("%s $%,.2f%n",getName(), getValue());
	}
}
