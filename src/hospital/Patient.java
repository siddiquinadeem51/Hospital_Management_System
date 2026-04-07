package hospital;

public class Patient {
    private final String id;
    private final String name;
    private final int age;
    private final String gender;
    private final String phone;
    private final String disease;

    public Patient(String id, String name, int age, String gender, String phone, String disease) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.disease = disease;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getDisease() {
        return disease;
    }
}
