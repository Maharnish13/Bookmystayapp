
import java.util.*;

// ----------- Reservation Class ----------- //
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

// ----------- Booking Queue ----------- //
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\nBooking Requests (FIFO Order):\n");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Get next request (for future processing)
    public Reservation getNextRequest() {
        return queue.peek(); // does not remove
    }
}

// ----------- Main Application ----------- //
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Booking Request Queue =====");

        // Initialize queue
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate guest booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display queue (FIFO order)
        bookingQueue.displayQueue();

        // Peek next request (without removing)
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.getNextRequest();
        if (next != null) {
            next.display();
        }

        System.out.println("\nNo rooms allocated yet (queue only).");
    }
}