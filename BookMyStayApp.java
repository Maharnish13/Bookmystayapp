
import java.util.*;

// ----------- Room Domain Model ----------- //
abstract class Room {
    protected String type;
    protected int beds;
    protected double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }

    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2000);
    }

    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }

    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

// ----------- Inventory (Read-Only Access) ----------- //
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: unavailable
    }

    // Read-only method
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Set<String> getAllRoomTypes() {
        return inventory.keySet();
    }
}

// ----------- Search Service ----------- //
class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, List<Room> rooms) {

        System.out.println("\nAvailable Rooms:\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getType());

            // Filter unavailable rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available + "\n");
            }
        }
    }
}

// ----------- Main Application ----------- //
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Room Search =====");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects (domain model)
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Perform search (read-only)
        SearchService searchService = new SearchService();
        searchService.searchAvailableRooms(inventory, rooms);

        System.out.println("\nSearch completed. No data modified.");
    }
}