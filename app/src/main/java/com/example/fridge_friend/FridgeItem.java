public class FridgeItem {
    private String id;
    private String name;
    private String details;

    // Constructor corrected to properly initialize the details
    public FridgeItem(String id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details; // Now correctly assigning the details
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDetails() { return details; }
}
