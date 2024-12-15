import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Flight class of AirlinBookingSystem used to represent flight information and implement related operations.
 *
 * Information includes flight number, departure, destination, departure time, arrival time, delay or not, capacity, first Class capacity, Economy class capacity, passenger list, whether reservations are open, VIP passenger list
 @author: CPT403-Group 9
 */

public class Flight {
    private String flightNumber;
    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private boolean isDelay;
    private int capacity; // 总容量
    private int firstClassCapacity; // 头等舱容量
    private int economyClassCapacity; // 经济舱容量
    private List<Passenger> passengers;
    private boolean isOpenForReservation;
    private List<Passenger> vip;
    private FlightStatus status;

    // 构造方法
    public Flight(String flightNumber, String departure, String destination,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, int capacity,
                  List<Passenger> passengers, List<Passenger> vip) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.isDelay = false;
        this.capacity = capacity;
        this.firstClassCapacity = (int) (capacity * 0.1); // // First class capacity is 10% of the total capacity
        this.economyClassCapacity = capacity - firstClassCapacity; // remaining capacity is economy class
        this.isOpenForReservation = true;
        this.passengers = passengers;
        this.vip = vip;
        checkReservationStatus(); // Check if reservation should be closed
        this.status = FlightStatus.SCHEDULED;
    }

    // Getters
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFirstClassCapacity() {
        return firstClassCapacity;
    }


    public int getEconomyClassCapacity() {
        return economyClassCapacity;
    }


    public List<Passenger> getPassengers() {
        return passengers;
    }

    public List<Passenger> getVip() {
        return vip;
    }

    // Setters
    public void setOpenForReservation(boolean openForReservation) {
        this.isOpenForReservation = openForReservation;
    }

    // Method to check if the reservations should be closed
    private void checkReservationStatus() {
        if (passengers.size() >= capacity) {
            this.isOpenForReservation = false; // Close reservations if capacity is full
        }
    }

    // Setter for status
    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public void delay(LocalDateTime newDepartureTime, LocalDateTime newArrivalTime) {
        if (newDepartureTime.isAfter(this.getDepartureTime())
                && newArrivalTime.isAfter(this.getArrivalTime())) {
            this.departureTime = newDepartureTime;
            this.arrivalTime = newArrivalTime;
            this.isDelay = true;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            System.out.println("Flight " + flightNumber + " has been delayed. New departure time: "
                    + newDepartureTime.format(formatter) + ", new arrival time: " + newArrivalTime.format(formatter));
        } else {
            throw new IllegalArgumentException("New departure and arrival times must be later than the original times.");
        }
    }

    public void closeForReservation() {
        this.isOpenForReservation = false;
    }

    /**
     * Returns a string representation of the flight.
     */
    @Override
    public String toString() {
        // define the date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return flightNumber + " | " +
                departure + " -> " + destination + " | " +
                departureTime.format(formatter) + " ~ " + arrivalTime.format(formatter) + " | " +
                "Delay: " + (isDelay ? "Yes" : "No") + " | " +
                "Capacity: " + capacity + " | " +
                "First: " + getFirstClassCapacity() + " · " + "Economy: " + getEconomyClassCapacity() + " | " +
                "Passengers: " + (passengers.isEmpty() ? 0 : passengers.size()) + " | " +
                "Open for Reservation: " + (isOpenForReservation ? "Yes" : "No");
    }

    /**
     * Determine whether flights conflict
     * @param other - the flight to compare with this flight
     * @return
     */
    public boolean conflictsWith(Flight other) {
        return !(this.arrivalTime.isBefore(other.getDepartureTime()) ||
                this.departureTime.isAfter(other.getArrivalTime()));
    }

    public String bookSeat(Passenger passenger, String seatType, String service) {
        // Check if the flight is open for booking
        if (!isOpenForReservation) {
            return "Reservation is closed for this flight.";
        }

        // Check if the seat type is valid
        if (!seatType.equalsIgnoreCase("FirstClass") && !seatType.equalsIgnoreCase("Economy")) {
            return "Invalid seat type. Please choose 'FirstClass' or 'Economy'.";
        }

        // Check if the passenger has any conflicts with other flights
        if (!passenger.isConflict(this)) {
            return "Conflict detected: Cannot book flight " + flightNumber;
        }

        // check and adjust capacity according to seat type
        if (!reduceSeatCapacity(seatType)) {
            return "No remaining seats in " + seatType + ".";
        }

        // Add passengers to the list
        passengers.add(passenger);

        // Update the passenger's own reservation list
        passenger.setReservations(this, seatType, service);

        // Check if it is necessary to close the reservation
        checkReservationStatus();

        //Return result message
        String resultMessage = "Seat successfully booked for " + passenger.getName() + " in " + seatType + ".";
        if (vip.contains(passenger)) {
            resultMessage += " You can enjoy a 15% discount on the ticket price.";
        }
        return resultMessage;
    }

    /**
     * Reduces the seat capacity for the specified seat type by one if seats are available.
     *
     * @param seatType The type of seat to reduce capacity for (e.g., "FirstClass", "Economy").
     * @return true if the seat capacity was successfully reduced, false if no seats are available
     *         for the specified type or the type is invalid.
     */
    private boolean reduceSeatCapacity(String seatType) {
        if (seatType.equalsIgnoreCase("FirstClass")) {
            if (firstClassCapacity > 0) {
                firstClassCapacity--;
                return true;
            }
        } else if (seatType.equalsIgnoreCase("Economy")) {
            if (economyClassCapacity > 0) {
                economyClassCapacity--;
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the flight's seat availability and processes cancellation fees when a passenger cancels their booking.
     * VIP passengers are exempted from the cancellation fee.
     *
     * @param seatType  The type of seat being canceled (e.g., "FirstClass", "Economy").
     * @param passenger The passenger canceling their booking.
     */
    public void update(String seatType, Passenger passenger) {
        if (vip.contains(passenger)) {
            System.out.println("You are a VIP, so we will waive the service fee for you this time");
        }
        if (seatType.equalsIgnoreCase("FirstClass")) {
            firstClassCapacity++;
            System.out.println("You need to pay an additional 15% of the first-class ticket price as a handling fee");
        } else {
            economyClassCapacity++;
            System.out.println("You need to pay an additional 10% of the economy-class ticket price as a handling fee");
        }
        // Remove the passenger from the passenger list
        if (passengers.contains(passenger)) {
            passengers.remove(passenger);
            System.out.println("Passenger " + passenger.getName() + " has been removed from the flight.");
        } else {
            System.out.println("Passenger not found in the flight.");
        }
    }

    /**
     * Modifies a passenger's seat type booking and adjusts the seat capacity accordingly.
     * VIP passengers are exempt from the service fee.
     *
     * @param newSeatType The new seat type the passenger wishes to switch to (e.g., "FirstClass", "Economy").
     * @param passenger   The passenger requesting the modification.
     */
    public void modify(String newSeatType, Passenger passenger) {
        if (newSeatType.equalsIgnoreCase("FirstClass")) {
            economyClassCapacity++;
            firstClassCapacity--;
            System.out.println("Please pay the upgrade fee.");
        } else {
            firstClassCapacity++;
            economyClassCapacity--;
            System.out.println("The fare difference will be refunded to your account.");
        }
        if (vip.contains(passenger)) {
            System.out.println("You are a VIP, so we will waive the service fee for you this time");
        } else {
            System.out.println("You need to pay a 5% service fee on the ticket price.");
        }

    }

}
