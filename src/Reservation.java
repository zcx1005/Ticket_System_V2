public class Reservation {
    /*   private String reservationID;  Reservation information is associated with multiple records for a user,
        and these records are stored in a list. When accessed, you can use the index of the list directly,
        so this reservation ID is unnecessary. */
    /*    private Passenger me;  Same issue as above, unnecessary. */
    private Flight myFlight;
    private String mySeatType;
    private String myService;

    // Constructor: Initializes the reservation with the flight, seat type, and service
    public Reservation(Flight flight, String seatType, String service) {
        this.myFlight = flight;
        this.myService = service;
        this.mySeatType = seatType;
    }

    //Getter
    public Flight getMyFlight() {
        return myFlight;
    }

    public String getMySeatType() {
        return mySeatType;
    }


    public String getService() {
        return myService;
    }


    public String getMyService() {
        return myService;
    }

    //Setter
    public void setMySeatType(String newSeatType) {
        mySeatType = newSeatType;
    }


    public void setMyService(String newService) {
        myService = newService;
    }


    @Override
    public String toString() {
        return "Flight: " + myFlight.getFlightNumber() + " | " +
                "Seat Type: " + mySeatType + " | " +
                "Service: " + myService;
    }
}
