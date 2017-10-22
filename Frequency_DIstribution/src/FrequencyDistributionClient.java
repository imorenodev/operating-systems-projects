import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Ian Moreno
 * @date 10/06/2017
 * @purpose The FrequencyDistributionClient class demonstrates the Frequency Distribution class
 * 			Calculates and displays the values 1...5 at a frequency of 5%, 10%, 15%, 20%, and 50%.
 */

public class FrequencyDistributionClient {
	public static void main(String[] args) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt")); 
		int[] freqArr = new int[100];
		int[] positionArr = new int[100];

		for (int i = 0; i < 100; i++) {
			freqArr[i] = FrequencyDistribution.getValue();
			positionArr[i] = i+1;
		}
		
		int[] freqCountArr = FrequencyDistribution.calculateFrequency(freqArr);
		
		System.out.println("Return Value\tFrequency");
		writer.write("Return Value\t\tFrequency");
		writer.newLine();

		for (int j = 0; j < 5; j++) {
			System.out.println((j+1) + "\t\t" + freqCountArr[j] + "%");
			writer.write((j+1) + "\t\t\t\t" + freqCountArr[j] + "%");
			writer.newLine();
		}
		writer.close();
	}
}

class FrequencyDistribution {
	public static int getValue() {
		Random rand = new Random();
		double num = rand.nextDouble();
		
		if (num <= 0.05) 
			return 1;
		else if (num <= 0.15)
			return 2;
		else if (num <= 0.30)
			return 3;
		else if (num <= 0.5)
			return 4;
		else 
			return 5;
	}
	
	public static int[] calculateFrequency(int[] freqArr) {
		int[] freqCountArr = new int[5];
		int indexVal = 0;
		Arrays.sort(freqArr);
		
		for (int i = 0; i < freqArr.length; i++) {
			indexVal = freqArr[i];
			
			switch (indexVal) {
			case 1 :
				freqCountArr[0]++;
				break;
			case 2 :
				freqCountArr[1]++;
				break;
			case 3 :
				freqCountArr[2]++;
				break;
			case 4 :
				freqCountArr[3]++;
				break;
			default :
				freqCountArr[4]++;
			}
		}
		return freqCountArr;
	}
}

/**
 *  OUTPUT
   
Return Value		Frequency
1				4%
2				8%
3				15%
4				24%
5				49%

*/