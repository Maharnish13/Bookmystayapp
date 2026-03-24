
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

// ----------- Booking Queue ----------- //
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // removes from queue (FIFO)
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ----------- Inventory Service ----------- //
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceAvailability(String type) {
        inventory.put(type, getAvailability(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// ----------- Booking Service ----------- //
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    // Generate unique room ID
    private String generateRoomId(String roomType, int counter) {
        return roomType.substring(0, 2).toUpperCase() + "-" + counter;
    }

    public void processBookings(BookingQueue queue, RoomInventory inventory) {

        int counter = 1;

        while (!queue.isEmpty()) {

            Reservation req = queue.getNextRequest();
            String type = req.getRoomType();

            System.out.println("\nProcessing request for " + req.getGuestName());

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                String roomId;

                // Ensure unique ID
                do {
                    roomId = generateRoomId(type, counter++);
                } while (allocatedRoomIds.contains(roomId));

                // Store unique ID
                allocatedRoomIds.add(roomId);

                // Map room type to allocated IDs
                roomAllocations
                        .computeIfAbsent(type, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory immediately
                inventory.reduceAvailability(type);

                // Confirm reservation
                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + req.getGuestName());
                System.out.println("Room Type: " + type);
                System.out.println("Allocated Room ID: " + roomId);

            } else {
                System.out.println("Booking Failed! No rooms available for " + type);
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\nRoom Allocations:");
        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " -> " + roomAllocations.get(type));
        }
    }
}

// ----------- Main Application ----------- //
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Room Allocation System =====");

        // Initialize components
        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService();

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Process bookings
        service.processBookings(queue, inventory);

        // Show final state
        service.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nAll bookings processed.");
    }
}