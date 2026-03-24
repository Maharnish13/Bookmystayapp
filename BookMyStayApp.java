/**
 * Book My Stay App - Use Case 10
 * Booking Cancellation & Inventory Rollback
 * 
 * Demonstrates:
 * Stack (LIFO), rollback logic, validation, safe state restoration
 * 
 * @author Maharnish
 * @version 1.0
 */

import java.util.*;

// ----------- Reservation ----------- //
class Reservation {
    private String reservationId;
    private String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ----------- Inventory ----------- //
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    public void increaseAvailability(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// ----------- Booking History ----------- //
class BookingHistory {
    private Map<String, Reservation> confirmedBookings = new HashMap<>();

    public void addReservation(Reservation r) {
        confirmedBookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return confirmedBookings.get(id);
    }

    public void removeReservation(String id) {
        confirmedBookings.remove(id);
    }

    public void displayHistory() {
        System.out.println("\nActive Bookings:");
        for (Reservation r : confirmedBookings.values()) {
            System.out.println(r.getReservationId() + " -> " + r.getRoomType());
        }
    }
}

// ----------- Cancellation Service ----------- //
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              RoomInventory inventory) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Validate reservation exists
        Reservation res = history.getReservation(reservationId);

        if (res == null) {
            System.out.println("Error: Reservation not found or already cancelled.");
            return;
        }

        // Push to rollback stack (LIFO)
        rollbackStack.push(reservationId);

        // Restore inventory
        inventory.increaseAvailability(res.getRoomType());

        // Remove booking from history
        history.removeReservation(reservationId);

        // Confirmation
        System.out.println("Cancellation successful for " + reservationId);
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recent Cancellations):");
        System.out.println(rollbackStack);
    }
}

// ----------- Main Application ----------- //
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Cancellation & Rollback =====");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("SR-1", "Single Room"));
        history.addReservation(new Reservation("DR-1", "Double Room"));

        history.displayHistory();
        inventory.displayInventory();

        // Perform cancellations
        cancelService.cancelBooking("SR-1", history, inventory);
        cancelService.cancelBooking("XX-1", history, inventory); // invalid case

        // Final state
        history.displayHistory();
        inventory.displayInventory();
        cancelService.displayRollbackStack();

        System.out.println("\nSystem state restored safely.");
    }
}