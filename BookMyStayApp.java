
import java.util.*;

// ----------- Add-On Service ----------- //
class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}

// ----------- Add-On Service Manager ----------- //
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap
            .computeIfAbsent(reservationId, k -> new ArrayList<>())
            .add(service);

        System.out.println(service.getName() + " added to " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        System.out.println("\nServices for Reservation " + reservationId + ":");

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s.getName() + " : ₹" + s.getPrice());
        }
    }

    // Calculate total cost
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.getPrice();
        }
        return total;
    }
}

// ----------- Main Application ----------- //
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Add-On Services =====");

        AddOnServiceManager manager = new AddOnServiceManager();

        // Example reservation IDs (from previous use case)
        String res1 = "SR-1";
        String res2 = "DR-1";

        // Create services
        AddOnService breakfast = new AddOnService("Breakfast", 200);
        AddOnService wifi = new AddOnService("WiFi", 100);
        AddOnService spa = new AddOnService("Spa", 500);

        // Add services to reservations
        manager.addService(res1, breakfast);
        manager.addService(res1, wifi);
        manager.addService(res2, spa);

        // Display services
        manager.displayServices(res1);
        System.out.println("Total Add-On Cost: ₹" + manager.calculateTotalCost(res1));

        manager.displayServices(res2);
        System.out.println("Total Add-On Cost: ₹" + manager.calculateTotalCost(res2));

        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}