/**
 * Book My Stay App - Use Case 9
 * Error Handling & Validation using Custom Exceptions
 * 
 * Demonstrates:
 * Input Validation, Fail-Fast, Custom Exceptions, Safe State Handling
 * 
 * @author Maharnish
 * @version 1.0
 */

import java.util.*;

// ----------- Custom Exceptions ----------- //
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class InsufficientRoomsException extends Exception {
    public InsufficientRoomsException(String message) {
        super(message);
    }
}

// ----------- Inventory ----------- //
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    public void validateRoomType(String roomType) throws InvalidRoomTypeException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidRoomTypeException("Invalid room type: " + roomType);
        }
    }

    public void validateAvailability(String roomType) throws InsufficientRoomsException {
        if (inventory.get(roomType) <= 0) {
            throw new InsufficientRoomsException("No rooms available for: " + roomType);
        }
    }

    public void bookRoom(String roomType)
            throws InvalidRoomTypeException, InsufficientRoomsException {

        // Fail-fast validation
        validateRoomType(roomType);
        validateAvailability(roomType);

        // Safe state update
        inventory.put(roomType, inventory.get(roomType) - 1);

        System.out.println("Room booked successfully: " + roomType);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// ----------- Main Application ----------- //
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Validation & Error Handling =====");

        RoomInventory inventory = new RoomInventory();

        // Test cases (valid + invalid)
        String[] testRequests = {
                "Single Room",     // valid
                "Suite Room",      // no availability
                "Luxury Room"      // invalid type
        };

        for (String request : testRequests) {
            System.out.println("\nProcessing request: " + request);

            try {
                inventory.bookRoom(request);
            } catch (InvalidRoomTypeException | InsufficientRoomsException e) {
                // Graceful failure
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Final state
        inventory.displayInventory();

        System.out.println("\nSystem continues running safely.");
    }
}