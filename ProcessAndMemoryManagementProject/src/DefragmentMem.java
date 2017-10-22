import java.util.LinkedList;
import java.util.Iterator;

public class DefragmentMem {
	public static boolean defragment(LinkedList<PCB> QMemOpen) {
		if (QMemOpen.isEmpty()) return false;

		boolean defragged = false;
		Iterator<PCB> QMemOpenIterator1 = QMemOpen.iterator();
	
		for (int i = 0; QMemOpen.size() > 1;) {
			PCB tempPCB = QMemOpen.get(i);
			PCB nextPCB = QMemOpen.get(i+1);
			if (tempPCB.get_memLimit() == nextPCB.get_memBase()) {
				nextPCB.set_memBase(tempPCB.get_memBase());
				defragged = true;
			}
			QMemOpen.remove(tempPCB);
		}

		return defragged;
	}
}
