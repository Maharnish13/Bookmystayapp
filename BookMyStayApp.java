
import java.io.*;
import java.util.*;

// ----------- Reservation ----------- //
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public String toString() {
        return reservationId + " -> " + roomType;
    }
}

// ----------- Inventory ----------- //
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " : " + inventory.get(key));
        }
    }
}

// ----------- Booking History ----------- //
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> history = new ArrayList<>();

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return history;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

// ----------- Persistence Service ----------- //
class PersistenceService {

    private static final String FILE_NAME = "hotel_data.ser";

    // Save state
    public static void save(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(inventory);
            out.writeObject(history);

            System.out.println("\nData saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load state
    public static Object[] load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            RoomInventory inventory = (RoomInventory) in.readObject();
            BookingHistory history = (BookingHistory) in.readObject();

            System.out.println("\nData loaded successfully.");

            return new Object[]{inventory, history};

        } catch (FileNotFoundException e) {
            System.out.println("No saved data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading data. Starting with safe defaults.");
        }

        return null;
    }
}

// ----------- Main Application ----------- //
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Persistence & Recovery =====");

        RoomInventory inventory;
        BookingHistory history;

        // Attempt recovery
        Object[] data = PersistenceService.load();

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (BookingHistory) data[1];
        } else {
            // Fresh start
            inventory = new RoomInventory();
            history = new BookingHistory();

            history.add(new Reservation("SR-1", "Single Room"));
        }

        // Display current state
        inventory.display();
        history.display();

        // Simulate new booking
        history.add(new Reservation("DR-1", "Double Room"));

        // Save before shutdown
        PersistenceService.save(inventory, history);

        System.out.println("\nSystem ready for restart with saved state.");
    }
}