public interface PassengerNotification {

    /**
     * Sends a notification to all passengers who have booked a specific flight.
     * The notification can be of various types (e.g., general message, delay, cancellation, etc.).
     *
     * @param flightNumber The flight number for which the notification is being sent.
     * @param message      The notification message to be sent to the passengers.
     * @param type         The type of notification (e.g., "Delay", "Cancellation", "Reminder").
     */
    void sendNotification(String flightNumber, String message, String type);
}
