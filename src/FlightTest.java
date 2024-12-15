import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {
    private static final LocalDateTime departureTime1 = LocalDateTime.of(2024, 11, 24, 10, 0);
    private static final LocalDateTime arrivalTime1 = LocalDateTime.of(2024, 11, 24, 14, 0);

    private static final LocalDateTime departureTime2 = LocalDateTime.of(2024, 11, 24, 15, 0);
    private static final LocalDateTime arrivalTime2 = LocalDateTime.of(2024, 11, 24, 17, 0);

    @Test
    void conflictsWith_NoConflict() {
        // Create two flights with non-overlapping schedules
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                departureTime1, arrivalTime1, 200,
                new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("CD456", "New York", "San Francisco",
                departureTime2, arrivalTime2, 200,
                new ArrayList<>(), new ArrayList<>());

        assertFalse(flight1.conflictsWith(flight2));
    }

    @Test
    void conflictsWith_CompleteConflict() {
        // Create two flights with completely overlapping schedules
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                departureTime1, arrivalTime1, 200,
                new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("CD456", "New York", "Los Angeles",
                departureTime1, arrivalTime1, 200,
                new ArrayList<>(), new ArrayList<>());

        assertTrue(flight1.conflictsWith(flight2));
    }

    @Test
    void bookSeat_ReservationClosed() {
        // Simulate a flight with reservations closed
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                departureTime1, arrivalTime1, 1,
                new ArrayList<>(), new ArrayList<>());

        flight.setOpenForReservation(false); // Set reservation status to closed
        Passenger passenger = new Passenger("John Doe", new ArrayList<>());

        String result = flight.bookSeat(passenger, "Economy", "Meal");
        assertEquals("Reservation is closed for this flight.", result);
    }

    @Test
    void bookSeat_InvalidSeatType() {
        // Test booking with an invalid seat type
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                departureTime1, arrivalTime1, 200,
                new ArrayList<>(), new ArrayList<>());

        Passenger passenger = new Passenger("John Doe", new ArrayList<>());
        String result = flight.bookSeat(passenger, "Business", "Meal"); // Invalid seat type
        assertEquals("Invalid seat type. Please choose 'FirstClass' or 'Economy'.", result);
    }

    @Test
    void bookSeat_FullEconomy() {
        // Simulate economy class being fully booked
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                departureTime1, arrivalTime1, 1,
                new ArrayList<>(), new ArrayList<>());

        Passenger passenger = new Passenger("John Doe", new ArrayList<>());
        flight.bookSeat(passenger, "Economy", "Meal");
        String result = flight.bookSeat(new Passenger("Jane Smith", new ArrayList<>()), "Economy", "Meal");
        assertEquals("Reservation is closed for this flight.", result);
    }

    @Test
    void bookSeat_VIPDiscount() {
        // Test booking for VIP passengers
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                departureTime1, arrivalTime1, 200,
                new ArrayList<>(), new ArrayList<>());

        Passenger vipPassenger = new Passenger("VIP John", new ArrayList<>()); // Create VIP passenger
        flight.bookSeat(vipPassenger, "FirstClass", "Meal");

        String result = flight.bookSeat(new Passenger("Regular Jane", new ArrayList<>()), "Economy", "Meal");
        vipPassenger.registerVip(flight);
        assertEquals("Seat successfully booked for Regular Jane in Economy.", result);
    }

    @Test
    void update_CancelNonVIP() {
        // Cancel reservation for non-VIP passenger
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                departureTime1, arrivalTime1, 200,
                new ArrayList<>(), new ArrayList<>());

        Passenger nonVIPPassenger = new Passenger("John Doe", new ArrayList<>());
        flight.bookSeat(nonVIPPassenger, "Economy", "Meal");
        flight.update("Economy", nonVIPPassenger); // Cancel reservation and process fees
    }

    @Test
    void modify_ChangeToFirstClass() {
        // Change seat from Economy to First Class
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                departureTime1, arrivalTime1, 200,
                new ArrayList<>(), new ArrayList<>());

        Passenger passenger = new Passenger("John Doe", new ArrayList<>());
        flight.bookSeat(passenger, "Economy", "Meal");
        flight.modify("FirstClass", passenger); // Upgrade to First Class
    }
}
