//	http://www.java2s.com/Tutorial/Java/0140__Collections/LinkedListClass.htm

import java.util.LinkedList	;
import java.util.Random ;

public class PCB implements Comparable<PCB>
{
	private String 	PCB_state;
	private int		PCB_ID ;
	private int		PCB_priority;
	private String	pgmCounter;
	private int		CPU_used;
	private int		CPU_max;
	private int		timeWaiting;
	private int		memBase;
	private int		memLimit;
	private DiskScheduler diskRequest;
	
	private static int	PCB__K;
	private Random random__X ;
		
		
	// default constructor
	public PCB ()
	{
		Random random__X	= new Random();

		PCB__K+= 1 ;	//=====>	Increment the static variable for Process ID
		PCB_state	= "Ready" ;
		PCB_ID		= PCB__K ; 
		PCB_priority= 0 ;
		pgmCounter	= "" ;
		CPU_used	= 0 ;
		CPU_max		= random__X.nextInt(100) + 1 ;	// Assign max to be between 1 and 1000
		timeWaiting	= 0 ;
		memBase = 0;
		memLimit = random__X.nextInt(51) + 25; // Assign memory needed between 25-75
		diskRequest = null;
	}	

	// Memory tracking Constructor
	public PCB (int m0)
	{
		PCB__K 		= 0;
		PCB_state	= "@" ;
		PCB_ID		= 0;
		PCB_priority	= 0;
		pgmCounter	= "";
		CPU_used		= 0;
		CPU_max		= 0;
		timeWaiting	= 0;
		memBase 		= 0;
		memLimit 	= m0;
		diskRequest = null;
	}	

	public int compareTo (PCB pcb0) 
	{
		int mem0	= pcb0.get_memBase();
		return this.memBase - mem0 ;
	}	

	public String showPCB()
	{
		return "state: " + PCB_state
			+ "\tID: "	+ Integer.toString(PCB_ID)
			+ "\tK: "	+ pgmCounter
			+ "\tpriority: "	+ Integer.toString(PCB_priority)
			+ "\tCPU used: "	+ Integer.toString(CPU_used)	
			+ "\tCPU max: "	+ Integer.toString(CPU_max)					
			+ "\tWait: "	+ Integer.toString(timeWaiting)
			+ "\t\tmemBase: " + Integer.toString(memBase)
			+ "\tmemLimit: " + Integer.toString(memLimit);
	}
		
	//	set methods
	
	public String get_state()
	{
		return PCB_state;
	}
	
	public int get_ID()
	{
		return PCB_ID;
	}
	
	public int get_PCB_priority()
	{
		return PCB_priority ;
	}
	
	public int get_CPU_used()
	{
		return CPU_used;
	}
	
	public int get_CPU_max()
	{
		return CPU_max;
	}	
	
	public String get_pgmCounter()
	{
		return pgmCounter;
	}	
	
	public int get_timeWaiting()
	{
		return timeWaiting;
	}	
	
	public int get_memBase() 
	{
		return memBase;
	}
	
	public int get_memLimit()
	{
		return memLimit;
	}
	
	// Set methods , void returns no value

	public void set_state(String state0)
	{
		PCB_state	= state0 ;
	}
	
	public void set_PCB_priority(int priority0)
	{
		PCB_priority	= priority0 ;
	}	
	
	public void set_CPU_used(int CPU0)
	{
		CPU_used	= CPU0 ;
	}
	
	public void set_CPU_max(int CPU0)
	{
		CPU_max	= CPU0 ;
	}
	
	public void set_pgmCounter(String pgmCounter0)
	{
		pgmCounter	= pgmCounter0 ;
	}
	
	public void set_timeWaiting(int timeWaiting0)
	{
		timeWaiting	= timeWaiting0 ;
	}
	
	public void set_memBase(int m0)
	{
		memBase = m0;
	}
	
	public void set_memLimit(int m0)
	{
		memLimit = m0;
	}
	
	public void setDiskRequest(DiskScheduler req) {
		if (req != null) 
			diskRequest = req;
		else
			diskRequest = null;
	}
	
	public DiskScheduler getDiskRequest() {
		return diskRequest;
	}
}