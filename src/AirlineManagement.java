import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface representing airline management functionalities.
 */
public interface AirlineManagement {

    /**
     * Adds a flight to the airline's schedule.
     *
     * @param flight the flight object to be added
     * @return true if the flight is successfully added, false otherwise
     */
    boolean addFlight(Flight flight);

    /**
     * Cancels a flight in the airline's schedule.
     *
     * @param flightNumber the flight number of the flight to be canceled
     */
    void cancelFlight(String flightNumber);

    /**
     * Delays a flight by updating its departure and arrival times.
     *
     * @param flightNumber     the flight number of the flight to be delayed
     * @param newDepartureTime the new departure time of the flight
     * @param newArrivalTime   the new arrival time of the flight
     */
    void delayFlight(String flightNumber, LocalDateTime newDepartureTime, LocalDateTime newArrivalTime);

    /**
     * Retrieves the details of a specific flight.
     *
     * @param flightNumber the flight number of the desired flight
     * @return the flight object corresponding to the flight number, or null if not found
     */
    Flight getFlightDetails(String flightNumber);

    /**
     * Retrieves a list of all flights in the airline's schedule.
     *
     * @return a list containing all flights
     */
    List<Flight> getAllFlights();
}
