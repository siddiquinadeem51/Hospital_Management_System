package hospital;

public class Appointment {
    private final String appointmentId;
    private final String patientName;
    private final String doctorName;
    private final String date;
    private final String status;

    public Appointment(String appointmentId, String patientName, String doctorName, String date, String status) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
        this.status = status;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
