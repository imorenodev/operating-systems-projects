import java.util.LinkedList ;
import java.util.Random ; 
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @purpose	ProcessMgmt program demonstrates a Process Management program to manage the process Ready, Waiting, 
 * 			and Running queues and prioritize the processes based on CPU and I/O events.
 * @author 	Ian Moreno
 * @date		10/06/2017
 *
 */

public class ProcessMgmt

{
	public static void main(String args[]) throws IOException	
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		//#010	Initialization of fields and data structures	///////////////
		int QREADY__T	= 25 ;		
		final int BLOCKIO	= 3 ;		final int BLOCKPAGE	= 4 ;		
		final int INTERRUPT	= 2 ;		final int COMPLETED	= 1 ; 
		int cycles = 1;
				
		Random random__X	= new Random();
		CPU_event event		= new CPU_event();
		
		int CPU_runtime ;		int event__X = 0;
  
		//#005 Create the List for QReady
		LinkedList<PCB> QReadyList = new LinkedList<>();
		//#006 Create the List for QWaiting
		LinkedList<PCB> QWaitingList = new LinkedList<>();
		
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

					continue;	// iterate to the next in the WHILE loop
				}
							
				//#0200 Simulate the type of Block on the Process (I/O Block, Memory Paging Block, Interrupt)
				
				event__X	= event.get_CPU_event() ;
				
				//#230 Set the state to "Ready" from "Running"
			
				switch (event__X)
				{
					case 1 :
						System.out.println("\n*****\t\t\tProcess Completed\t\t\t*****CPU event*****");   	  
						System.out.println(PCB_Running.showPCB());		

						writer.write("\n*****\t\t\tProcess Completed\t\t\t*****CPU event*****");   	  
						writer.newLine();
						writer.write(PCB_Running.showPCB());	
						writer.newLine();
						break;
					case 2 :
					{
						//#240 Add to QReady	
						PCB_Running.set_state("Ready");
						QReadyList.addFirst(PCB_Running); // High Priority
						break;
					}				
					case 3 :
					{	
						//#242 Add to QReady
						PCB_Running.set_state("Ready");
						QReadyList.add(QReadyList.size()/2, PCB_Running); // Medium Priority
						break;
					}
					case 4 :
					{
						//#244 Add to QWait	
						PCB_Running.set_state("Waiting");
						QWaitingList.add(PCB_Running);
						break;
					}
					default :
					{
						//#246 Add to QReady
						PCB_Running.set_state("Ready");
						QReadyList.addLast(PCB_Running); // Low Priority
						break;
					}
				}

				if ((cycles % 4 == 0) && !QWaitingList.isEmpty()) {// every 1/4 cycles, removeFirst from QWaitingList and addFirst to QReadyList
					PCB_Temp = QWaitingList.removeFirst();
					PCB_Temp.set_state("Ready");
					QReadyList.addFirst(PCB_Temp);	
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

				System.out.println("\n*****\t\t\tWaiting Queue\t\t\t*****");   	  
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
}