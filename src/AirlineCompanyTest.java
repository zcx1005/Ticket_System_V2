import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AirlineCompanyTest {

    @Test
    void addFlight() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Normal case: Adding a valid flight
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-11 10:00", formatter), LocalDateTime.parse("2024-11-11 15:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        assertTrue(company.addFlight(flight1));

        // Edge case: Invalid flight number
        Flight flight2 = new Flight("", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-11 10:00", formatter), LocalDateTime.parse("2024-11-11 15:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> company.addFlight(flight2));
    }

    @Test
    void cancelFlight() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Add a flight
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-11 10:00", formatter), LocalDateTime.parse("2024-11-11 15:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());

        // Normal case: Cancel an existing flight
        company.addFlight(flight1);
        company.cancelFlight("AB123");
        assertNull(company.getFlightDetails("AB123"));

        // Edge case: Cancel a non-existent flight
        assertDoesNotThrow(() -> company.cancelFlight("CD456"));

        // Error case: Empty flight number
        assertThrows(IllegalArgumentException.class, () -> company.cancelFlight(""));
    }

    @Test
    void delayFlight() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Add a flight
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        company.addFlight(flight1);

        // Normal case: Delay the flight
        company.delayFlight("AB123", LocalDateTime.parse("2024-11-24 12:00", formatter), LocalDateTime.parse("2024-11-24 16:00", formatter));
        assertEquals(LocalDateTime.parse("2024-11-24 12:00", formatter), company.getFlightDetails("AB123").getDepartureTime());

        // Edge case: Delay time earlier than original time
        assertThrows(IllegalArgumentException.class, () -> company.delayFlight("AB123",
                LocalDateTime.parse("2024-11-24 08:00", formatter), LocalDateTime.parse("2024-11-24 12:00", formatter)));

        // Error case: Non-existent flight
        assertThrows(IllegalArgumentException.class, () -> company.delayFlight("CD456",
                LocalDateTime.parse("2024-11-24 12:00", formatter), LocalDateTime.parse("2024-11-24 16:00", formatter)));
    }

    @Test
    void getFlightDetails() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Add a flight
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        company.addFlight(flight1);

        // Normal case: Retrieve details of an existing flight
        assertNotNull(company.getFlightDetails("AB123"));

        // Error case: Non-existent flight number
        assertNull(company.getFlightDetails("CD456"));
    }

    @Test
    void getAllFlights() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Add multiple flights
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("CD456", "Beijing", "Shanghai",
                LocalDateTime.parse("2024-11-25 08:00", formatter), LocalDateTime.parse("2024-11-25 11:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        company.addFlight(flight1);
        company.addFlight(flight2);

        // Verify total flights
        assertEquals(2, company.getAllFlights().size());
    }

    @Test
    void getPopularRoutes() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Add flights for the same route
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("CD456", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        Flight flight3 = new Flight("EF789", "China", "Japan",
                LocalDateTime.parse("2024-11-26 11:30", formatter), LocalDateTime.parse("2024-11-26 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        company.addFlight(flight1);
        company.addFlight(flight2);
        company.addFlight(flight3);

        // Verify popular routes
        assertEquals("New York - Los Angeles (2 flights)", company.getPopularRoutes().getFirst());
    }

    @Test
    void getNearlyFullFlights() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Add a flight
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                2, new ArrayList<>(), new ArrayList<>());

        // Add passengers
        Passenger passenger1 = new Passenger("John Doe", new ArrayList<>());
        Passenger passenger2 = new Passenger("Ben Machiel", new ArrayList<>());
        flight1.bookSeat(passenger1, "FirstClass", "");
        flight1.bookSeat(passenger2, "SecondClass", "");
        company.addFlight(flight1);

        // Verify nearly full flights
        assertEquals(1, company.getNearlyFullFlights().size());
        assertEquals("AB123", company.getNearlyFullFlights().getFirst().getFlightNumber());
    }
}
