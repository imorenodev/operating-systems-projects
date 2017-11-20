import java.util.*;

/**
 * @author 	Ian Moreno
 * @date 	10/06/2017
 * @purpose 	SortingProjectLinkedNodesClient class demonstrates Insertion Sort on a LinkedList
 * 			of 10 nodes each with data of a random integer between 0...9
 */

public class SortingProjectLinkedNodesClient {
	public static void main(String[] args) {
		final int LIST_SIZE = 10;
		
		List<Node> integerList = buildRandomIntegerList(LIST_SIZE);

		printList("Before", integerList);
		insertionSort(integerList, LIST_SIZE);
		printList("After", integerList);

	}

	private static List<Node> buildRandomIntegerList(int size) {
		Random rand = new Random();
		List<Node> newList = new LinkedList<>(); // create empty linked list
		
		Node head = new Node(rand.nextInt(size)); // create head node
		newList.add(head); // add head node to list
		Node tempNode = head; // create and set temp pointer to head node
		Node newNode = null;
		
		for (int i = 0; i < size-1; i ++) { // create 9 new nodes with random numbers
			newNode = new Node(rand.nextInt(size));
			tempNode.setNext(newNode);
			tempNode = newNode;
			newList.add(newNode);
		}

		return newList;
	}
	
	private static void printList(String msg, List<Node> list) {
		System.out.print(msg + "\t ");
		for (Node n : list) {
			System.out.print(" " + n);
		}
		System.out.println("\n");
	}
	
	private static void insertionSort(List<Node> list, int size) {
		Node swapNode = new Node(0);
		
		for (int i = 0; i < size; i++) {
			for (int j = i+1; j < size; j++) {
				if (list.get(i).getData() > list.get(j).getData()) {
					swapNode.setData(list.get(j).getData());
					list.get(j).setData(list.get(i).getData());
					list.get(i).setData(swapNode.getData());
				}
			}
			printList(("Pass #" + (i+1)), list);
		}
	}
	
}

class Node {
	private int data;
	private Node next;
	
	public Node(int aData) {
		data = aData;
		next = null;
	}
	
	public int getData() {
		return data;
	}
	
	public void setData(int aData) {
		data = aData;
	}

	public Node getNext() {
		return next;
	}
	
	public void setNext(Node aNode) {
		next = aNode;
	}
	
	public String toString() {
		return Integer.toString(data);
	}
}
	
