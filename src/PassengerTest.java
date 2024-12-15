import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {
    private static final LocalDateTime departureTime1 = LocalDateTime.of(2024, 11, 24, 10, 0);
    private static final LocalDateTime arrivalTime1 = LocalDateTime.of(2024, 11, 24, 14, 0);

    private static final LocalDateTime departureTime2 = LocalDateTime.of(2024, 11, 24, 12, 0);
    private static final LocalDateTime arrivalTime2 = LocalDateTime.of(2024, 11, 24, 15, 0);

    private static final LocalDateTime departureTime3 = LocalDateTime.of(2024, 11, 24, 12, 0);
    private static final LocalDateTime arrivalTime3 = LocalDateTime.of(2024, 11, 24, 15, 0);

    @Test
    void isConflict() {
        // Prepare test data
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                departureTime1,
                arrivalTime1,
                200, new ArrayList<>(), new ArrayList<>());

        Flight flight2 = new Flight("CD456", "New York", "Chicago",
                departureTime2,
                arrivalTime2,
                200, new ArrayList<>(), new ArrayList<>());

        Reservation reservation = new Reservation(flight1, "Economy", "Meal");
        Passenger passenger = new Passenger("John Doe", new ArrayList<>(List.of(reservation)));

        // Test conflicting flights
        assertFalse(passenger.isConflict(flight2), "Flights should conflict due to overlapping times.");

        // Test non-conflicting flights
        Flight flight3 = new Flight("EF789", "Boston", "Miami",
                departureTime3,
                arrivalTime3,
                200, new ArrayList<>(), new ArrayList<>());

        assertTrue(passenger.isConflict(flight3), "Flights should not conflict.");
    }

    @Test
    void cancelReservation() {
        // Prepare test data
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                departureTime1,
                arrivalTime1,
                200, new ArrayList<>(), new ArrayList<>());

        Reservation reservation = new Reservation(flight, "Economy", "Meal");
        Passenger passenger = new Passenger("John Doe", new ArrayList<>(List.of(reservation)));

        // Test successful reservation cancellation
        passenger.cancelReservation(flight);
        assertTrue(passenger.getReservations().isEmpty(),
                "Reservation list should be empty after cancellation.");

        // Test canceling a non-existent reservation
        passenger.cancelReservation(flight);
        assertTrue(passenger.getReservations().isEmpty(),
                "Canceling a non-existent reservation should not affect the list.");
    }

    @Test
    void modifyReservation() {
        // Prepare test data
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                departureTime1,
                arrivalTime1,
                2, new ArrayList<>(), new ArrayList<>()); // Only one FirstClass seat available


        Reservation reservation = new Reservation(flight, "Economy", "Meal");
        Passenger passenger = new Passenger("John Doe", new ArrayList<>(List.of(reservation)));

        // Test successful seat type modification
        passenger.modifyReservation(flight, "FirstClass", "Extra Meal");
        assertEquals("FirstClass", passenger.getReservations().get(0).getMySeatType(),
                "Seat type should be updated to FirstClass.");
        assertEquals("Extra Meal", passenger.getReservations().get(0).getMyService(),
                "Service should be updated to Extra Meal.");

        Passenger passenger2 = new Passenger("John Doe", new ArrayList<>(List.of(reservation)));
        flight.bookSeat(passenger2, "Economy", "Meal");

        // Test seat modification failure (insufficient seats)
        passenger.modifyReservation(flight, "FirstClass", "Standard Meal");
        assertEquals("FirstClass", passenger.getReservations().get(0).getMySeatType(),
                "Seat type should remain unchanged when modification fails.");
    }

    @Test
    void registerVip() {
        // Prepare test data
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                departureTime1,
                arrivalTime1,
                200, new ArrayList<>(), new ArrayList<>());

        Passenger passenger = new Passenger("John Doe", new ArrayList<>());

        // Test VIP registration
        passenger.registerVip(flight);
        assertTrue(flight.getVip().contains(passenger), "Passenger should be added to the flight's VIP list.");
    }

}
