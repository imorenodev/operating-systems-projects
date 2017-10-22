import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 
 * @author Ian Moreno
 * date: 09/12/2017
 * purpose: This class demonstrates the PCB class
 */

public class ClientDriver {
	public static void main(String[] args) throws IOException {
		List<PCB> pcbList = new ArrayList<>(); // instantiate ArrayList consisting of PCB objects
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		
		// create 25 new PCB objects and add each to the pcbList
		for (int i = 0; i < 25; i++) {
			pcbList.add(new PCB());
		}
		
		try {
			// print the contents of each PCB object in the pcbList
			for (PCB pcb : pcbList) {
				System.out.println(pcb.showPCB()); // to console
				writer.write(pcb.showPCB()); // to file output.txt
			}
		} catch (IOException e) {
			System.exit(0);;
		}
		writer.close();
	}
}
