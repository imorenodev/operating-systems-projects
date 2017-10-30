import java.util.*;

/**
 * @author 	Ian Moreno
 * @date 	10/30/2017
 * @purpose 	TrainStationProjectClient class demonstrates the Stack & Queue Train Station Project
 * 			Program simulates a train route.
 */

public class TrainStationProjectClient {
	public static Random generator = new Random();
	public static final int NUM_STATIONS = 5;
	public static final int TRAIN_CAPACITY = 50;
	public static final int TRAIN_INTERVAL = 10;
	public static final int TIME_INTERVAL = 200;
	public static int trainCount = 0;
	public static int passengersOnTrains = 0;
	public static int passengersDelivered = 0;
	
	public static void main(String[] args) {
		
	}
	
	private static void createStations() {
		
	}
	
	private static void startNewTrain() {
		
	}
	
	private static void generatePassengers() {
		
	}
	
	private static void moveTrains() {
		
	}

	/** Reports the final situations of the trains and passengers waiting
	 *  and some statistics for passengers' wait times.
	 *  @param clock The time that train operations have ceased. 
	 */
	public static void finalReport(int clock, int passengersCreated, QueueInterface<Passenger> passengers) {
		System.out.println("Final Report");
		System.out.println("The total number of passengers is " + passengersCreated);
		System.out.println("The number of passengers currently on a train " + passengersOnTrains );
		System.out.println("The number of passengers delivered is " + passengersDelivered);

		int waitBoardedSum = 0;
		int waitNotBoardedSum = 0;

		for (int i=0; i < passengersCreated; i++) {
			Passenger p = passengers.dequeue();

			if(p.boarded())
				waitBoardedSum += p.waitTime(clock);
			else
				waitNotBoardedSum += p.waitTime(clock);
		} // end for

		System.out.println("The average wait time for passengers " + 	"that have boarded is");
		System.out.println((double)waitBoardedSum/(passengersOnTrains + passengersDelivered));
		System.out.println("The average wait time for passengers " + "that have not yet boarded is");
		System.out.println((double)waitNotBoardedSum / (passengersCreated - passengersOnTrains -passengersDelivered));
	} // end finalReport
}
