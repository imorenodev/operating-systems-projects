import java.util.*;

/**
 * @author 	Ian Moreno
 * @date 	10/30/2017
 * @purpose 	TrainStationProjectClient class demonstrates the Stack & Queue Train Station Project
 * 			Program simulates a train route using Stacks, Lists, and Queues.
 */

public class TrainStationProjectClient {
	// initialize global variables
	public static Random generator = new Random();
	public static final int NUM_STATIONS = 5;
	public static final int TRAIN_CAPACITY = 50;
	public static final int TRAIN_INTERVAL = 10;
	public static final int TIME_INTERVAL = 200;
	public static int trainCount = 0;
	public static int passengersOnTrains = 0;
	public static int passengersDelivered = 0;
	
	public static void main(String[] args) {
		// initialize variables
		List<Station> stationList = new ArrayList<>();
		QueueInterface<Train> trainQueue = new LinkedQueue<>();
		QueueInterface<Passenger> passengerQueue = new LinkedQueue<>();
		int numberOfPassengersCreated = 0;
		int numberOfPassengersWaiting = 0;
		int time = 0;

		// create new Stations
		createStations(stationList);
		
		// enter loop for time interval
		while (time <= TIME_INTERVAL) {
			// start a new train
			startNewTrain(time, trainQueue);
			
			// create passengers at this interval, and update number of passengers created
			numberOfPassengersCreated += generatePassengers(stationList, passengerQueue);
			
			// move the trains in the queue
			moveTrains(trainQueue, stationList, time);

			// update numberOfPassengersWaiting
			numberOfPassengersWaiting = (numberOfPassengersCreated - passengersOnTrains - passengersDelivered);
			// output time #waiting # on Trains
			System.out.println("-----\nCurrent Time: " + time + ".\nNumber of passengers waiting: " 
												+ numberOfPassengersWaiting + ".\nNumber of passengers on trains: " 
												+ passengersOnTrains + ".\n-----");

			time++; 
		}
		
		finalReport(time, numberOfPassengersCreated, passengerQueue);
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
			tempStation = new Station(timeToNextStation); // create new Station object passing in timeToNextStation to constructor
			stationList.add(tempStation); // add new Station object to the stationList
			
			System.out.println("Station has been created with time to next station: " + timeToNextStation + " minutes.");
		}
	}
	
	/**
	 * Purpose: private helper method startNewTrain creates a new Train object and enqueues it into the trainQueue.
	 * Pre-Condition: current time must be a factor of the 10 minute time interval to create and enqueue new Trains
	 * @param time int variable representing the current time
	 * @param trainQueue QueueInterface<Train> variable representing the LinkedQueue of Train objects.
	 */
	private static void startNewTrain(int time, QueueInterface<Train> trainQueue) {
		if (time % TRAIN_INTERVAL == 0) { // then it's a 10-minute interval
			trainQueue.enqueue(new Train(TRAIN_CAPACITY));
			trainCount++;
		} // else do nothing
	}
	
	/**
	 * Purpose: private helper method generatePassengers creates a random number of passengers between 0-5 inclusive 
	 * 			and adds each passenger to their corresponding startStation's passenger queue
	 * @param stationList List<Station> variable representing the list of Station objects
	 * @param passengerQueue QueueInterface<Passenger> variable representing the queue of all passengers
	 * @return int value representing the total number of passengers generated during the method call
	 */
	private static int generatePassengers(List<Station> stationList, QueueInterface<Passenger> passengerQueue) {
		// initialize variables
		Passenger tempPassenger = null;
		int numberOfPassengers = generator.nextInt(6);
		int startStation = 0;
		int stopStation = 0;
		
		for (int i = 0; i < numberOfPassengers; i++) {
			// reset startStation and stopStation variables so new random values can be calculated and assigned for each loop iteration
			startStation = 0;
			stopStation = 0;
			while (startStation >= stopStation) { // generate random values until startStation is less than stopStation
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
	
	/**
	 * Purpose: private helper method moveTrains simulates the moving of each train in the trainQueue during an iteration of the time interval
	 * @param trainQueue QueueInterface<Train> variable representing the queue of Train objects
	 * @param stationList List<Station> variable representing the list of Station objects
	 * @param time int variable representing the current time
	 */
	private static void moveTrains(QueueInterface<Train> trainQueue, List<Station> stationList, int time) {
		int currentTime = 0;
		int stationNumber = 0;
		// create temporary variable of the train count during this time interval in order to loop through each train
		int tempTrainCount = trainCount;
		int passengersUnloaded = 0;
		Train tempTrain = null;

		// iterate through each Train in the trainQueue once
		for (int i = 0; i < tempTrainCount; i++) {
			// remove the Train from the trainQueue
			tempTrain = trainQueue.dequeue();
			// move the Train	
			tempTrain.move();
			// save the current Train's remaining time to next station
			currentTime = tempTrain.timeToNext();
			
			if (currentTime == 0) { // then the Train has arrived at the next station
				stationNumber = tempTrain.nextStation(); // get the current Station's number

				// unload all passengers getting off at current station 
				passengersUnloaded = tempTrain.unloadPassengers(stationNumber);
				passengersDelivered += passengersUnloaded;
				passengersOnTrains -= passengersUnloaded;
				
				
				// load all passengers getting on to train from current station
				passengersOnTrains += tempTrain.loadPassengers(stationList.get(stationNumber), time);
				
				// update train to next station to move to
				tempTrain.updateStation(stationList.get(stationNumber + 1).getTimeToNextStation());
			}
			
			if (tempTrain.nextStation() == 4) { // Train has reached the end of the line, no more stations, remove Train from queue
				trainCount--;
			} else { // more Stations left for Train to visit, add back to end of queue
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

/**
 * Sample Output
 * 
Station has been created with time to next station: 12 minutes.
Station has been created with time to next station: 13 minutes.
Station has been created with time to next station: 6 minutes.
Station has been created with time to next station: 14 minutes.
Station has been created with time to next station: 5 minutes.
	Created train 1
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 4
	Train 1 unloaded 0 passengers at station 0
		loaded 2 passengers; Space left 48
-----
Current Time: 0.
Number of passengers waiting: 1.
Number of passengers on trains: 2.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 3 heading to 4
-----
Current Time: 1.
Number of passengers waiting: 3.
Number of passengers on trains: 2.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 3
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 4
-----
Current Time: 2.
Number of passengers waiting: 7.
Number of passengers on trains: 2.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 1
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 3
-----
Current Time: 3.
Number of passengers waiting: 12.
Number of passengers on trains: 2.
-----
	Created passenger at station 3 heading to 4
-----
Current Time: 4.
Number of passengers waiting: 13.
Number of passengers on trains: 2.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 3
-----
Current Time: 5.
Number of passengers waiting: 16.
Number of passengers on trains: 2.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 2 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 1
-----
Current Time: 6.
Number of passengers waiting: 20.
Number of passengers on trains: 2.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 4
-----
Current Time: 7.
Number of passengers waiting: 24.
Number of passengers on trains: 2.
-----
-----
Current Time: 8.
Number of passengers waiting: 24.
Number of passengers on trains: 2.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 2 heading to 4
-----
Current Time: 9.
Number of passengers waiting: 27.
Number of passengers on trains: 2.
-----
	Created train 2
	Created passenger at station 1 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 3
	Train 2 unloaded 0 passengers at station 0
		loaded 12 passengers; Space left 38
-----
Current Time: 10.
Number of passengers waiting: 19.
Number of passengers on trains: 14.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 3
-----
Current Time: 11.
Number of passengers waiting: 24.
Number of passengers on trains: 14.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 2 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 2
-----
Current Time: 12.
Number of passengers waiting: 29.
Number of passengers on trains: 14.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 2
	Train 1 unloaded 0 passengers at station 1
		loaded 11 passengers; Space left 37
-----
Current Time: 13.
Number of passengers waiting: 20.
Number of passengers on trains: 25.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 3
	Created passenger at station 3 heading to 4
-----
Current Time: 14.
Number of passengers waiting: 24.
Number of passengers on trains: 25.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 2 heading to 3
-----
Current Time: 15.
Number of passengers waiting: 26.
Number of passengers on trains: 25.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 3
-----
Current Time: 16.
Number of passengers waiting: 28.
Number of passengers on trains: 25.
-----
	Created passenger at station 2 heading to 3
-----
Current Time: 17.
Number of passengers waiting: 29.
Number of passengers on trains: 25.
-----
-----
Current Time: 18.
Number of passengers waiting: 29.
Number of passengers on trains: 25.
-----
	Created passenger at station 3 heading to 4
	Train 1 unloaded 3 passengers at station 2
		loaded 10 passengers; Space left 30
-----
Current Time: 19.
Number of passengers waiting: 20.
Number of passengers on trains: 32.
-----
	Created train 3
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 2
	Train 3 unloaded 0 passengers at station 0
		loaded 13 passengers; Space left 37
-----
Current Time: 20.
Number of passengers waiting: 10.
Number of passengers on trains: 45.
-----
	Created passenger at station 1 heading to 4
-----
Current Time: 21.
Number of passengers waiting: 11.
Number of passengers on trains: 45.
-----
	Created passenger at station 1 heading to 4
-----
Current Time: 22.
Number of passengers waiting: 12.
Number of passengers on trains: 45.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 2
	Train 2 unloaded 2 passengers at station 1
		loaded 5 passengers; Space left 35
-----
Current Time: 23.
Number of passengers waiting: 9.
Number of passengers on trains: 48.
-----
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 3
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 4
-----
Current Time: 24.
Number of passengers waiting: 13.
Number of passengers on trains: 48.
-----
	Created passenger at station 1 heading to 4
-----
Current Time: 25.
Number of passengers waiting: 14.
Number of passengers on trains: 48.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 4
-----
Current Time: 26.
Number of passengers waiting: 18.
Number of passengers on trains: 48.
-----
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 2 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 2
-----
Current Time: 27.
Number of passengers waiting: 23.
Number of passengers on trains: 48.
-----
	Created passenger at station 0 heading to 3
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 3
-----
Current Time: 28.
Number of passengers waiting: 27.
Number of passengers on trains: 48.
-----
	Created passenger at station 0 heading to 1
	Train 2 unloaded 5 passengers at station 2
		loaded 4 passengers; Space left 36
-----
Current Time: 29.
Number of passengers waiting: 24.
Number of passengers on trains: 47.
-----
	Created train 4
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 1
	Train 4 unloaded 0 passengers at station 0
		loaded 9 passengers; Space left 41
-----
Current Time: 30.
Number of passengers waiting: 18.
Number of passengers on trains: 56.
-----
	Created passenger at station 2 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 2
-----
Current Time: 31.
Number of passengers waiting: 21.
Number of passengers on trains: 56.
-----
-----
Current Time: 32.
Number of passengers waiting: 21.
Number of passengers on trains: 56.
-----
	Created passenger at station 0 heading to 1
	Train 1 unloaded 12 passengers at station 3
		loaded 8 passengers; Space left 34
	Train 3 unloaded 3 passengers at station 1
		loaded 10 passengers; Space left 30
-----
Current Time: 33.
Number of passengers waiting: 4.
Number of passengers on trains: 59.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 34.
Number of passengers waiting: 8.
Number of passengers on trains: 59.
-----
-----
Current Time: 35.
Number of passengers waiting: 8.
Number of passengers on trains: 59.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 2 heading to 4
	Created passenger at station 2 heading to 3
-----
Current Time: 36.
Number of passengers waiting: 11.
Number of passengers on trains: 59.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 3
	Created passenger at station 2 heading to 3
-----
Current Time: 37.
Number of passengers waiting: 15.
Number of passengers on trains: 59.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 4
-----
Current Time: 38.
Number of passengers waiting: 18.
Number of passengers on trains: 59.
-----
	Created passenger at station 1 heading to 4
	Train 3 unloaded 8 passengers at station 2
		loaded 6 passengers; Space left 32
-----
Current Time: 39.
Number of passengers waiting: 13.
Number of passengers on trains: 57.
-----
	Created train 5
	Created passenger at station 0 heading to 1
	Train 5 unloaded 0 passengers at station 0
		loaded 8 passengers; Space left 42
-----
Current Time: 40.
Number of passengers waiting: 6.
Number of passengers on trains: 65.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 41.
Number of passengers waiting: 8.
Number of passengers on trains: 65.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 1
-----
Current Time: 42.
Number of passengers waiting: 10.
Number of passengers on trains: 65.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 4
	Created passenger at station 2 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 3
	Train 2 unloaded 3 passengers at station 3
		loaded 1 passengers; Space left 38
	Train 4 unloaded 2 passengers at station 1
		loaded 7 passengers; Space left 36
-----
Current Time: 43.
Number of passengers waiting: 7.
Number of passengers on trains: 68.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 1
-----
Current Time: 44.
Number of passengers waiting: 11.
Number of passengers on trains: 68.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 1
	Created passenger at station 3 heading to 4
	Created passenger at station 2 heading to 4
-----
Current Time: 45.
Number of passengers waiting: 16.
Number of passengers on trains: 68.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 3
-----
Current Time: 46.
Number of passengers waiting: 21.
Number of passengers on trains: 68.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 1
-----
Current Time: 47.
Number of passengers waiting: 24.
Number of passengers on trains: 68.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 4
-----
Current Time: 48.
Number of passengers waiting: 29.
Number of passengers on trains: 68.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 4
	Train 4 unloaded 2 passengers at station 2
		loaded 5 passengers; Space left 33
-----
Current Time: 49.
Number of passengers waiting: 29.
Number of passengers on trains: 71.
-----
	Created train 6
	Created passenger at station 2 heading to 4
	Created passenger at station 2 heading to 4
	Created passenger at station 1 heading to 2
	Train 6 unloaded 0 passengers at station 0
		loaded 17 passengers; Space left 33
-----
Current Time: 50.
Number of passengers waiting: 15.
Number of passengers on trains: 88.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 3
-----
Current Time: 51.
Number of passengers waiting: 18.
Number of passengers on trains: 88.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 2
-----
Current Time: 52.
Number of passengers waiting: 20.
Number of passengers on trains: 88.
-----
	Train 3 unloaded 10 passengers at station 3
		loaded 5 passengers; Space left 37
	Train 5 unloaded 2 passengers at station 1
		loaded 10 passengers; Space left 34
-----
Current Time: 53.
Number of passengers waiting: 5.
Number of passengers on trains: 91.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 3
-----
Current Time: 54.
Number of passengers waiting: 7.
Number of passengers on trains: 91.
-----
-----
Current Time: 55.
Number of passengers waiting: 7.
Number of passengers on trains: 91.
-----
-----
Current Time: 56.
Number of passengers waiting: 7.
Number of passengers on trains: 91.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 1
-----
Current Time: 57.
Number of passengers waiting: 11.
Number of passengers on trains: 91.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 4
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 2
-----
Current Time: 58.
Number of passengers waiting: 16.
Number of passengers on trains: 91.
-----
	Created passenger at station 2 heading to 4
	Train 5 unloaded 8 passengers at station 2
		loaded 4 passengers; Space left 38
-----
Current Time: 59.
Number of passengers waiting: 13.
Number of passengers on trains: 87.
-----
	Created train 7
	Created passenger at station 0 heading to 3
	Train 7 unloaded 0 passengers at station 0
		loaded 10 passengers; Space left 40
-----
Current Time: 60.
Number of passengers waiting: 4.
Number of passengers on trains: 97.
-----
-----
Current Time: 61.
Number of passengers waiting: 4.
Number of passengers on trains: 97.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 3
-----
Current Time: 62.
Number of passengers waiting: 6.
Number of passengers on trains: 97.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 1
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 1
	Train 4 unloaded 9 passengers at station 3
		loaded 0 passengers; Space left 42
	Train 6 unloaded 6 passengers at station 1
		loaded 7 passengers; Space left 32
-----
Current Time: 63.
Number of passengers waiting: 3.
Number of passengers on trains: 89.
-----
-----
Current Time: 64.
Number of passengers waiting: 3.
Number of passengers on trains: 89.
-----
	Created passenger at station 0 heading to 1
-----
Current Time: 65.
Number of passengers waiting: 4.
Number of passengers on trains: 89.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 3
-----
Current Time: 66.
Number of passengers waiting: 8.
Number of passengers on trains: 89.
-----
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 3 heading to 4
-----
Current Time: 67.
Number of passengers waiting: 11.
Number of passengers on trains: 89.
-----
	Created passenger at station 2 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 1
-----
Current Time: 68.
Number of passengers waiting: 16.
Number of passengers on trains: 89.
-----
	Created passenger at station 0 heading to 1
	Train 6 unloaded 2 passengers at station 2
		loaded 3 passengers; Space left 31
-----
Current Time: 69.
Number of passengers waiting: 14.
Number of passengers on trains: 90.
-----
	Created train 8
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 4
	Train 8 unloaded 0 passengers at station 0
		loaded 10 passengers; Space left 40
-----
Current Time: 70.
Number of passengers waiting: 6.
Number of passengers on trains: 100.
-----
	Created passenger at station 0 heading to 3
	Created passenger at station 1 heading to 3
-----
Current Time: 71.
Number of passengers waiting: 8.
Number of passengers on trains: 100.
-----
-----
Current Time: 72.
Number of passengers waiting: 8.
Number of passengers on trains: 100.
-----
	Created passenger at station 2 heading to 4
	Train 5 unloaded 3 passengers at station 3
		loaded 1 passengers; Space left 40
	Train 7 unloaded 3 passengers at station 1
		loaded 6 passengers; Space left 37
-----
Current Time: 73.
Number of passengers waiting: 2.
Number of passengers on trains: 101.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 2
	Created passenger at station 2 heading to 3
	Created passenger at station 2 heading to 4
-----
Current Time: 74.
Number of passengers waiting: 6.
Number of passengers on trains: 101.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 2
-----
Current Time: 75.
Number of passengers waiting: 9.
Number of passengers on trains: 101.
-----
-----
Current Time: 76.
Number of passengers waiting: 9.
Number of passengers on trains: 101.
-----
	Created passenger at station 1 heading to 2
-----
Current Time: 77.
Number of passengers waiting: 10.
Number of passengers on trains: 101.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 2
-----
Current Time: 78.
Number of passengers waiting: 13.
Number of passengers on trains: 101.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 4
	Train 7 unloaded 3 passengers at station 2
		loaded 4 passengers; Space left 36
-----
Current Time: 79.
Number of passengers waiting: 14.
Number of passengers on trains: 102.
-----
	Created train 9
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 2
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 2
	Train 9 unloaded 0 passengers at station 0
		loaded 10 passengers; Space left 40
-----
Current Time: 80.
Number of passengers waiting: 9.
Number of passengers on trains: 112.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 3
	Created passenger at station 2 heading to 4
-----
Current Time: 81.
Number of passengers waiting: 12.
Number of passengers on trains: 112.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 82.
Number of passengers waiting: 17.
Number of passengers on trains: 112.
-----
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 3
	Train 6 unloaded 6 passengers at station 3
		loaded 0 passengers; Space left 37
	Train 8 unloaded 5 passengers at station 1
		loaded 11 passengers; Space left 34
-----
Current Time: 83.
Number of passengers waiting: 8.
Number of passengers on trains: 112.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 84.
Number of passengers waiting: 10.
Number of passengers on trains: 112.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 2 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 2 heading to 4
-----
Current Time: 85.
Number of passengers waiting: 15.
Number of passengers on trains: 112.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 3
-----
Current Time: 86.
Number of passengers waiting: 17.
Number of passengers on trains: 112.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 3 heading to 4
	Created passenger at station 2 heading to 4
-----
Current Time: 87.
Number of passengers waiting: 21.
Number of passengers on trains: 112.
-----
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 4
-----
Current Time: 88.
Number of passengers waiting: 25.
Number of passengers on trains: 112.
-----
	Train 8 unloaded 6 passengers at station 2
		loaded 8 passengers; Space left 32
-----
Current Time: 89.
Number of passengers waiting: 17.
Number of passengers on trains: 114.
-----
	Created train 10
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 4
	Created passenger at station 3 heading to 4
	Train 10 unloaded 0 passengers at station 0
		loaded 10 passengers; Space left 40
-----
Current Time: 90.
Number of passengers waiting: 12.
Number of passengers on trains: 124.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 3
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 2
-----
Current Time: 91.
Number of passengers waiting: 17.
Number of passengers on trains: 124.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 2
-----
Current Time: 92.
Number of passengers waiting: 19.
Number of passengers on trains: 124.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 3
	Train 7 unloaded 7 passengers at station 3
		loaded 5 passengers; Space left 38
	Train 9 unloaded 0 passengers at station 1
		loaded 13 passengers; Space left 27
-----
Current Time: 93.
Number of passengers waiting: 6.
Number of passengers on trains: 135.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 2 heading to 3
-----
Current Time: 94.
Number of passengers waiting: 10.
Number of passengers on trains: 135.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 3
-----
Current Time: 95.
Number of passengers waiting: 13.
Number of passengers on trains: 135.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 4
-----
Current Time: 96.
Number of passengers waiting: 16.
Number of passengers on trains: 135.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 2 heading to 4
	Created passenger at station 2 heading to 3
-----
Current Time: 97.
Number of passengers waiting: 20.
Number of passengers on trains: 135.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 2
-----
Current Time: 98.
Number of passengers waiting: 25.
Number of passengers on trains: 135.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 2
	Train 9 unloaded 12 passengers at station 2
		loaded 6 passengers; Space left 33
-----
Current Time: 99.
Number of passengers waiting: 21.
Number of passengers on trains: 129.
-----
	Created train 11
	Created passenger at station 0 heading to 3
	Created passenger at station 2 heading to 4
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 1
	Train 11 unloaded 0 passengers at station 0
		loaded 18 passengers; Space left 32
-----
Current Time: 100.
Number of passengers waiting: 8.
Number of passengers on trains: 147.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 2
	Created passenger at station 2 heading to 4
	Created passenger at station 1 heading to 2
-----
Current Time: 101.
Number of passengers waiting: 13.
Number of passengers on trains: 147.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 1 heading to 3
-----
Current Time: 102.
Number of passengers waiting: 16.
Number of passengers on trains: 147.
-----
	Train 8 unloaded 7 passengers at station 3
		loaded 4 passengers; Space left 35
	Train 10 unloaded 1 passengers at station 1
		loaded 7 passengers; Space left 34
-----
Current Time: 103.
Number of passengers waiting: 5.
Number of passengers on trains: 150.
-----
	Created passenger at station 1 heading to 3
-----
Current Time: 104.
Number of passengers waiting: 6.
Number of passengers on trains: 150.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 105.
Number of passengers waiting: 9.
Number of passengers on trains: 150.
-----
-----
Current Time: 106.
Number of passengers waiting: 9.
Number of passengers on trains: 150.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 107.
Number of passengers waiting: 12.
Number of passengers on trains: 150.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 3
	Created passenger at station 3 heading to 4
	Created passenger at station 2 heading to 4
-----
Current Time: 108.
Number of passengers waiting: 17.
Number of passengers on trains: 150.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 2
	Train 10 unloaded 7 passengers at station 2
		loaded 5 passengers; Space left 36
-----
Current Time: 109.
Number of passengers waiting: 15.
Number of passengers on trains: 148.
-----
	Created train 12
	Train 12 unloaded 0 passengers at station 0
		loaded 7 passengers; Space left 43
-----
Current Time: 110.
Number of passengers waiting: 8.
Number of passengers on trains: 155.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 111.
Number of passengers waiting: 13.
Number of passengers on trains: 155.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 2
-----
Current Time: 112.
Number of passengers waiting: 15.
Number of passengers on trains: 155.
-----
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 1
	Train 9 unloaded 11 passengers at station 3
		loaded 4 passengers; Space left 40
	Train 11 unloaded 5 passengers at station 1
		loaded 9 passengers; Space left 28
-----
Current Time: 113.
Number of passengers waiting: 6.
Number of passengers on trains: 152.
-----
	Created passenger at station 2 heading to 3
-----
Current Time: 114.
Number of passengers waiting: 7.
Number of passengers on trains: 152.
-----
	Created passenger at station 0 heading to 4
-----
Current Time: 115.
Number of passengers waiting: 8.
Number of passengers on trains: 152.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 3
	Created passenger at station 1 heading to 2
-----
Current Time: 116.
Number of passengers waiting: 11.
Number of passengers on trains: 152.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 4
-----
Current Time: 117.
Number of passengers waiting: 13.
Number of passengers on trains: 152.
-----
-----
Current Time: 118.
Number of passengers waiting: 13.
Number of passengers on trains: 152.
-----
	Created passenger at station 1 heading to 4
	Train 11 unloaded 8 passengers at station 2
		loaded 3 passengers; Space left 33
-----
Current Time: 119.
Number of passengers waiting: 11.
Number of passengers on trains: 147.
-----
	Created train 13
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 2
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 4
	Train 13 unloaded 0 passengers at station 0
		loaded 9 passengers; Space left 41
-----
Current Time: 120.
Number of passengers waiting: 7.
Number of passengers on trains: 156.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 3
-----
Current Time: 121.
Number of passengers waiting: 10.
Number of passengers on trains: 156.
-----
-----
Current Time: 122.
Number of passengers waiting: 10.
Number of passengers on trains: 156.
-----
	Train 10 unloaded 6 passengers at station 3
		loaded 0 passengers; Space left 42
	Train 12 unloaded 1 passengers at station 1
		loaded 9 passengers; Space left 35
-----
Current Time: 123.
Number of passengers waiting: 1.
Number of passengers on trains: 158.
-----
-----
Current Time: 124.
Number of passengers waiting: 1.
Number of passengers on trains: 158.
-----
-----
Current Time: 125.
Number of passengers waiting: 1.
Number of passengers on trains: 158.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 4
-----
Current Time: 126.
Number of passengers waiting: 5.
Number of passengers on trains: 158.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 2
	Created passenger at station 3 heading to 4
-----
Current Time: 127.
Number of passengers waiting: 8.
Number of passengers on trains: 158.
-----
	Created passenger at station 0 heading to 3
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 3 heading to 4
-----
Current Time: 128.
Number of passengers waiting: 12.
Number of passengers on trains: 158.
-----
	Created passenger at station 1 heading to 2
	Train 12 unloaded 9 passengers at station 2
		loaded 1 passengers; Space left 43
-----
Current Time: 129.
Number of passengers waiting: 12.
Number of passengers on trains: 150.
-----
	Created train 14
	Created passenger at station 2 heading to 3
	Created passenger at station 3 heading to 4
	Train 14 unloaded 0 passengers at station 0
		loaded 5 passengers; Space left 45
-----
Current Time: 130.
Number of passengers waiting: 9.
Number of passengers on trains: 155.
-----
-----
Current Time: 131.
Number of passengers waiting: 9.
Number of passengers on trains: 155.
-----
-----
Current Time: 132.
Number of passengers waiting: 9.
Number of passengers on trains: 155.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 3
	Created passenger at station 3 heading to 4
	Train 11 unloaded 10 passengers at station 3
		loaded 5 passengers; Space left 38
	Train 13 unloaded 3 passengers at station 1
		loaded 5 passengers; Space left 39
-----
Current Time: 133.
Number of passengers waiting: 3.
Number of passengers on trains: 152.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 3
	Created passenger at station 2 heading to 4
	Created passenger at station 2 heading to 3
-----
Current Time: 134.
Number of passengers waiting: 7.
Number of passengers on trains: 152.
-----
	Created passenger at station 3 heading to 4
-----
Current Time: 135.
Number of passengers waiting: 8.
Number of passengers on trains: 152.
-----
-----
Current Time: 136.
Number of passengers waiting: 8.
Number of passengers on trains: 152.
-----
	Created passenger at station 0 heading to 4
-----
Current Time: 137.
Number of passengers waiting: 9.
Number of passengers on trains: 152.
-----
	Created passenger at station 1 heading to 2
-----
Current Time: 138.
Number of passengers waiting: 10.
Number of passengers on trains: 152.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 1
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 1
	Train 13 unloaded 5 passengers at station 2
		loaded 6 passengers; Space left 38
-----
Current Time: 139.
Number of passengers waiting: 9.
Number of passengers on trains: 153.
-----
	Created train 15
	Created passenger at station 0 heading to 2
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 2
	Train 15 unloaded 0 passengers at station 0
		loaded 6 passengers; Space left 44
-----
Current Time: 140.
Number of passengers waiting: 6.
Number of passengers on trains: 159.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 2
-----
Current Time: 141.
Number of passengers waiting: 10.
Number of passengers on trains: 159.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 2
-----
Current Time: 142.
Number of passengers waiting: 13.
Number of passengers on trains: 159.
-----
	Train 12 unloaded 4 passengers at station 3
		loaded 3 passengers; Space left 44
	Train 14 unloaded 0 passengers at station 1
		loaded 6 passengers; Space left 39
-----
Current Time: 143.
Number of passengers waiting: 4.
Number of passengers on trains: 164.
-----
	Created passenger at station 2 heading to 3
-----
Current Time: 144.
Number of passengers waiting: 5.
Number of passengers on trains: 164.
-----
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 1
-----
Current Time: 145.
Number of passengers waiting: 9.
Number of passengers on trains: 164.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 1
	Created passenger at station 2 heading to 3
-----
Current Time: 146.
Number of passengers waiting: 13.
Number of passengers on trains: 164.
-----
	Created passenger at station 0 heading to 3
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 3
-----
Current Time: 147.
Number of passengers waiting: 16.
Number of passengers on trains: 164.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 2
-----
Current Time: 148.
Number of passengers waiting: 20.
Number of passengers on trains: 164.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 3
	Train 14 unloaded 5 passengers at station 2
		loaded 4 passengers; Space left 40
-----
Current Time: 149.
Number of passengers waiting: 18.
Number of passengers on trains: 163.
-----
	Created train 16
	Created passenger at station 0 heading to 3
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 4
	Created passenger at station 2 heading to 3
	Train 16 unloaded 0 passengers at station 0
		loaded 11 passengers; Space left 39
-----
Current Time: 150.
Number of passengers waiting: 12.
Number of passengers on trains: 174.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 2 heading to 3
-----
Current Time: 151.
Number of passengers waiting: 16.
Number of passengers on trains: 174.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 1
-----
Current Time: 152.
Number of passengers waiting: 21.
Number of passengers on trains: 174.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 2 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 4
	Train 13 unloaded 7 passengers at station 3
		loaded 3 passengers; Space left 42
	Train 15 unloaded 2 passengers at station 1
		loaded 12 passengers; Space left 34
-----
Current Time: 153.
Number of passengers waiting: 10.
Number of passengers on trains: 180.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 4
	Created passenger at station 2 heading to 4
-----
Current Time: 154.
Number of passengers waiting: 13.
Number of passengers on trains: 180.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 4
-----
Current Time: 155.
Number of passengers waiting: 17.
Number of passengers on trains: 180.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 2 heading to 4
	Created passenger at station 2 heading to 3
-----
Current Time: 156.
Number of passengers waiting: 20.
Number of passengers on trains: 180.
-----
-----
Current Time: 157.
Number of passengers waiting: 20.
Number of passengers on trains: 180.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 3
-----
Current Time: 158.
Number of passengers waiting: 24.
Number of passengers on trains: 180.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 4
	Train 15 unloaded 7 passengers at station 2
		loaded 8 passengers; Space left 33
-----
Current Time: 159.
Number of passengers waiting: 20.
Number of passengers on trains: 181.
-----
	Created train 17
	Created passenger at station 0 heading to 2
	Train 17 unloaded 0 passengers at station 0
		loaded 15 passengers; Space left 35
-----
Current Time: 160.
Number of passengers waiting: 6.
Number of passengers on trains: 196.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 1
-----
Current Time: 161.
Number of passengers waiting: 9.
Number of passengers on trains: 196.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 2
-----
Current Time: 162.
Number of passengers waiting: 13.
Number of passengers on trains: 196.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 2
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 3
	Train 14 unloaded 6 passengers at station 3
		loaded 3 passengers; Space left 43
	Train 16 unloaded 4 passengers at station 1
		loaded 6 passengers; Space left 37
-----
Current Time: 163.
Number of passengers waiting: 9.
Number of passengers on trains: 195.
-----
-----
Current Time: 164.
Number of passengers waiting: 9.
Number of passengers on trains: 195.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 2
-----
Current Time: 165.
Number of passengers waiting: 12.
Number of passengers on trains: 195.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 3
	Created passenger at station 1 heading to 4
-----
Current Time: 166.
Number of passengers waiting: 17.
Number of passengers on trains: 195.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 4
	Created passenger at station 2 heading to 3
-----
Current Time: 167.
Number of passengers waiting: 22.
Number of passengers on trains: 195.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 3
	Created passenger at station 2 heading to 3
	Created passenger at station 2 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 168.
Number of passengers waiting: 27.
Number of passengers on trains: 195.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 2
	Created passenger at station 2 heading to 3
	Train 16 unloaded 4 passengers at station 2
		loaded 7 passengers; Space left 34
-----
Current Time: 169.
Number of passengers waiting: 24.
Number of passengers on trains: 198.
-----
	Created train 18
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 3
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 1
	Train 18 unloaded 0 passengers at station 0
		loaded 16 passengers; Space left 34
-----
Current Time: 170.
Number of passengers waiting: 12.
Number of passengers on trains: 214.
-----
-----
Current Time: 171.
Number of passengers waiting: 12.
Number of passengers on trains: 214.
-----
-----
Current Time: 172.
Number of passengers waiting: 12.
Number of passengers on trains: 214.
-----
	Created passenger at station 0 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 4
	Created passenger at station 3 heading to 4
	Train 15 unloaded 11 passengers at station 3
		loaded 4 passengers; Space left 40
	Train 17 unloaded 4 passengers at station 1
		loaded 9 passengers; Space left 30
-----
Current Time: 173.
Number of passengers waiting: 3.
Number of passengers on trains: 212.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 3
-----
Current Time: 174.
Number of passengers waiting: 7.
Number of passengers on trains: 212.
-----
-----
Current Time: 175.
Number of passengers waiting: 7.
Number of passengers on trains: 212.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 1
-----
Current Time: 176.
Number of passengers waiting: 12.
Number of passengers on trains: 212.
-----
-----
Current Time: 177.
Number of passengers waiting: 12.
Number of passengers on trains: 212.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 2
-----
Current Time: 178.
Number of passengers waiting: 17.
Number of passengers on trains: 212.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 2 heading to 4
	Train 17 unloaded 9 passengers at station 2
		loaded 2 passengers; Space left 37
-----
Current Time: 179.
Number of passengers waiting: 17.
Number of passengers on trains: 205.
-----
	Created train 19
	Created passenger at station 1 heading to 3
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 2
	Train 19 unloaded 0 passengers at station 0
		loaded 6 passengers; Space left 44
-----
Current Time: 180.
Number of passengers waiting: 14.
Number of passengers on trains: 211.
-----
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 2
	Created passenger at station 2 heading to 3
-----
Current Time: 181.
Number of passengers waiting: 17.
Number of passengers on trains: 211.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 0 heading to 4
	Created passenger at station 2 heading to 3
-----
Current Time: 182.
Number of passengers waiting: 21.
Number of passengers on trains: 211.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 2 heading to 4
	Created passenger at station 2 heading to 3
	Train 16 unloaded 9 passengers at station 3
		loaded 4 passengers; Space left 39
	Train 18 unloaded 3 passengers at station 1
		loaded 13 passengers; Space left 24
-----
Current Time: 183.
Number of passengers waiting: 7.
Number of passengers on trains: 216.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 4
-----
Current Time: 184.
Number of passengers waiting: 11.
Number of passengers on trains: 216.
-----
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 185.
Number of passengers waiting: 13.
Number of passengers on trains: 216.
-----
-----
Current Time: 186.
Number of passengers waiting: 13.
Number of passengers on trains: 216.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 2
	Created passenger at station 2 heading to 4
-----
Current Time: 187.
Number of passengers waiting: 17.
Number of passengers on trains: 216.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 0 heading to 3
	Created passenger at station 3 heading to 4
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 4
-----
Current Time: 188.
Number of passengers waiting: 22.
Number of passengers on trains: 216.
-----
	Created passenger at station 0 heading to 3
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 3
	Train 18 unloaded 11 passengers at station 2
		loaded 6 passengers; Space left 29
-----
Current Time: 189.
Number of passengers waiting: 19.
Number of passengers on trains: 211.
-----
	Created train 20
	Created passenger at station 3 heading to 4
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 1
	Train 20 unloaded 0 passengers at station 0
		loaded 14 passengers; Space left 36
-----
Current Time: 190.
Number of passengers waiting: 10.
Number of passengers on trains: 225.
-----
	Created passenger at station 1 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 4
-----
Current Time: 191.
Number of passengers waiting: 15.
Number of passengers on trains: 225.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 1 heading to 4
	Created passenger at station 3 heading to 4
-----
Current Time: 192.
Number of passengers waiting: 18.
Number of passengers on trains: 225.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 1 heading to 2
	Train 17 unloaded 6 passengers at station 3
		loaded 4 passengers; Space left 39
	Train 19 unloaded 3 passengers at station 1
		loaded 12 passengers; Space left 35
-----
Current Time: 193.
Number of passengers waiting: 4.
Number of passengers on trains: 232.
-----
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 4
-----
Current Time: 194.
Number of passengers waiting: 6.
Number of passengers on trains: 232.
-----
	Created passenger at station 0 heading to 1
	Created passenger at station 2 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 1 heading to 3
	Created passenger at station 3 heading to 4
-----
Current Time: 195.
Number of passengers waiting: 11.
Number of passengers on trains: 232.
-----
	Created passenger at station 0 heading to 2
	Created passenger at station 1 heading to 4
	Created passenger at station 0 heading to 2
	Created passenger at station 2 heading to 4
-----
Current Time: 196.
Number of passengers waiting: 15.
Number of passengers on trains: 232.
-----
	Created passenger at station 2 heading to 3
	Created passenger at station 2 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 4
	Created passenger at station 0 heading to 2
-----
Current Time: 197.
Number of passengers waiting: 20.
Number of passengers on trains: 232.
-----
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 2
	Created passenger at station 0 heading to 3
	Created passenger at station 1 heading to 2
-----
Current Time: 198.
Number of passengers waiting: 25.
Number of passengers on trains: 232.
-----
	Train 19 unloaded 5 passengers at station 2
		loaded 6 passengers; Space left 34
-----
Current Time: 199.
Number of passengers waiting: 19.
Number of passengers on trains: 233.
-----
	Created train 21
	Created passenger at station 3 heading to 4
	Created passenger at station 1 heading to 3
	Created passenger at station 0 heading to 4
	Created passenger at station 1 heading to 2
	Created passenger at station 0 heading to 2
	Train 21 unloaded 0 passengers at station 0
		loaded 12 passengers; Space left 38
-----
Current Time: 200.
Number of passengers waiting: 12.
Number of passengers on trains: 245.
-----
Final Report
The total number of passengers is 552
The number of passengers currently on a train 245
The number of passengers delivered is 295
The average wait time for passengers that have boarded is
105.3
The average wait time for passengers that have not yet boarded is
199.5
*/