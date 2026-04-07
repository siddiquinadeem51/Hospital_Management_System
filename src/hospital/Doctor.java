package hospital;

public class Doctor {
    private final String id;
    private final String name;
    private final String department;
    private final String availability;

    public Doctor(String id, String name, String department, String availability) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.availability = availability;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getAvailability() {
        return availability;
    }
}
