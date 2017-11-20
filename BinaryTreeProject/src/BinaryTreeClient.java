import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BinaryTreeClient {
	private static BinarySearchTree<Character> morseCode = new BinarySearchTree<>();
	
	public static void main(String[] args)  throws FileNotFoundException {
		Scanner readInMorseCode = new Scanner(new File("MORSECODE"));
		Scanner readInTestMorseCode = new Scanner(new File("TESTMORSECODE"));
		
		// add the root to start the tree
		morseCode.add('*', 0, "*");;

		while (readInMorseCode.hasNext()) {
			morseCode.add(readInMorseCode.next().charAt(0), 0, readInMorseCode.next());
		}
		morseCode.printBinarySearchTree();
		
		// close Scanner input stream
		readInMorseCode.close();

		int i = 1;
		while (readInTestMorseCode.hasNext()) {
			String codeToDecode = readInTestMorseCode.nextLine();
			System.out.println(i++ + ": " + codeToDecode + "\n");
		}
		// close Scanner input stream
		readInTestMorseCode.close();
	}
	
	private static String decodeSentence(String sentence) {
		String sentenceDecoded = "";
		int numberOfWords = countWords(sentence);
		String[] words = new String[numberOfWords];

		
		return sentenceDecoded;
	}
	
	private static void separateWords(String[] words, String sentence) {
		StringBuilder tempWordString = new StringBuilder(); 
		int count = 0;
		
		for (int i = 0; i < sentence.length(); i++) {
			char tempChar = sentence.charAt(i);
			if (tempChar != '/') {
				tempWordString.append(tempChar);
			} else {
				words[count++] = tempWordString.toString();
				tempWordString = new StringBuilder();
			}
		}
		words[count++] = tempWordString.toString();
	}
	
	private static void separateLetters(String[] letters, String word) {
		StringBuilder tempLetterString = new StringBuilder(); 
		int count = 0;
		
		for (int i = 0; i < word.length(); i++) {
			char tempChar = word.charAt(i);
			if (tempChar != ' ') {
				tempLetterString.append(tempChar);
			} else {
				letters[count++] = tempLetterString.toString();
				tempLetterString = new StringBuilder();
			}
		}
		letters[count++] = tempLetterString.toString();
	}
	
	public static int countSpaces(String phrase) {
		int count = 0;
		for (int i = 0; i < phrase.length(); i++) {
			if(phrase.charAt(i) == ' ') 
				count++;
		}
		return count+1; //add one more to count the last letter
	}

	public static int countWords(String phrase) {
		int count = 0;
		for (int i = 0; i < phrase.length(); i++) {
		if (phrase.charAt(i) == '/') 
			count++;
		}
		return count+1; //add one more for the last word
	}
}
