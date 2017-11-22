import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class OpenMem 
{

	public OpenMem ()
	{}
	
	public boolean findOpenMem	(
			PCB PCB_Ready
			,LinkedList<PCB> QMemOpen
			,LinkedList<PCB> QMemUsed	) throws IOException
	{
		// first check if PCB is already in QMemUsed
		for (PCB pcb : QMemUsed) {
			if (PCB_Ready.get_ID() == pcb.get_ID()) {
				return true;
			}
		}

		boolean memFound__B = false ;
			
		int memNeed = PCB_Ready.get_memLimit() ;	//@0100
		
		for (int ii = 0; ii < QMemOpen.size(); ii ++)
		{
			PCB memOpen = QMemOpen.get(ii) ;	//@0120
			int base0 = memOpen.get_memBase() ;
			int limit0 = memOpen.get_memLimit();

			if ((limit0 - base0) > memNeed )	//@0200
			{
				memFound__B = true;
								
				//@0220	split the open memory	@@ QMemOpen @@
				memOpen.set_memBase(base0 + memNeed);
				
				//@0240	replace the open memory
				PCB_Ready.set_memBase(base0);
				PCB_Ready.set_memLimit(base0 + memNeed);
					
				//@0260	allocate the used memory	@@ QMemUsed @@
				QMemUsed.add(PCB_Ready);
				//@0280	set the base for the process
					
				//@0300	push the used memory
				System.out.println("\n-----\t\t\tAdded base:" + PCB_Ready.get_memBase() + " limit: " + PCB_Ready.get_memLimit() + " to Used Memory"); 
				ProcessMgmt.writer.write("\n-----\t\t\tAdded base:" + PCB_Ready.get_memBase() + " limit: " + PCB_Ready.get_memLimit() + " to Used Memory"); 
				ProcessMgmt.writer.newLine();
					
				break ;	// exit out of the FOR loop for memory
			}
		}
		return memFound__B ;
	}
}