import java.util.*;

/**
 * @author Ian Moreno
 * @date 11/11/2017
 * @Purpose The HashingExample Class demonstrates linear probing, quadratic probing, and double hashing methods of solving hash table collisions
 */

public class HashingExample 
{
	public static Entry<Integer, Integer>[] hashTable = new Entry[101];
	public static int j = 1; // make j public for quadratic probing method so that it isn't reassigned to 0 for each iteration of the forEach loop

	public static void main(String[] args) 
	{
		
		// list of Integer values
		List<Integer> intValues = new ArrayList<Integer>();
		intValues.add(627);
		intValues.add(378); 
		intValues.add(549);
		intValues.add(432);
		intValues.add(148); 
		intValues.add(121);
		intValues.add(528); 
		intValues.add(402); 
		intValues.add(246); 
		intValues.add(599); 
		intValues.add(85); 
		intValues.add(359); 
		intValues.add(195); 
		intValues.add(345); 
		intValues.add(212); 
		intValues.add(187); 
		intValues.add(525); 
		intValues.add(393); 
		intValues.add(632); 
		intValues.add(143);
		
		for (Integer value : intValues)
		{
			int index = getHashIndex(value);
			if(hashTable[index] == null)
			{
				Integer key = index;
				Entry<Integer, Integer> item = new Entry<Integer, Integer>(key, value);
				hashTable[index] = item;
			}
			else
			{
				//deal with collision
				//linearProbing(index, value);
				//quadraticProbing(index, value);
				doubleHashing(index, value);
				
			}
			System.out.printf("%20d%20d \n",value, getHashIndex(value));  // shows the result of getHashIndex
			System.out.printf("%20d%20d \n",value, value.hashCode());    // shows the hashCode() method of String
			System.out.printf("%20s%20d \n\n",value, hashcode(value));     // shows that the hashcode method below returns the same as class method
		}
		System.out.println("******************************************************************");
		System.out.printf("%20s%20s\n\n", "Index", "Value");
		for(Entry<Integer, Integer> e : hashTable)
		{
			if(e != null)
				System.out.printf("%20d%20s\n", (int)e.getKey(), e.getValue());
		}

	}
	/*
	 * Calculates hash code for an Integer
	 * @param s - value to be hashed
	 * @return hashcode of value
	 */
	public static int hashcode(Integer value)
	{
		int hash = 0;
		int n = value;
		for(int i = 0; i < n; i++)
			hash = 31 * hash + value;
		return hash;
	}
	/*
	 * Makes hashCode a viable index for an array
	 * @param key - value to hash
	 * @return index to store value
	 */
	private static int getHashIndex(Integer key)
	{
		int hashIndex = key.hashCode() % hashTable.length;
		if(hashIndex < 0)
			hashIndex = hashIndex + hashTable.length;
		return hashIndex;
	}
	/*
	 * Resolving hashing collision with linear probing
	 * increase index by 1 until open spot in array is found
	 * @param index - original hash index
	 * @param n - original value
	 */
	public static void linearProbing(int index, Integer n)
	{
		System.out.println("Solving Collision with Linear Probing");
		while(index < hashTable.length)
		{
			index++;
			if(index >= hashTable.length) //when index reaches end of array wrap around to beginning
				index = 0;
			
			if(hashTable[index] == null)
			{
				Integer key = index;
				Entry<Integer, Integer> item = new Entry<Integer, Integer>(key, n);
				hashTable[index] = item;
				return;
			}
			
			
		}
	}
	/*
	 * Resolves hashing collisions with quadratic probing
	 * adds j^2 to original hash index, for j >= 0
	 * @param index - original hash index
	 * @param n - original value
	 */
	public static void quadraticProbing(int index, Integer n)
	{
		System.out.println("Solving Collision with Quadratic Probing");

		while(index < hashTable.length)
		{
			index += (j * j);
			j++;

			if(index >= hashTable.length) //when index reaches end of array wrap around to beginning
				index = 0;
			
			if(hashTable[index] == null)
			{
				Integer key = index;
				Entry<Integer, Integer> item = new Entry<Integer, Integer>(key, n);
				hashTable[index] = item;
				return;
			}
			//j++;
		}
	}
		/*
		 * Resolves hashing collisions by Double Hashing
		 * hashes value and does a second hash to determine length
		 * of span between indexes to check 
		 * 
		 * @param index - original hash index
		 * @param n - original value
		 */
		public static void doubleHashing(int index, Integer n)
		{
			System.out.println("Solving Collision with Double Hashing");
			int increment = 5 - index % 5;
			while(index < hashTable.length)
			{
				index+=increment;
				if(index >= hashTable.length) //when index reaches end of array wrap around to beginning
					index = 0;
				
				if(hashTable[index] == null)
				{
					Integer key = index;
					Entry<Integer, Integer> item = new Entry<Integer, Integer>(key, n);
					hashTable[index] = item;
					return;
				}
				
				
			}
		}	
	
	
	private static class Entry<S, T>
	{
		private S key;
		private T value;
		
		private Entry(S searchKey, T dataValue)
		{
			key = searchKey;
			value = dataValue;
		}
		private S getKey()
		{
			return key;
		}
		private T getValue()
		{
			return value;
		}
		private void setValue(T newValue)
		{
			value = newValue;
		}
	}
}

/**
 * EXAMPLE OUTPUT
 * 
 * Linear Probing:
 *                  627                  21 
                 627                 627 
                 627          1343765267 

                 378                  75 
                 378                 378 
                 378          1789133376 

                 549                  44 
                 549                 549 
                 549          -706322715 

                 432                  28 
                 432                 432 
                 432          1989120000 

                 148                  47 
                 148                 148 
                 148           951793920 

                 121                  20 
                 121                 121 
                 121          1949545721 

                 528                  23 
                 528                 528 
                 528          1385566208 

                 402                  99 
                 402                 402 
                 402          -691981248 

Solving Collision with Linear Probing
                 246                  44 
                 246                 246 
                 246         -1242532288 

                 599                  94 
                 599                 599 
                 599         -1977447753 

                  85                  85 
                  85                  85 
                  85          1878233621 

                 359                  56 
                 359                 359 
                 359           252255431 

Solving Collision with Linear Probing
                 195                  94 
                 195                 195 
                 195         -1116024733 

                 345                  42 
                 345                 345 
                 345         -1630779431 

                 212                  10 
                 212                 212 
                 212          2013845760 

                 187                  86 
                 187                 187 
                 187           836751067 

Solving Collision with Linear Probing
                 525                  20 
                 525                 525 
                 525          1570220109 

                 393                  90 
                 393                 393 
                 393           959161609 

                 632                  26 
                 632                 632 
                 632          -255065088 

Solving Collision with Linear Probing
                 143                  42 
                 143                 143 
                 143          -911734929 

******************************************************************
               Index               Value

                  10                 212
                  20                 121
                  21                 627
                  22                 525
                  23                 528
                  26                 632
                  28                 432
                  42                 345
                  43                 143
                  44                 549
                  45                 246
                  47                 148
                  56                 359
                  75                 378
                  85                  85
                  86                 187
                  90                 393
                  94                 599
                  95                 195
                  99                 402
                  
 * 
 * Quadratic Probing:
 * 
 *                  627                  21 
                 627                 627 
                 627          1343765267 

                 378                  75 
                 378                 378 
                 378          1789133376 

                 549                  44 
                 549                 549 
                 549          -706322715 

                 432                  28 
                 432                 432 
                 432          1989120000 

                 148                  47 
                 148                 148 
                 148           951793920 

                 121                  20 
                 121                 121 
                 121          1949545721 

                 528                  23 
                 528                 528 
                 528          1385566208 

                 402                  99 
                 402                 402 
                 402          -691981248 

Solving Collision with Quadratic Probing
                 246                  44 
                 246                 246 
                 246         -1242532288 

                 599                  94 
                 599                 599 
                 599         -1977447753 

                  85                  85 
                  85                  85 
                  85          1878233621 

                 359                  56 
                 359                 359 
                 359           252255431 

Solving Collision with Quadratic Probing
                 195                  94 
                 195                 195 
                 195         -1116024733 

                 345                  42 
                 345                 345 
                 345         -1630779431 

                 212                  10 
                 212                 212 
                 212          2013845760 

                 187                  86 
                 187                 187 
                 187           836751067 

Solving Collision with Quadratic Probing
                 525                  20 
                 525                 525 
                 525          1570220109 

                 393                  90 
                 393                 393 
                 393           959161609 

                 632                  26 
                 632                 632 
                 632          -255065088 

Solving Collision with Quadratic Probing
                 143                  42 
                 143                 143 
                 143          -911734929 

******************************************************************
               Index               Value

                  10                 212
                  20                 121
                  21                 627
                  23                 528
                  26                 632
                  28                 432
                  29                 525
                  42                 345
                  44                 549
                  45                 246
                  47                 148
                  56                 359
                  58                 143
                  75                 378
                  85                  85
                  86                 187
                  90                 393
                  94                 599
                  98                 195
                  99                 402
                  
 *
 * Double Hashing:
 *                  627                  21 
                 627                 627 
                 627          1343765267 

                 378                  75 
                 378                 378 
                 378          1789133376 

                 549                  44 
                 549                 549 
                 549          -706322715 

                 432                  28 
                 432                 432 
                 432          1989120000 

                 148                  47 
                 148                 148 
                 148           951793920 

                 121                  20 
                 121                 121 
                 121          1949545721 

                 528                  23 
                 528                 528 
                 528          1385566208 

                 402                  99 
                 402                 402 
                 402          -691981248 

Solving Collision with Double Hashing
                 246                  44 
                 246                 246 
                 246         -1242532288 

                 599                  94 
                 599                 599 
                 599         -1977447753 

                  85                  85 
                  85                  85 
                  85          1878233621 

                 359                  56 
                 359                 359 
                 359           252255431 

Solving Collision with Double Hashing
                 195                  94 
                 195                 195 
                 195         -1116024733 

                 345                  42 
                 345                 345 
                 345         -1630779431 

                 212                  10 
                 212                 212 
                 212          2013845760 

                 187                  86 
                 187                 187 
                 187           836751067 

Solving Collision with Double Hashing
                 525                  20 
                 525                 525 
                 525          1570220109 

                 393                  90 
                 393                 393 
                 393           959161609 

                 632                  26 
                 632                 632 
                 632          -255065088 

Solving Collision with Double Hashing
                 143                  42 
                 143                 143 
                 143          -911734929 

******************************************************************
               Index               Value

                  10                 212
                  20                 121
                  21                 627
                  23                 528
                  25                 525
                  26                 632
                  28                 432
                  42                 345
                  44                 549
                  45                 246
                  47                 148
                  48                 143
                  56                 359
                  75                 378
                  85                  85
                  86                 187
                  90                 393
                  94                 599
                  95                 195
                  99                 402
 */
