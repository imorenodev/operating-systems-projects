import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class DefragmentMem {
	public static boolean defragment(LinkedList<PCB> QMemOpen) {
		if (QMemOpen.isEmpty()) return false;

		boolean defragged = false;
		Iterator<PCB> QMemOpenIterator = QMemOpen.iterator();
	
		PCB head = QMemOpen.getFirst();
		PCB next = null;
		List<PCB> removeList = new LinkedList<>();
		LinkedList<PCB> newOpenList = new LinkedList<>();

		while (QMemOpenIterator.hasNext()) {
			next = QMemOpenIterator.next();
			if (head.get_memLimit() == next.get_memBase()) {
				head.set_memLimit(next.get_memLimit());
				removeList.add(next);
			} else {
				newOpenList.add(head);
				head = next;
			}
		}
		
		if (!removeList.isEmpty()) {
			QMemOpen.removeAll(removeList);
			defragged = true;
		}

		return defragged;
	}


}
