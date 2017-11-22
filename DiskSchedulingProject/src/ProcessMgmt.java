import java.util.Collections;
import java.util.LinkedList ;
import java.util.List;
import java.util.Random ; 
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @purpose	ProcessMgmt program demonstrates a Process Management program to manage the process Ready, Waiting, 
 * 			and Running queues and prioritize the processes based on CPU and I/O events, also incorporates 
 * 			memory allocation management.
 * @author 	Ian Moreno
 * @date		10/21/2017
 *
 */

public class ProcessMgmt

{
	public static BufferedWriter writer;

	public static void main(String args[]) throws IOException	
	{
		writer = new BufferedWriter(new FileWriter("output.txt"));
		//#010	Initialization of fields and data structures	///////////////
		int QREADY__T	= 5 ;		
		final int BLOCKIO	= 3 ;		final int BLOCKPAGE	= 4 ;		
		final int INTERRUPT	= 2 ;		final int COMPLETED	= 1 ; 
		final int mem__T = 256;
		int cycles = 1;
				
		Random random__X	= new Random();
		CPU_event event		= new CPU_event();
		
		int CPU_runtime ;		int event__X = 0;
  
		//#005 Create the List for QReady
		LinkedList<PCB> QReadyList = new LinkedList<>();
		//#006 Create the List for QWaiting
		LinkedList<PCB> QWaitingList = new LinkedList<>();

		LinkedList<PCB> QMemOpen	= new LinkedList<PCB>();	//#0020 Create List QMemOpen
		LinkedList<PCB> QMemUsed	= new LinkedList<PCB>();	//#0030 Create List QMemUsed
		
		QMemOpen.add(new PCB(mem__T)) ;	//#0032 Set the Open Memory
		OpenMem om = new OpenMem(); // OpenMem object
		
		PCB PCB_Running	= null ; 		//#020 Create the field for: PCB_Running 
		PCB PCB_Temp = null;
	
		//#030	
		
		for (int ii = 0; ii < QREADY__T; ii++)
		{
			//#040 Add a new PCB object onto the LL 
			QReadyList.addFirst(new PCB());
		}

		///////////////////////////////////////////////////////////////////////
		//#080	===> end of Initialization 
		
		System.out.println("\n*****\t\t\tReady Queue\t\t\t*****");  
		writer.write("\n*****\t\t\tReady Queue\t\t\t*****");  
		writer.newLine();
		//for (PCB pcbLoop : QReady)		//#090 Loop that executes based on the no. of nodes in the LL
			//#095 Print the PCB for an LL node
		for (PCB pcb : QReadyList) {
			System.out.println(pcb.showPCB());
			writer.write(pcb.showPCB());  
			writer.newLine();
		}
  
		//#0100	Process until the active processes are all completed	///////
		
		while (!QReadyList.isEmpty() || !QWaitingList.isEmpty())	//#120	change to iterate until both QReady and QWait are empty
		{
			//#0105	Next process to Run
			if (!QReadyList.isEmpty()) { // remove a PCB from QReadyList if list is not empty
				PCB_Running = QReadyList.removeFirst();

				if (om.findOpenMem(PCB_Running, QMemOpen, QMemUsed)) {
					//#0145 Set the state value to "Running"
					PCB_Running.set_state("Running");
					
					CPU_runtime	= random__X.nextInt(20) + 1 ;	//#0150 Get a random no. between 0 and 20
					
					//#0160 Tally and set the CPU used for the process
					PCB_Running.set_CPU_used(PCB_Running.get_CPU_used() + CPU_runtime);

					System.out.println("\n*****\t\t\tRunning Queue\t\t\t*****");   	  
					System.out.println(PCB_Running.showPCB());
					
					writer.write("\n*****\t\t\tRunning Queue\t\t\t*****");   	  
					writer.newLine();
					writer.write(PCB_Running.showPCB());
					writer.newLine();
			
					//#0180 Increment the wait times for all other processes
					for (PCB pcb : QReadyList) {
						pcb.set_timeWaiting(pcb.get_timeWaiting() + CPU_runtime);
					}

					for (PCB pcb : QWaitingList) {
						pcb.set_timeWaiting(pcb.get_timeWaiting() + CPU_runtime);
					}				

					//#0190 IF statement for termination based on CPU Max
					if (PCB_Running.get_CPU_used() >= PCB_Running.get_CPU_max()){	
						System.out.println("\n*****\t\t\tProcess Completed\t\t\t*****");   	  
						System.out.println(PCB_Running.showPCB());

						writer.write("\n*****\t\t\tProcess Completed\t\t\t*****");   	  
						writer.newLine();
						writer.write(PCB_Running.showPCB());
						writer.newLine();

						// Remove from QMemUsed and add back to QMemOpen
						memAllocate(PCB_Running, QMemOpen, QMemUsed);

						continue;	// iterate to the next in the WHILE loop
					}
								
					//#0200 Simulate the type of Block on the Process (I/O Block, Memory Paging Block, Interrupt)
					
					event__X	= event.get_CPU_event() ;
					
					//#230 Set the state to "Ready" from "Running"
				
					switch (event__X)
					{ // TERMINATION
						case 1 :
							System.out.println("\n*****\t\t\tProcess Completed\t\t\t*****CPU event*****");   	  
							System.out.println(PCB_Running.showPCB());		

							writer.write("\n*****\t\t\tProcess Completed\t\t\t*****CPU event*****");   	  
							writer.newLine();
							writer.write(PCB_Running.showPCB());	
							writer.newLine();

							// Remove from QMemUsed and add back to QMemOpen
							memAllocate(PCB_Running, QMemOpen, QMemUsed);


							break;
						case 2 :
						{ // INTERRUPT
							//#240 Add to QReady	
							PCB_Running.set_state("Ready");
							QReadyList.addFirst(PCB_Running); // High Priority
							break;
						}				
						case 3 :
						{ // PAGE FAULT
							//#242 Add to QReady
							PCB_Running.set_state("Ready");
							QReadyList.add(QReadyList.size()/2, PCB_Running); // Medium Priority
							break;
						}
						case 4 :
						{ // BLOCK I/O
							//#244 Add to QWait	
							PCB_Running.set_state("Waiting");
							PCB_Running.setDiskRequest(new DiskScheduler());
							QWaitingList.add(PCB_Running);
							break;
						}
						default :
						{ // OTHER
							//#246 Add to QReady
							PCB_Running.set_state("Ready");
							QReadyList.addLast(PCB_Running); // Low Priority
							break;
						}
					}
				} else {
					System.out.printf("##### No Memory Available for Process:%d\tneeding:%d\n"
							,PCB_Running.get_ID() 
							,PCB_Running.get_memLimit());
					writer.write(String.format("##### No Memory Available for Process:%d\tneeding:%d\n", PCB_Running.get_ID(), PCB_Running.get_memLimit()));
					writer.newLine();
					QReadyList.addLast(PCB_Running);
				}

				if ((cycles % 4 == 0) && !QWaitingList.isEmpty()) {// every 1/4 cycles, removeFirst from QWaitingList and addFirst to QReadyList
					PCB_Temp = QWaitingList.removeFirst();
					PCB_Temp.set_state("Ready");
					QReadyList.addFirst(PCB_Temp);	
					
					System.out.println("\n\n*****\t\t\tPCB Disk Request Finished\tRemove From QWait and Add to Ready Queue\t\t\t*****");   
					System.out.println(PCB_Temp.showPCB());
					System.out.println(PCB_Temp.getDiskRequest().toString());
					writer.write("\n\n*****\t\t\tPCB Disk Request Finished\tRemove From QWait and Add to Ready Queue\t\t\t*****");   
					writer.newLine();
					writer.write(PCB_Temp.showPCB());
					writer.newLine();
					writer.write(PCB_Temp.getDiskRequest().toString());
					writer.newLine();
				}
				
				System.out.println("\n*****\t\t\tContext Switch\tReady Queue\t\t\t*****");   
				writer.write("\n*****\t\t\tContext Switch\tReady Queue\t\t\t*****");   
				writer.newLine();

				//#300 print out PCB's in both QReady and QWait
				System.out.println("\n*****\t\t\tReady Queue\t\t\t*****");   	  
				writer.write("\n*****\t\t\tReady Queue\t\t\t*****");   	  
				writer.newLine();
				for (PCB pcb : QReadyList) {
					System.out.println(pcb.showPCB());
					writer.write(pcb.showPCB());
					writer.newLine();
				};

				System.out.println("\n*****\t\t\tWaiting Queue\t\t\t*****\n");   	  
				writer.write("\n*****\t\t\tWaiting Queue\t\t\t*****");   	  
				writer.newLine();
				for (PCB pcb : QWaitingList) {
					System.out.println(pcb.showPCB());
					writer.write(pcb.showPCB());
					writer.newLine();
				}
				cycles++;
			}
			else if (!QWaitingList.isEmpty()) {// QReadyList must be empty, therefore removeFirst PCB from QWaitingList if not empty and put onto QReadyList
				PCB_Temp = QWaitingList.removeFirst();
				PCB_Temp.set_state("Ready");
				QReadyList.addFirst(PCB_Temp);
			}
		}	
		writer.close();
	}
	
	private static void memAllocate(PCB PCB_Running, LinkedList<PCB> QMemOpen, LinkedList<PCB> QMemUsed) throws IOException {
		QMemUsed.remove(PCB_Running); // remove from QMemUsed
		QMemOpen.add(PCB_Running); // add the memory back to QMemOpen
		System.out.println("\n-----\t\t\tAdded base:" + PCB_Running.get_memBase() + " limit: " + PCB_Running.get_memLimit() + " to Open Memory"); 
		writer.write("\n-----\t\t\tAdded base:" + PCB_Running.get_memBase() + " limit: " + PCB_Running.get_memLimit() + " to Open Memory"); 
		writer.newLine();

		Collections.sort((List)QMemOpen);
		for (PCB loopI : QMemOpen)
		{
			System.out.printf("@0500 QMemOpen sorted\t%s\n", loopI.showPCB()) ;
			writer.write(String.format("@0500 QMemOpen sorted\t%s\n", loopI.showPCB()));
			writer.newLine();
		}
		
		if (DefragmentMem.defragment(QMemOpen)) {
			System.out.println("\n-----\t\t\tDefragmented QMemOpen");
			writer.write("\n-----\t\t\tDefragmented QMemOpen");
			writer.newLine();
			
			for (PCB pcb : QMemOpen)
			{
				System.out.printf("@0500 QMemOpen sorted\t%s\n", pcb.showPCB()) ;
				writer.write(String.format("@0500 QMemOpen sorted\t%s\n", pcb.showPCB()));
				writer.newLine();
			}
		}

		System.out.println("\n*****\t\t\tMemory Used\t\t\t*****");   	  
		for (PCB loopI : QMemUsed) {
			System.out.printf("@0200Used\t%s\n"	,loopI.showPCB());
			writer.write(String.format("@0200Used\t%s\n"	,loopI.showPCB()));
			writer.newLine();
		}
	}
}