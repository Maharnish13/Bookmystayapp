/**
 * Book My Stay App - Use Case 8
 * Booking History & Reporting
 * 
 * Demonstrates:
 * List (ordered storage), historical tracking, reporting separation
 * 
 * @author Maharnish
 * @version 1.0
 */

import java.util.*;

// ----------- Reservation ----------- //
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// ----------- Booking History ----------- //
class BookingHistory {

    // Ordered storage
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation r) {
        history.add(r);
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }

    // Display history
    public void displayHistory() {
        System.out.println("\nBooking History:\n");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : history) {
            r.display();
        }
    }
}

// ----------- Reporting Service ----------- //
class BookingReportService {

    // Generate summary report
    public void generateReport(List<Reservation> reservations) {

        System.out.println("\nBooking Report Summary:\n");

        if (reservations.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        Map<String, Integer> countByRoomType = new HashMap<>();

        // Count bookings per room type
        for (Reservation r : reservations) {
            countByRoomType.put(
                r.getRoomType(),
                countByRoomType.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        // Display report
        for (String type : countByRoomType.keySet()) {
            System.out.println(type + " Bookings: " + countByRoomType.get(type));
        }

        System.out.println("Total Bookings: " + reservations.size());
    }
}

// ----------- Main Application ----------- //
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Booking History & Reporting =====");

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings (from UC6)
        history.addReservation(new Reservation("SR-1", "Alice", "Single Room"));
        history.addReservation(new Reservation("DR-1", "Bob", "Double Room"));
        history.addReservation(new Reservation("SR-2", "Charlie", "Single Room"));

        // Admin views booking history
        history.displayHistory();

        // Admin generates report
        reportService.generateReport(history.getAllReservations());

        System.out.println("\nReport generated successfully (read-only).");
    }
}