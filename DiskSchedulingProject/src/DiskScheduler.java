import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class DiskScheduler {
	private Random rand = new Random();
	private List<Integer> diskRequestsList = new ArrayList<>();
	private int headPosition = 0;
	private int requestToSatisfy = 0;
	private int SSTFtotalHeadMovement = 0;
	private int SCANtotalHeadMovement = 0;
	private int CSCANtotalHeadMovement = 0;
	private int CLOOKtotalHeadMovement = 0;

	public DiskScheduler() {
		// initialize head position to random number between 0-500
		headPosition = rand.nextInt(501);		

		// initialize list of randomized disk requests 0-500
		buildRandomRequestsList();

		// assign last item in diskRequestsList as the disk request for the process
		requestToSatisfy = diskRequestsList.get(diskRequestsList.size()-1);
		
		SSTF();
		SCAN();
		CSCAN();
		CLOOK();
	}
	
	private void SSTF() {
		// temporary request list to mutate
		List<Integer> tempRequestsList = new ArrayList<>(diskRequestsList);
		boolean satisfied = false;
		Integer tempReq = null;
		Integer shortestReq = null;
		Integer tempHeadPos = headPosition;
		
		while (!satisfied) {
			for (Integer i : tempRequestsList) {
				tempReq = i;
				
				if (shortestReq == null)
					shortestReq = tempReq;
				else if (getDistance(tempHeadPos, tempReq) < getDistance(tempHeadPos, shortestReq))
					shortestReq = tempReq;
			}
			SSTFtotalHeadMovement += getDistance(tempHeadPos, shortestReq);
			tempHeadPos = shortestReq;
			tempRequestsList.remove(shortestReq);
			shortestReq = null;
			
			if (tempHeadPos.intValue() == requestToSatisfy)
				satisfied = true;
		}
	}

	private void SCAN() {
		// temporary request list to mutate
		List<Integer> tempRequestsList = new ArrayList<>(diskRequestsList);
		boolean satisfied = false;
		Integer tempReq = null;
		Integer shortestReq = null;
		Integer tempHeadPos = headPosition;
		
		while (!satisfied) {
			for (Integer i : tempRequestsList) {
				tempReq = i;
				
				if (shortestReq == null)
					shortestReq = tempReq;
				else if (getDistance(tempHeadPos, tempReq) < getDistance(tempHeadPos, shortestReq))
					shortestReq = tempReq;
			}
			SCANtotalHeadMovement += getDistance(tempHeadPos, shortestReq);
			tempHeadPos = shortestReq;
			tempRequestsList.remove(shortestReq);
			shortestReq = null;
			
			if (tempHeadPos.intValue() == requestToSatisfy)
				satisfied = true;
		}
	}
	
	private void CSCAN() {
		// temporary request list to mutate
		List<Integer> tempRequestsList = new ArrayList<>(diskRequestsList);
		boolean satisfied = false;
		Integer tempReq = null;
		Integer shortestReq = null;
		Integer tempHeadPos = headPosition;
		
		while (!satisfied) {
			for (Integer i : tempRequestsList) {
				tempReq = i;
				
				if (shortestReq == null)
					shortestReq = tempReq;
				else if (getDistance(tempHeadPos, tempReq) < getDistance(tempHeadPos, shortestReq))
					shortestReq = tempReq;
			}
			CSCANtotalHeadMovement += getDistance(tempHeadPos, shortestReq);
			tempHeadPos = shortestReq;
			tempRequestsList.remove(shortestReq);
			shortestReq = null;
			
			if (tempHeadPos.intValue() == requestToSatisfy)
				satisfied = true;
		}
	}
	
	private void CLOOK() {
		// temporary request list to mutate
		List<Integer> tempRequestsList = new ArrayList<>(diskRequestsList);
		boolean satisfied = false;
		Integer tempReq = null;
		Integer shortestReq = null;
		Integer tempHeadPos = headPosition;
		
		while (!satisfied) {
			for (Integer i : tempRequestsList) {
				tempReq = i;
				
				if (shortestReq == null)
					shortestReq = tempReq;
				else if (getDistance(tempHeadPos, tempReq) < getDistance(tempHeadPos, shortestReq))
					shortestReq = tempReq;
			}
			CLOOKtotalHeadMovement += getDistance(tempHeadPos, shortestReq);
			tempHeadPos = shortestReq;
			tempRequestsList.remove(shortestReq);
			shortestReq = null;
			
			if (tempHeadPos.intValue() == requestToSatisfy)
				satisfied = true;
		}
	}
	
	private int getDistance(int a, int b) {
		return Math.abs(a - b);
	}

	private void buildRandomRequestsList() {
		// initialize the Integer variable outside of loop
		Integer num = 0;
		// initialize boolean switch to ensure we don't add duplicates to the diskRequestsList
		boolean isPresentInList = false;

		// build list of 10 unique random numbers between 0-500
		for (int i = 0; i < 10; i++) {
			do { // iterate at least once
				num = Integer.valueOf(rand.nextInt(501)); // random number between 0-500
				if (diskRequestsList.contains(num)) {
					isPresentInList = true; // not unique, continue do-while to generate a new number
				} else {
					isPresentInList = false; // unique, add to list and continue for loop iterations
				}
			} while (isPresentInList); // if the number is already present in the list, generate a new num

			diskRequestsList.add(num);
		}
	}
	
	private void printRandomRequestsList() {
		for (Integer i : diskRequestsList) {
			System.out.println(i);
		}
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		String[] algorithms = {" SSTF ", " SCAN ", "C-SCAN", "C-LOOK"};
		int[] headMovements = {SSTFtotalHeadMovement, SCANtotalHeadMovement, CSCANtotalHeadMovement, CLOOKtotalHeadMovement};

		for (int i = 0; i < 4; i++) {
			str.append("**" + algorithms[i] + "**\tRequest List: " + diskRequestsList);
			str.append("  Head Position: " + headPosition);
			str.append("\tTotal Head Movement: " + headMovements[i] + "\n");
		}

		return str.toString();
	}
}

