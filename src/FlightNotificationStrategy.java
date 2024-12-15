import java.util.List;

/**
 * A strategy class for notifying passengers about flight-related updates.
 * Implements the PassengerNotification interface.
 */
public class FlightNotificationStrategy implements PassengerNotification {
    private List<Flight> flights; // List of flights to manage notifications for

    /**
     * Constructor to initialize the notification strategy with a list of flights.
     *
     * @param flights The list of flights to be managed.
     */
    public FlightNotificationStrategy(List<Flight> flights) {
        this.flights = flights;
    }

    /**
     * Sends a notification to all passengers of a specific flight.
     *
     * @param flightNumber The flight number for which the notification is to be sent.
     * @param message      The content of the notification message.
     * @param type         The type of notification (e.g., "Cancellation", "Delay").
     */
    public void sendNotification(String flightNumber, String message, String type) {
        // Retrieve the flight details based on the flight number
        Flight flight = getFlightDetails(flightNumber);

        if (flight != null) {
            // Notify each passenger of the flight
            for (Passenger passenger : flight.getPassengers()) {
                System.out.println("Sending " + type + " notification to "
                        + passenger.getName() + ": " + message);
                // Replace this with actual implementation, e.g., email or SMS sending
            }
        } else {
            System.out.println("Flight " + flightNumber + " not found.");
        }
    }

    /**
     * Retrieves the flight details for a given flight number.
     *
     * @param flightNumber The flight number to search for.
     * @return The Flight object if found, otherwise null.
     */
    private Flight getFlightDetails(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }
}
