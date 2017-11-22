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
		
		if (headPosition != requestToSatisfy) {
			SSTF();
			SCAN();
			CSCAN();
			CLOOK();
		} 
	}
	
	private void SSTF() {
		// temporary request list to mutate
		List<Integer> tempRequestsList = new ArrayList<>(diskRequestsList);
		boolean satisfied = false;
		Integer tempReq = null;
		Integer nearestReq = null;
		Integer tempHeadPos = headPosition;
		
		while (!satisfied) {
			for (Integer i : tempRequestsList) {
				tempReq = i;
				
				if (nearestReq == null)
					nearestReq = tempReq;
				else if (getDistance(tempHeadPos, tempReq) < getDistance(tempHeadPos, nearestReq))
					nearestReq = tempReq;
			}
			SSTFtotalHeadMovement += getDistance(tempHeadPos, nearestReq);
			tempHeadPos = nearestReq;
			tempRequestsList.remove(nearestReq);
			nearestReq = null;
			
			if (tempHeadPos.intValue() == requestToSatisfy)
				satisfied = true;
		}
	}

	private void SCAN() {
		// temporary request list to mutate
		List<Integer> tempRequestsList = new ArrayList<>(diskRequestsList);
		// sort reqs list
		Collections.sort(tempRequestsList);
		boolean satisfied = false;
		// initialize nearest largest req to the last index of the list in case the head position is greater than all the reqs in the list
		Integer lastReqInList = tempRequestsList.get(tempRequestsList.size()-1); 
		Integer upperBound = null;
		Integer lowerBound = null;
		Integer tempHeadPos = new Integer(headPosition);
		int indexOfUpperBound = 0;
	

		while (!satisfied) {
			if (tempHeadPos > lastReqInList) {
				// head is positioned past the last/largest req in the list so set lowerBound to the last element in list
				lowerBound = lastReqInList;
				upperBound = Integer.valueOf(500);
			} else { // find the upper bound req relative to the head position
				while (indexOfUpperBound < tempRequestsList.size()) {
					if (tempHeadPos < tempRequestsList.get(indexOfUpperBound)) {
						upperBound = new Integer(tempRequestsList.get(indexOfUpperBound));
						
						if (indexOfUpperBound > 0) 
							lowerBound = new Integer(tempRequestsList.get(indexOfUpperBound-1));
						else
							lowerBound = new Integer(0);

						break;
					}
					indexOfUpperBound++;
				}
			}

			if (getDistance(tempHeadPos, lowerBound) < getDistance(tempHeadPos, upperBound)) {
				// go left
				for (int i = tempRequestsList.indexOf(lowerBound)-1; i > 0; i--) {
					SCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
					tempHeadPos = new Integer(lowerBound);
					//tempRequestsList.remove(lowerBound);
					lowerBound = new Integer(tempRequestsList.get(i));
					if (tempHeadPos.intValue() == requestToSatisfy)
						return;
				}
				SCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
				tempHeadPos = new Integer(lowerBound);
				//tempRequestsList.remove(lowerBound);
				lowerBound = new Integer(0);
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				SCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
				tempHeadPos = new Integer(0);
				upperBound = new Integer(tempRequestsList.get(indexOfUpperBound));
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				// at 0 now go right
				for (int j = indexOfUpperBound+1; j < tempRequestsList.size()-1; j++) {
					SCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
					tempHeadPos = new Integer(upperBound);
					upperBound = new Integer(tempRequestsList.get(j));
					if (tempHeadPos.intValue() == requestToSatisfy)
						return;
				}
				SCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
				tempHeadPos = new Integer(upperBound);
				upperBound = Integer.valueOf(500);
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;			
				
				SCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
				
			} else {
				// go right
				for (int i = tempRequestsList.indexOf(upperBound)+1; i < tempRequestsList.size()-1; i++) {
					SCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
					tempHeadPos = new Integer(upperBound);
					//tempRequestsList.remove(upperBound);
					upperBound = new Integer(tempRequestsList.get(i));
					if (tempHeadPos.intValue() == requestToSatisfy)
						return;
				}
				SCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
				tempHeadPos = new Integer(upperBound);
				//tempRequestsList.remove(upperBound);
				upperBound = new Integer(500);
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				SCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
				tempHeadPos = new Integer(500);
				lowerBound = new Integer(tempRequestsList.get(indexOfUpperBound-1));
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				// at 5000 now go left 
				for (int j = indexOfUpperBound-2; j >= 0; j--) {
					SCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
					tempHeadPos = new Integer(lowerBound);
					lowerBound = new Integer(tempRequestsList.get(j));
					if (tempHeadPos.intValue() == requestToSatisfy)
						return;
				}
				SCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
				tempHeadPos = new Integer(lowerBound);
				lowerBound = Integer.valueOf(0);
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;			
				
				SCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);	
			}
			return;
		}
	}
	
	private void CSCAN() {
		// temporary request list to mutate
		List<Integer> tempRequestsList = new ArrayList<>(diskRequestsList);
		// sort reqs list
		Collections.sort(tempRequestsList);
		boolean satisfied = false;
		// initialize nearest largest req to the last index of the list in case the head position is greater than all the reqs in the list
		Integer lastReqInList = tempRequestsList.get(tempRequestsList.size()-1); 
		Integer upperBound = null;
		Integer lowerBound = null;
		Integer tempHeadPos = new Integer(headPosition);
		int indexOfUpperBound = 0;
	

		while (!satisfied) {
			if (tempHeadPos > lastReqInList) {
				// head is positioned past the last/largest req in the list so set lowerBound to the last element in list
				lowerBound = lastReqInList;
				upperBound = Integer.valueOf(500);
			} else { // find the upper bound req relative to the head position
				while (indexOfUpperBound < tempRequestsList.size()) {
					if (tempHeadPos < tempRequestsList.get(indexOfUpperBound)) {
						upperBound = new Integer(tempRequestsList.get(indexOfUpperBound));
						
						if (indexOfUpperBound > 0) 
							lowerBound = new Integer(tempRequestsList.get(indexOfUpperBound-1));
						else
							lowerBound = new Integer(0);

						break;
					}
					indexOfUpperBound++;
				}
			}

			if (getDistance(tempHeadPos, lowerBound) < getDistance(tempHeadPos, upperBound)) {
				// go left
				for (int i = tempRequestsList.indexOf(lowerBound)-1; i > 0; i--) {
					CSCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
					tempHeadPos = new Integer(lowerBound);
					//tempRequestsList.remove(lowerBound);
					lowerBound = new Integer(tempRequestsList.get(i));
					if (tempHeadPos.intValue() == requestToSatisfy)
						return;
				}
				CSCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
				tempHeadPos = new Integer(lowerBound);
				//tempRequestsList.remove(lowerBound);
				lowerBound = new Integer(0);
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				CSCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				// at 0 now go to end and conitnue to go left

				tempHeadPos = new Integer(500);
				lowerBound = new Integer(tempRequestsList.get(tempRequestsList.size()-1));
				
				// go left
				for (int i = tempRequestsList.indexOf(lowerBound)-1; i > indexOfUpperBound; i--) {
					CSCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
					tempHeadPos = new Integer(lowerBound);
					//tempRequestsList.remove(lowerBound);
					lowerBound = new Integer(tempRequestsList.get(i));
					if (tempHeadPos.intValue() == requestToSatisfy)
						return;
				}

				CSCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
				tempHeadPos = new Integer(lowerBound);
				//tempRequestsList.remove(lowerBound);
				lowerBound = new Integer(tempRequestsList.get(indexOfUpperBound));
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				CSCANtotalHeadMovement += getDistance(tempHeadPos, lowerBound);
			} else {
				// go right

				for (int i = tempRequestsList.indexOf(upperBound)+1; i < tempRequestsList.size()-1; i++) {
					CSCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
					tempHeadPos = new Integer(upperBound);
					upperBound = new Integer(tempRequestsList.get(i));
					if (tempHeadPos.intValue() == requestToSatisfy)
						return;
				}
				CSCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
				tempHeadPos = new Integer(upperBound);
				upperBound = new Integer(500);
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				CSCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				// at 500 now go to beginning and continue to go right 

				tempHeadPos = new Integer(0);
				upperBound = new Integer(tempRequestsList.get(0));
				
				// go right 
				for (int i = tempRequestsList.indexOf(upperBound)+1; i < indexOfUpperBound; i++) {
					CSCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
					tempHeadPos = new Integer(upperBound);
					upperBound = new Integer(tempRequestsList.get(i));
					if (tempHeadPos.intValue() == requestToSatisfy)
						return;
				}

				CSCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);
				tempHeadPos = new Integer(upperBound);
				upperBound = new Integer(tempRequestsList.get(indexOfUpperBound));
				if (tempHeadPos.intValue() == requestToSatisfy)
					return;

				CSCANtotalHeadMovement += getDistance(tempHeadPos, upperBound);	
			}
			return;
		}	
	}
	
	private void CLOOK() {
		// temporary request list to mutate
		List<Integer> tempRequestsList = new ArrayList<>(diskRequestsList);
		boolean satisfied = false;
		Integer tempReq = null;
		Integer nearestReq = null;
		Integer tempHeadPos = headPosition;
		
		while (!satisfied) {
			for (Integer i : tempRequestsList) {
				tempReq = i;
				
				if (nearestReq == null)
					nearestReq = tempReq;
				else if (getDistance(tempHeadPos, tempReq) < getDistance(tempHeadPos, nearestReq))
					nearestReq = tempReq;
			}
			CLOOKtotalHeadMovement += getDistance(tempHeadPos, nearestReq);
			tempHeadPos = nearestReq;
			tempRequestsList.remove(nearestReq);
			nearestReq = null;
			
			if (tempHeadPos.intValue() == requestToSatisfy)
				satisfied = true;
		}
	}
	
	private int getDistance(Integer a, Integer b) {
		if (a == null || b == null) {
			JOptionPane.showMessageDialog(null,  "Null passed to getDistance");
		} else {
			return Math.abs(a - b);
		}
		return 0;
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
			str.append("  Request to Satisfy: " + requestToSatisfy);
			str.append("\tTotal Head Movement: " + headMovements[i] + "\n");
		}

		return str.toString();
	}
}

