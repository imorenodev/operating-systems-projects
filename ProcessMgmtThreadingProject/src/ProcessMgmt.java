import java.util.Collections;
import java.util.LinkedList ;
import java.util.List;
import java.util.Random ;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @purpose	ProcessMgmt program demonstrates a Process Management program to manage the process Ready, Waiting, 
 * 			and Running queues and prioritize the processes based on CPU and I/O events, also incorporates 
 * 			memory allocation management and threading.
 * @author 	Ian Moreno
 * @date		12/11/2017
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
		LinkedBlockingQueue<PCB> QReadyList = new LinkedBlockingQueue<>();
		//#006 Create the List for QWaiting
		LinkedBlockingQueue<PCB> QWaitingList = new LinkedBlockingQueue<>();

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
			QReadyList.add(new PCB());
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
				try {
					PCB_Running = QReadyList.take();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

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
					{
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
						{
							//#240 Add to QReady	
							PCB_Running.set_state("Ready");
							QReadyList.add(PCB_Running); // High Priority
							break;
						}				
						case 3 :
						{	
							//#242 Add to QReady
							PCB_Running.set_state("Ready");
							QReadyList.add(PCB_Running); // Medium Priority
							break;
						}
						case 4 :
						{
							//#244 Add to QWait	
							PCB_Running.set_state("Waiting");
							QWaitingList.add(PCB_Running);
							new Thread(){
								@Override
								public void run() {
									System.out.println("***I/O EXCEPTION - START NEW THREAD***");
									try {
										writer.write("\n***I/O EXCEPTION - START NEW THREAD***");
										writer.newLine();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}   	  
									try {
										Thread.sleep(random__X.nextInt(2500)+500);
										QReadyList.add(QWaitingList.take());
										System.out.println("***KILL THREAD***");
										try {
											writer.write("\n****KILL THREAD***");
											writer.newLine();
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}.start();
							break;
						}
						default :
						{
							//#246 Add to QReady
							PCB_Running.set_state("Ready");
							QReadyList.add(PCB_Running); // Low Priority
							break;
						}
					}
				} else {
					System.out.printf("##### No Memory Available for Process:%d\tneeding:%d\n"
							,PCB_Running.get_ID() 
							,PCB_Running.get_memLimit());
					writer.write(String.format("##### No Memory Available for Process:%d\tneeding:%d\n", PCB_Running.get_ID(), PCB_Running.get_memLimit()));
					writer.newLine();
					QReadyList.add(PCB_Running);
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