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
		List<Station> stationList = new ArrayList<>();
		QueueInterface<Train> trainQueue = new LinkedQueue<>();
		QueueInterface<Passenger> passengerQueue = new LinkedQueue<>();
		int numberOfPassengersCreated = 0;
		int numberOfPassengersWaiting = 0;

		createStations(stationList);
		
		for (int time = 0; time < TIME_INTERVAL; time++) {
			// calculate total passengers waiting
			// output time #waiting # on Trains

			// start a new train
			startNewTrain(time, trainQueue);
			
			// create passengers at this interval, and update number of passengers created
			numberOfPassengersCreated = generatePassengers(stationList, passengerQueue);
			
			// move the trains in the queue
			moveTrains(trainQueue, stationList, time);
		}
	}
	
	/**
	 * Purpose: private helper method createStations creates new Station objects each with a random timeToNextStation
	 * 			and adds them to stationList.
	 * @param stationList List<Station> that will contain the newly created Station objects.
	 */
	private static void createStations(List<Station> stationList) {
		int timeToNextStation = 0;
		Station tempStation = null;

		for (int i = 0; i < NUM_STATIONS; i++) {
			timeToNextStation = generator.nextInt(10) + 5; // a random time between 5-14 inclusive
			tempStation = new Station(timeToNextStation);
			stationList.add(tempStation);
			
			System.out.println("Station has been created with time to next station: " + timeToNextStation + " minutes.");
		}
	}
	
	private static void startNewTrain(int time, QueueInterface<Train> trainQueue) {
		if (time % TRAIN_INTERVAL == 0) {
			trainQueue.enqueue(new Train(TRAIN_CAPACITY));
			trainCount++;
		}
	}
	
	private static int generatePassengers(List<Station> stationList, QueueInterface<Passenger> passengerQueue) {
		Passenger tempPassenger = null;
		int numberOfPassengers = generator.nextInt(6);
		int startStation = 0;
		int stopStation = 0;
		
		for (int i = 0; i < numberOfPassengers; i++) {
			while (startStation >= stopStation) {
				startStation = generator.nextInt(NUM_STATIONS);
				stopStation = generator.nextInt(NUM_STATIONS);
			}
			
			// create new passenger
			tempPassenger = new Passenger(startStation, stopStation);
			
			// add passenger to startStation's queue
			stationList.get(startStation).addPassenger(tempPassenger);
			
			// add passenger to passengerQueue
			passengerQueue.enqueue(tempPassenger);
		}
		return numberOfPassengers;
	}
	
	private static void moveTrains(QueueInterface<Train> trainQueue, List<Station> stationList, int time) {
		int currentTime = 0;
		int stationNumber = 0;
		// create temporary variable of the train count during this time interval to loop through each train
		int tempTrainCount = trainCount;
		Train tempTrain = null;

		for (int i = 0; i < tempTrainCount; i++) {
			tempTrain = trainQueue.dequeue();
			tempTrain.move();
			currentTime = tempTrain.timeToNext();
			
			if (currentTime == 0) {
				stationNumber = tempTrain.nextStation();

				// unload all passengers getting off at current station 
				passengersDelivered = tempTrain.unloadPassengers(stationNumber);
				
				// load all passengers getting on to train from current station
				passengersOnTrains = tempTrain.loadPassengers(stationList.get(stationNumber), time);
				
				// update train to next station to move to
				tempTrain.updateStation(stationList.get(stationNumber + 1).getTimeToNextStation());
			}
			
			if (tempTrain.nextStation() == 4) {
				trainCount--;
			} else {
				trainQueue.enqueue(tempTrain);
			}
		}
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
