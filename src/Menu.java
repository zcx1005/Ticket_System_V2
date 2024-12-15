import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Menu {

    private AirlineCompany airlineCompany;
    private Scanner scanner;

    public Menu(AirlineCompany airlineCompany) {
        this.airlineCompany = airlineCompany;
        this.scanner = new Scanner(System.in);
    }

    public void display() {
        boolean exit = false;

        while (!exit) {
            // Print menu
            printMenu();

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    viewAllFlights();
                    break;
                case 2:
                    viewFlightDetails();
                    break;
                case 3:
                    createPassengerAndBookSeat();
                    break;
                case 4:
                    modifyReservation();
                    break;
                case 5:
                    cancelReservation();
                    break;
                case 6:
                    setFlightDelay();
                    break;
                case 7:
                    viewPopularRoutes();
                    break;
                case 8:
                    viewNearlyFullFlights();
                    break;
                case 9:
                    viewPassengerReservations();
                    break;
                case 10:
                    viewAllPassengersForFlight();
                    break;
                case 11:
                    exit = true;
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n=== Airline Booking System ===");
        System.out.println("1. View all flights");
        System.out.println("2. View flight details");
        System.out.println("3. Create a passenger and book a seat");
        System.out.println("4. Modify a reservation");
        System.out.println("5. Cancel a reservation");
        System.out.println("6. Set flight delay");
        System.out.println("7. View popular routes");
        System.out.println("8. View nearly full flights");
        System.out.println("9. View passenger reservations");
        System.out.println("10. View all passengers for a flight");
        System.out.println("11. Exit");
        System.out.print("Choose an option: ");
    }

    private int getUserChoice() {
        return scanner.nextInt();
    }

    // Add other methods for each functionality below:

    private void viewAllFlights() {
        List<Flight> allFlights = airlineCompany.getAllFlights();
        if (allFlights.isEmpty()) {
            System.out.println("No flights available.");
        } else {
            System.out.println("All flights in the airline:");
            for (Flight flight : allFlights) {
                System.out.println(flight.toString());
            }
        }
    }

    private void viewFlightDetails() {
        System.out.println("Enter flight number: ");
        scanner.nextLine();  // Consume the newline
        String flightNumber = scanner.nextLine();
        Flight flight = airlineCompany.getFlightDetails(flightNumber);
        if (flight != null) {
            System.out.println(flight.toString());
        } else {
            System.out.println("Flight not found.");
        }
    }

    private void createPassengerAndBookSeat() {
        System.out.println("Enter passenger name: ");
        scanner.nextLine();  // Consume the newline
        String passengerName = scanner.nextLine();

        Passenger passenger = new Passenger(passengerName, new ArrayList<>());
        System.out.println("Enter flight number to book: ");
        String flightNumber = scanner.nextLine();

        Flight flight = airlineCompany.getFlightDetails(flightNumber);
        if (flight != null) {
            System.out.println("Enter seat type (FirstClass/Economy): ");
            String seatType = scanner.nextLine();
            System.out.println("Enter service type: ");
            String serviceType = scanner.nextLine();
            System.out.println(flight.bookSeat(passenger, seatType, serviceType));
        } else {
            System.out.println("Flight not found.");
        }
    }

    private void modifyReservation() {

    }

    private void cancelReservation() {

    }

    private void setFlightDelay() {
        System.out.println("Enter flight number to delay: ");
        scanner.nextLine();  // Consume the newline
        String flightNumber = scanner.nextLine();
        Flight flight = airlineCompany.getFlightDetails(flightNumber);
        if (flight != null) {
            System.out.println("Enter new departure time (yyyy-MM-dd HH:mm): ");
            String newDepartureTime = scanner.nextLine();
            System.out.println("Enter new arrival time (yyyy-MM-dd HH:mm): ");
            String newArrivalTime = scanner.nextLine();
            airlineCompany.delayFlight(flightNumber, LocalDateTime.parse(newDepartureTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), LocalDateTime.parse(newArrivalTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.println("Flight delay updated.");
        } else {
            System.out.println("Flight not found.");
        }
    }

    private void viewPopularRoutes() {
        List<String> popularRoutes = airlineCompany.getPopularRoutes();
        if (popularRoutes.isEmpty()) {
            System.out.println("No popular routes available.");
        } else {
            System.out.println("Popular routes:");
            for (String route : popularRoutes) {
                System.out.println(route);
            }
        }
    }

    private void viewNearlyFullFlights() {
        List<Flight> nearlyFullFlights = airlineCompany.getNearlyFullFlights();
        if (nearlyFullFlights.isEmpty()) {
            System.out.println("No nearly full flights available.");
        } else {
            System.out.println("Nearly full flights:");
            for (Flight flight : nearlyFullFlights) {
                System.out.println(flight.toString());
            }
        }
    }

    private void viewPassengerReservations() {
        System.out.println("Enter passenger name: ");
        scanner.nextLine();  // Consume the newline
        String passengerName = scanner.nextLine();
        // Logic for viewing passenger reservations
    }

    private void viewAllPassengersForFlight() {
        System.out.println("Enter flight number to view passengers: ");
        scanner.nextLine();  // Consume the newline
        String flightNumber = scanner.nextLine();
        Flight flight = airlineCompany.getFlightDetails(flightNumber);
        if (flight != null) {
            System.out.println("List of passengers for Flight " + flight.getFlightNumber() + ":");
            List<Passenger> flightPassengers = flight.getPassengers();
            if (flightPassengers.isEmpty()) {
                System.out.println("No passengers have booked this flight.");
            } else {
                for (Passenger passenger : flightPassengers) {
                    for (Reservation reservation : passenger.getReservations()) {
                        if (reservation.getMyFlight().equals(flight)) {
                            System.out.println("Passenger: " + passenger.getName() +
                                    " | Seat Type: " + reservation.getMySeatType() +
                                    " | Service: " + reservation.getMyService());
                        }
                    }
                }
            }
        } else {
            System.out.println("Flight not found.");
        }
    }
}
