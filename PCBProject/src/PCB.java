import java.util.Random;

/**
 * 
 * @author Ian Moreno
 * date: 09/12/2017
 * purpose: This class defines the PCB object
 */

public class PCB {
	private static int idCount = 0; // static class variable keeps track of the number of unique processes globally
	private Random rand = new Random(); // random number generator
	private String processState = ""; // instance variable representing the process state
	private int processID = 0; // instance variable representing the unique processID
	private int processPriority = 0; // instance variable representing the priority level of the process
	private int cpuUsed = 0; // instance variable representing how many CPU cycles given to the process during activity and before completion
	private int memoryNeeded = 0; // instance variable representing how much memory is needed 

	/**
	 * purpose: no-argument constructor sets default values for new PCB objects
	 */
	public PCB() {
		this.processState = "New";
		this.processID = ++idCount;
		this.processPriority = 0;
		this.cpuUsed = 0;
		this.memoryNeeded = getRandomMemory();
		
	}
		
	/**
	 * purpose: helper method returns a random number between 25 and 50 inclusive 
	 * that represents the memory needed
	 * @return int value (randomly chosen) between 25 and 50 inclusive
	 */
	private int getRandomMemory() {
		return (25 + rand.nextInt(50 - 25 + 1));
	}
	
	/**
	 * purpose: getter method returns the current Process State
	 * @return String value representing the current Process State
	 */
	public String getProcessState() {
		return processState;
	}
	
	/**
	 * purpose: getter method returns the unique Process ID
	 * @return int value representing the unique Process ID
	 */
	public int getProcessId() {
		return processID;
	}
	
	/**
	 * purpose: getter method returns the Process Priority level
	 * @return int value representing the Process Priority level
	 */
	public int getProcessPriority() {
		return processPriority;
	}
	
	/**
	 * purpose: getter method returns the amount of CPU used by the process during execution
	 * @return int value representing the amount of CPU used by the process
	 */
	public int getCpuUsed() {
		return cpuUsed;
	}
	
	/**
	 * purpose: getter method that returns how much memory is needed by the process
	 * @return int value representing how much memory is needed by the process
	 */
	public int getMemoryNeeded() {
		return memoryNeeded;
	}
	

	/**
	 * purpose: setter method sets the current Process State
	 * @param aState String value representing the current Process State
	 */
	public void setProcessState(String aState) {
		if (aState.equalsIgnoreCase("running") || 
			aState.equalsIgnoreCase("waiting") || 
			aState.equalsIgnoreCase("ready")) {
			processState = aState;
		} else {
			processState = "New";
		}
	}
	
	/**
	 * purpose: setter method sets the Process Priority level
	 * @param aPriorityLevel int value representing the Process Priority level
	 */
	public void setProcessPriority(int aPriorityLevel) {
		processPriority = aPriorityLevel;
	}
	
	/**
	 * purpose: setter method sets the amount of CPU used by the process during execution
	 * @param aCpuUsed int value representing the amount of CPU used by the process
	 */
	public void setCpuUsed(int aCpuUsed) {
		cpuUsed  = (aCpuUsed >= 0) ? aCpuUsed : 0;
	}
	
	/**
	 * purpose: setter method that sets how much memory is needed by the process
	 * @param aMemoryNeeded int value representing how much memory is needed by the process
	 */
	public void setMemoryNeeded(int aMemoryNeeded) {
		memoryNeeded = (aMemoryNeeded >= 0) ? aMemoryNeeded : 0;
	}

	/**
	 * purpose: returns a String value representation of the field names and values
	 * @return String value representation of the field names and values
	 */
	public String showPCB() {
		return toString();
	}

	/**
	 * purpose: returns a String value representation of the field names and values
	 * @return String value representation of the field names and values
	 */
	public String toString() {
		return "Process ID: " + getProcessId() + 
			   "\tProcess State: " + getProcessState() + 
			   "\tProcess Priority: " + getProcessPriority() + 
			   "\tCPU Used: " + getCpuUsed() + 
			   "\tMemory Needed: " + getMemoryNeeded() + "\n";
	}
}
