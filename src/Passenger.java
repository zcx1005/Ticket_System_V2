import java.util.List;

/**
 * The Passenger class represents a passenger who can make and manage flight reservations.
 * A passenger can have multiple reservations for different flights. This class allows for
 * operations such as adding, modifying, canceling reservations, checking for flight conflicts,
 * and registering as a VIP on a flight. Each passenger has a name and a list of reservations
 * associated with them.
 *
 * Key features:
 * - Add a reservation for a flight.
 * - Check if a conflict exists between a flight and existing reservations.
 * - Cancel or modify a reservation.
 * - Register the passenger as a VIP on a flight.
 * - Provides a string representation of the passenger's details and their reservations.
 *
 * This class relies on the Reservation and Flight classes to function. A reservation holds details
 * about a specific flight, seat type, and service type, while the Flight class represents flight
 * details and the availability of seats.
 */

public class Passenger {
    private String name;  // The name of the passenger
    List<Reservation> reservations;  // A list of reservations made by the passenger

    /**
     * Constructor to initialize a Passenger with a name and a list of reservations.
     * @param name The name of the passenger.
     * @param reservations A list of reservations for the passenger.
     */
    public Passenger(String name, List<Reservation> reservations) {
        this.name = name;
        this.reservations = reservations;
    }

    //Getter
    public String getName() {
        return name;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    //Setter
    public void setReservations(Flight flight, String sType, String ser) {
        Reservation res = new Reservation(flight, sType, ser);
        reservations.add(res);  // Add the new reservation to the list
    }

    /**
     * Checks if there is a conflict between the current flight and the passenger's existing reservations.
     * A conflict occurs if the passenger has already reserved a seat on a flight that overlaps with the current flight.
     * @param flight The flight to check for conflicts with.
     * @return True if there is no conflict with any existing reservation; false if a conflict is found.
     */
    public boolean isConflict(Flight flight) {
        for (Reservation res : reservations) {
            if (res.getMyFlight().conflictsWith(flight)) {
                return false;  // A conflict is found, return false
            }
        }
        return true;  // No conflict
    }

    /**
     * Cancels a reservation for a specific flight.
     * This method will remove the reservation from the passenger's list and update the flight accordingly.
     * @param flight The flight whose reservation is to be canceled.
     */
    public void cancelReservation(Flight flight) {
        for (Reservation res : reservations) {
            if (res.getMyFlight().equals(flight)) {
                reservations.remove(res);  // Remove the reservation from the list
                System.out.println("Flight: " + flight.getFlightNumber()
                        + " canceled successfully.");
                flight.update(res.getMySeatType(), this);  // Update the flight with the canceled reservation
                return;
            }
        }
        System.out.println("Flight " + flight.getFlightNumber()
                + " not found in reservations.");
    }

    /**
     * Modifies an existing reservation, changing the seat type and/or service.
     * If the new seat type is available on the flight, the modification is successful.
     * @param curFlight The current flight to modify.
     * @param seatType The new seat type (e.g., "FirstClass", "Economy").
     * @param service The new service type (e.g., "Meal", "Extra Luggage").
     */
    public void modifyReservation(Flight curFlight, String seatType, String service) {
        // Check if the seat type is valid
        if (!seatType.equalsIgnoreCase("FirstClass") && !seatType.equalsIgnoreCase("Economy")) {
            System.out.println("Invalid seat type. Please choose 'FirstClass' or 'Economy'.");
            return;
        }

        // Iterate through the reservations to find the current flight
        for (Reservation res : reservations) {
            if (res.getMyFlight().equals(curFlight)) {
                // If the seat type is unchanged, exit
                if (res.getMySeatType().equals(seatType)) {
                    System.out.println("You have already booked this type of seat.");
                    return;
                }

                // Check if the new seat type is available on the flight
                if ((seatType.equalsIgnoreCase("FirstClass") && curFlight.getFirstClassCapacity() > 0) ||
                        (seatType.equalsIgnoreCase("Economy") && curFlight.getEconomyClassCapacity() > 0)) {

                    // Modify the flight and reservation details
                    curFlight.modify(seatType, this);  // Modify the flight's seat availability
                    res.setMySeatType(seatType);  // Update the reservation's seat type
                    res.setMyService(service);  // Update the reservation's service

                    System.out.println("You have successfully modified your reservation.");
                    return;
                } else {
                    System.out.println("There are not enough available seat types to make the modification.");
                    return;
                }
            }
        }

        // If no reservation was found for the specified flight
        System.out.println("Flight " + curFlight.getFlightNumber() + " not found in reservations.");
    }

    /**
     * Registers the passenger as a VIP for a specific flight.
     * Adds the passenger to the flight's VIP list.
     * @param flight The flight for which the passenger is being registered as a VIP.
     */
    public void registerVip(Flight flight) {
        flight.getVip().add(this);  // Add the passenger to the flight's VIP list
    }

    /**
     * Returns a string representation of the passenger's name and their reservations.
     * @return A string representing the passenger's name and their reservations.
     */
    @Override
    public String toString() {
        return name + " " + reservations;  // Return the name and reservations as a string
    }
}
