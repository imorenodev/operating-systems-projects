import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author  Ian Moreno
 * 			09/20/2017
 * purpose: PCB_Driver class demonstrates iterating through a LinkedList of PCB objects
 * 			simulating the process states and cycle of CPU resource delegation
 */

public class PCB_Driver {
	private static BufferedWriter writer;

	public static void main(String[] args) throws IOException {
		writer = new BufferedWriter(new FileWriter("output.txt")); 
		Random rand = new Random(); 
		LinkedList<PCB> PCBList = new LinkedList<>(); // LinkedList to hold PCB objects
		PCB runningPCB = null; // variable to hold PCB object currently running
		int cpuUsed = 0; // variable to hold the cumulative sum of cpuUsed for PCB object

		for (int i = 0; i < 10; i++) { // create 10 random PCB objects and add to PCBList 
			PCBList.add(new PCB());
		}

		printList(PCBList); 
		System.out.println("\nIterate through LinkedList:");
		writer.write("\nIterate through LinkedList:");
		writer.newLine();
		
		/** Iterate through each PCB object in sequence starting at the first node
		 *  calculate the cumulative amount of CPU_used and test if PCB has reached or exceeded
		 *  its CPU_max. If so, remove the PCB from the list, otherwise add to end of list
		 */
		while (PCBList.size() > 0) {
			runningPCB = PCBList.removeFirst(); // remove first node
			runningPCB.set_state("Running"); // set node's state to Running
			cpuUsed = runningPCB.get_CPU_used() + (rand.nextInt(10) + 10); // calculate cumulative CPU_used
			runningPCB.set_CPU_used(cpuUsed); // set new amount of CPU_used
			System.out.println(runningPCB.showPCB()); 
			writer.write(runningPCB.showPCB()); 
			writer.newLine();
			
			if (cpuUsed >= runningPCB.get_CPU_max()) { // CPU_used meets or exceeds CPU_max
				runningPCB.set_state("Terminated"); // remove from PCBList
			} else { // CPU_used does not meet or exceed CPU_max
				PCBList.addLast(runningPCB); // add to end of PCBList
			}
		}
		writer.close();
	}
	
	/**
	 * purpose: private static helper method prints the list of PCB objects
	 * @param aList a LinkedList of PCB objects
	 */
	private static void printList(LinkedList<PCB> aList) throws IOException {
		System.out.println("Contents of LinkedList:");
		writer.write("Contents of LinkedList:");
		writer.newLine();

		Object[] arrPCB = aList.toArray();
			for (Object pcb : arrPCB) {
				System.out.println(((PCB)pcb).showPCB());
				writer.write(((PCB)pcb).showPCB());
				writer.newLine();
		}
	}

}
