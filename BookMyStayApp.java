/**
 * Book My Stay App - Use Case 11
 * Concurrent Booking Simulation with Thread Safety
 * 
 * Demonstrates:
 * Multi-threading, synchronization, race condition prevention
 * 
 * @author Maharnish
 * @version 1.0
 */

import java.util.*;

// ----------- Reservation ----------- //
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
}

// ----------- Shared Booking Queue ----------- //
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("Request added: " + r.getGuestName());
    }

    public synchronized Reservation getRequest() {
        return queue.poll(); // thread-safe dequeue
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ----------- Thread-Safe Inventory ----------- //
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    // Critical section
    public synchronized boolean bookRoom(String type) {

        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// ----------- Booking Processor (Thread) ----------- //
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation req;

            // synchronized access to queue
            synchronized (queue) {
                if (queue.isEmpty()) break;
                req = queue.getRequest();
            }

            if (req == null) continue;

            // Critical section: booking
            boolean success = inventory.bookRoom(req.getRoomType());

            if (success) {
                System.out.println(getName() + " booked for " + req.getGuestName()
                        + " (" + req.getRoomType() + ")");
            } else {
                System.out.println(getName() + " FAILED for " + req.getGuestName()
                        + " (" + req.getRoomType() + ")");
            }
        }
    }
}

// ----------- Main Application ----------- //
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Concurrent Booking Simulation =====");

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Double Room"));

        // Create multiple threads (simulating multiple users)
        BookingProcessor t1 = new BookingProcessor(queue, inventory, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(queue, inventory, "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.displayInventory();

        System.out.println("\nAll bookings processed safely.");
    }
}