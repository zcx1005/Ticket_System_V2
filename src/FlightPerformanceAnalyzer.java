import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FlightPerformanceAnalyzer is a utility class for analyzing the performance of flights.
 * It provides methods to calculate metrics such as on-time rate, seat occupancy trends,
 * and flight cancellation rate based on a list of flight data.
 */
public class FlightPerformanceAnalyzer {
    private List<Flight> flights; // List of flights to be analyzed

    /**
     * Constructor for FlightPerformanceAnalyzer.
     * @param flights List of Flight objects to be analyzed.
     */
    public FlightPerformanceAnalyzer(List<Flight> flights) {
        this.flights = flights;
    }

    /**
     * Calculates the average on-time rate of flights.
     * The on-time rate is determined by checking flights whose status is not DELAYED.
     * @return The percentage of on-time flights.
     */
    public double calculateOnTimeRate() {
        long onTimeFlights = flights.stream()
                .filter(flight -> flight.getStatus() != FlightStatus.DELAYED) // Check flights that are not delayed
                .count();
        return (double) onTimeFlights / flights.size() * 100;
    }

    /**
     * Analyzes seat occupancy trends for all flights.
     * The seat occupancy rate is calculated as the ratio of passengers to total capacity for each flight.
     * @return A map containing flight numbers as keys and their seat occupancy rates as values.
     */
    public Map<String, Double> analyzeSeatOccupancyTrend() {
        return flights.stream()
                .collect(Collectors.toMap(
                        Flight::getFlightNumber, // Use flight number as the key
                        flight -> {
                            int totalPassengers = flight.getPassengers().size(); // Number of passengers
                            int capacity = flight.getCapacity(); // Total capacity
                            return (double) totalPassengers / capacity * 100; // Calculate seat occupancy rate
                        }
                ));
    }

    /**
     * Calculates the cancellation rate of flights.
     * The cancellation rate is determined by the percentage of flights with a status of CANCELLED.
     * @return The percentage of cancelled flights.
     */
    public double calculateCancellationRate() {
        long cancelledFlights = flights.stream()
                .filter(flight -> flight.getStatus() == FlightStatus.CANCELLED) // Filter flights with CANCELLED status
                .count();
        return (double) cancelledFlights / flights.size() * 100;
    }
}
