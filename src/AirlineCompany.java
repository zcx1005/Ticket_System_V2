import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Represents an airline company that manages flights, handles flight modifications,
 * and notifies passengers about flight updates such as cancellations and delays.
 */
public class AirlineCompany implements AirlineManagement {
    private final String name; // Airline company name
    private List<Flight> flights; // List of flights managed by the airline

    /**
     * Constructor to initialize the airline company with its name and an empty flight list.
     *
     * @param name The name of the airline company
     */
    public AirlineCompany(String name) {
        this.name = name;
        this.flights = new ArrayList<>();
    }

    /**
     * Retrieves the name of the airline company.
     *
     * @return The airline company name with a suffix
     */
    public String getName() {
        return this.name + " AirlineCompany";
    }

    /**
     * Adds a flight to the airline's flight list after validating the flight details.
     *
     * @param flight The flight object to be added
     * @return True if the flight was successfully added, otherwise false
     */
    @Override
    public boolean addFlight(Flight flight) {
        // Validate flight information
        if (flight.getFlightNumber().isEmpty() || flight.getDepartureTime() == null || flight.getArrivalTime() == null ||
                flight.getDeparture() == null || flight.getDestination() == null ||
                flight.getCapacity() <= 0) {
            System.out.println("Incomplete flight information. Please provide all details.");
            return false;
        }

        // Add flight to the list and set it open for reservation
        flights.add(flight);
        flight.setOpenForReservation(true);
        System.out.println("Flight " + flight.getFlightNumber() + " from " + flight.getDeparture() +
                " to " + flight.getDestination() + " has been successfully added.");
        return true;
    }

    /**
     * Cancels a flight by its flight number. If passengers have booked the flight,
     * they are notified, otherwise the flight is removed from the list.
     *
     * @param flightNumber The flight number of the flight to be cancelled
     */
    @Override
    public void cancelFlight(String flightNumber) {
        try {
            // Find the target flight
            Flight targetFlight = getFlightDetails(flightNumber);

            if (targetFlight == null) {
                System.out.println("Flight " + flightNumber + " not found.");
                return;
            }

            // If no passengers have booked
            if (targetFlight.getPassengers().isEmpty()) {
                flights.remove(targetFlight);
                System.out.println("No passengers have booked this flight. The flight has been successfully cancelled.");
            } else {
                targetFlight.setStatus(FlightStatus.CANCELLED);
                System.out.println("Flight " + flightNumber + " has been marked as cancelled.");

                // Notify passengers
                FlightNotificationStrategy notificationStrategy = new FlightNotificationStrategy(flights);
                notificationStrategy.sendNotification(
                        flightNumber,
                        "We apologize for the inconvenience. Please contact our customer service for assistance.",
                        "Cancellation"
                );

                System.out.println("All passengers who have booked this flight have been notified about the cancellation.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred while cancelling the flight: " + e.getMessage());
        }
    }

    /**
     * Delays a flight and updates its departure and arrival times. Passengers
     * are notified about the new timings.
     *
     * @param flightNumber     The flight number of the delayed flight
     * @param newDepartureTime The new departure time
     * @param newArrivalTime   The new arrival time
     */
    @Override
    public void delayFlight(String flightNumber, LocalDateTime newDepartureTime, LocalDateTime newArrivalTime) {
        Flight targetFlight = getFlightDetails(flightNumber);

        if (targetFlight == null) {
            throw new IllegalArgumentException("Flight " + flightNumber + " not found.");
        }
        targetFlight.delay(newDepartureTime, newArrivalTime);
        if (targetFlight.getPassengers().isEmpty()) {
            System.out.println("No passengers have booked this flight.");
        } else {
            FlightNotificationStrategy notificationStrategy = new FlightNotificationStrategy(flights);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            notificationStrategy.sendNotification(flightNumber,
                    "The flight has been delayed. New departure time: "
                            + newDepartureTime.format(formatter) + ", new arrival time: "
                            + newArrivalTime.format(formatter),
                    "Delay");
            targetFlight.setStatus(FlightStatus.DELAYED);
            System.out.println("All passengers who have booked this flight have been notified about the delay.");
        }
    }

    /**
     * Retrieves flight details by its flight number.
     *
     * @param flightNumber The flight number
     * @return The flight object if found, otherwise null
     */
    @Override
    public Flight getFlightDetails(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    /**
     * Retrieves the list of all flights managed by the airline.
     *
     * @return A list of all flights
     */
    @Override
    public List<Flight> getAllFlights() {
        return flights;
    }

    /**
     * Identifies the most popular routes based on the number of flights.
     *
     * This method calculates the popularity of each route by counting how many times each
     * route appears in the list of flights. It then returns the top 3 most popular routes
     * along with the number of flights for each route.
     *
     * @return A list of the top 3 popular routes with flight counts, formatted as
     *         "Departure - Destination (flight count)".
     */
    public List<String> getPopularRoutes() {
        // Use a HashMap to count the occurrences of each route (departure to destination)
        Map<String, Integer> routeCountMap = new HashMap<>();

        // Iterate over all flights to update the route counts in the map
        for (Flight flight : flights) {
            // Construct the route string as "departure - destination"
            String route = flight.getDeparture() + " - " + flight.getDestination();
            // Increment the count for the route (default to 0 if not present)
            routeCountMap.put(route, routeCountMap.getOrDefault(route, 0) + 1);
        }

        // Use a priority queue (min-heap) to keep track of the top 3 most popular routes
        PriorityQueue<Map.Entry<String, Integer>> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        // Add all routes to the priority queue, and ensure the heap never exceeds 3 elements
        for (Map.Entry<String, Integer> entry : routeCountMap.entrySet()) {
            minHeap.offer(entry);  // Add the route to the heap
            if (minHeap.size() > 3) {
                minHeap.poll(); // Remove the route with the lowest count if the heap exceeds 3
            }
        }

        // Transfer the routes from the heap to a list, ensuring they are ordered by frequency
        List<String> popularRoutes = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            Map.Entry<String, Integer> entry = minHeap.poll();
            // Add each route and its flight count to the list in the correct format
            popularRoutes.add(entry.getKey() + " (" + entry.getValue() + " flights)");
        }

        // The heap is a min-heap, so the elements are ordered from lowest to highest frequency.
        // Reverse the list to get the most popular routes first.
        Collections.reverse(popularRoutes);

        return popularRoutes;
    }


    /**
     * Retrieves flights that are nearly full (90% or more seats booked).
     *
     * @return A list of nearly full flights
     */
    public List<Flight> getNearlyFullFlights() {
        List<Flight> nearlyFullFlights = new ArrayList<>(flights.size());

        for (Flight flight : flights) {
            int bookedPassengers = flight.getPassengers().size();
            int capacity = flight.getCapacity();

            if (bookedPassengers > capacity * 0.9) {
                nearlyFullFlights.add(flight);
            }
        }

        if (nearlyFullFlights.isEmpty()) {
            System.out.println("All flights have sufficient available seats.");
        }

        return nearlyFullFlights;
    }
}
