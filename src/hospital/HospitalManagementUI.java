package hospital;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class HospitalManagementUI extends JFrame {
    private static final Color BG = new Color(247, 242, 255), CARD = Color.WHITE, BORDER = new Color(226, 219, 243);
    private static final Color TEXT = new Color(38, 30, 65), MUTED = new Color(111, 100, 148);
    private static final Color PINK = new Color(255, 99, 163), BLUE = new Color(82, 177, 255), MINT = new Color(43, 214, 166), GOLD = new Color(255, 188, 88);

    private final List<Patient> patients = new ArrayList<>();
    private final List<Doctor> doctors = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final DefaultTableModel patientModel = model(new String[] {"ID", "Patient", "Age", "Gender", "Phone", "Condition"});
    private final DefaultTableModel doctorModel = model(new String[] {"ID", "Doctor", "Department", "Availability"});
    private final DefaultTableModel appointmentModel = model(new String[] {"ID", "Patient", "Doctor", "Date", "Status"});
    private final JTable patientTable = new JTable(patientModel), doctorTable = new JTable(doctorModel), appointmentTable = new JTable(appointmentModel);
    private final TableRowSorter<DefaultTableModel> patientSorter = new TableRowSorter<>(patientModel);
    private final TableRowSorter<DefaultTableModel> doctorSorter = new TableRowSorter<>(doctorModel);
    private final TableRowSorter<DefaultTableModel> appointmentSorter = new TableRowSorter<>(appointmentModel);
    private final JTabbedPane tabs = new JTabbedPane();
    private final JLabel patientCount = metric(), doctorCount = metric(), appointmentCount = metric(), occupancy = metric();
    private final JLabel dashPatients = metric(), dashDoctors = metric(), dashAppointments = metric(), deskNote = html("Everything feels smooth.");
    private final JTextField pName = field(), pAge = field(), pPhone = field(), pDisease = field();
    private final JComboBox<String> pGender = combo(new String[] {"Male", "Female", "Other"});
    private final JTextField patientSearch = field();
    private final JComboBox<String> patientFilter = combo(new String[] {"All Genders", "Male", "Female", "Other"});
    private final JTextField dName = field(), dDept = field(), dAvail = field();
    private final JTextField doctorSearch = field();
    private final JComboBox<String> doctorFilter = combo(new String[] {"All Departments"});
    private final JTextField aPatient = field(), aDate = field();
    private final JComboBox<String> aDoctor = combo(new String[] {}), aStatus = combo(new String[] {"Scheduled", "Completed", "Pending"});
    private final JTextField appointmentSearch = field();
    private final JComboBox<String> appointmentFilter = combo(new String[] {"All Status", "Scheduled", "Completed", "Pending"});
    private int patientCounter = 1003, doctorCounter = 121, appointmentCounter = 5003;

    public HospitalManagementUI() {
        setTitle("PulseCare 2026");
        setSize(1300, 780);
        setMinimumSize(new Dimension(1200, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel root = new JPanel(new BorderLayout(22, 22));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.add(hero(), BorderLayout.NORTH);
        root.add(body(), BorderLayout.CENTER);
        setContentPane(root);
        patientTable.setRowSorter(patientSorter);
        doctorTable.setRowSorter(doctorSorter);
        appointmentTable.setRowSorter(appointmentSorter);
        seed();
        refreshAll();
    }

    private JPanel hero() {
        GradientPanel p = new GradientPanel(new BorderLayout(18, 18), new Color(91, 58, 199), new Color(255, 105, 169));
        p.setBorder(new EmptyBorder(26, 28, 26, 28));
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(label("PULSECARE 2026", new Color(255, 228, 244), 13, true));
        left.add(Box.createVerticalStrut(10));
        left.add(htmlTitle("Hospital management,<br>but make it clean."));
        left.add(Box.createVerticalStrut(10));
        left.add(htmlWhite("Brighter colors, softer cards, live stats, and buttons that actually do something."));
        left.add(Box.createVerticalStrut(16));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setOpaque(false);
        actions.add(button("New Patient", PINK, () -> tabs.setSelectedIndex(1)));
        actions.add(button("Book Visit", BLUE, () -> tabs.setSelectedIndex(3)));
        actions.add(button("Refresh", MINT, this::refreshAll));
        left.add(actions);
        JPanel right = glassCard();
        right.setPreferredSize(new Dimension(280, 130));
        right.add(label("Next Gen Desk", Color.WHITE, 20, true), BorderLayout.NORTH);
        right.add(htmlWhite("No more boring admin screen. This version feels more like a 2026 student showcase."), BorderLayout.CENTER);
        p.add(left, BorderLayout.CENTER);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel body() {
        JPanel body = new JPanel(new BorderLayout(22, 0));
        body.setOpaque(false);
        body.add(sidebar(), BorderLayout.WEST);
        body.add(mainTabs(), BorderLayout.CENTER);
        return body;
    }

    private JPanel sidebar() {
        JPanel card = card(new BorderLayout(), 280);
        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(label("Shift Snapshot", TEXT, 22, true));
        c.add(Box.createVerticalStrut(8));
        c.add(html("Fast stats for a cleaner front-desk experience."));
        c.add(Box.createVerticalStrut(16));
        c.add(mini("Patients", patientCount, new Color(255, 238, 247), PINK));
        c.add(Box.createVerticalStrut(10));
        c.add(mini("Doctors", doctorCount, new Color(236, 246, 255), BLUE));
        c.add(Box.createVerticalStrut(10));
        c.add(mini("Appointments", appointmentCount, new Color(234, 255, 246), MINT));
        c.add(Box.createVerticalStrut(10));
        c.add(mini("Occupancy", occupancy, new Color(255, 247, 228), GOLD));
        c.add(Box.createVerticalStrut(16));
        JPanel note = card(new BorderLayout(), 0);
        note.setBackground(new Color(251, 248, 255));
        note.add(label("Desk Vibe", TEXT, 15, true), BorderLayout.NORTH);
        note.add(deskNote, BorderLayout.CENTER);
        c.add(note);
        c.add(Box.createVerticalGlue());
        card.add(c, BorderLayout.CENTER);
        return card;
    }

    private JTabbedPane mainTabs() {
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(new Color(243, 236, 255));
        tabs.setForeground(TEXT);
        tabs.setOpaque(true);
        tabs.addTab("Dashboard", dashboard());
        tabs.addTab("Patients", patientsPage());
        tabs.addTab("Doctors", doctorsPage());
        tabs.addTab("Appointments", appointmentsPage());
        return tabs;
    }

    private JPanel dashboard() {
        JPanel p = panel();
        JPanel stats = new JPanel(new GridLayout(1, 3, 16, 16));
        stats.setOpaque(false);
        stats.add(stat("Patient Flow", "Today's admissions", dashPatients, PINK));
        stats.add(stat("Doctor Grid", "Who's available", dashDoctors, BLUE));
        stats.add(stat("Visit Queue", "Upcoming visits", dashAppointments, MINT));
        JPanel bottom = new JPanel(new GridLayout(1, 2, 18, 18));
        bottom.setOpaque(false);
        bottom.add(infoCard("Made for a 2026 demo", "Quick buttons, live numbers, and management actions in every tab make this feel like a real project, not just a mockup."));
        JPanel h = card(new BorderLayout(), 0);
        h.add(label("Department Highlights", TEXT, 22, true), BorderLayout.NORTH);
        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.add(highlightButton("Cardiology", "Heart care and diagnostics", "High demand", new Color(255, 239, 245), PINK));
        rows.add(Box.createVerticalStrut(10));
        rows.add(highlightButton("Neurology", "Consultation and follow-up", "Steady flow", new Color(238, 247, 255), BLUE));
        rows.add(Box.createVerticalStrut(10));
        rows.add(highlightButton("Orthopedics", "Recovery and fracture support", "Moderate load", new Color(236, 255, 247), MINT));
        rows.add(Box.createVerticalStrut(10));
        rows.add(highlightButton("Pediatrics", "Child health and routine care", "Family favorite", new Color(255, 246, 230), GOLD));
        rows.add(Box.createVerticalStrut(10));
        rows.add(highlightButton("Dermatology", "Skin care and treatment reviews", "Trending", new Color(247, 239, 255), new Color(151, 92, 245)));
        rows.add(Box.createVerticalStrut(10));
        rows.add(highlightButton("Emergency Medicine", "Urgent response and trauma care", "24/7 active", new Color(255, 238, 238), new Color(234, 86, 86)));
        JScrollPane departmentPane = new JScrollPane(rows);
        departmentPane.setBorder(BorderFactory.createEmptyBorder());
        departmentPane.setOpaque(false);
        departmentPane.getViewport().setOpaque(false);
        departmentPane.getVerticalScrollBar().setUnitIncrement(12);
        h.add(departmentPane, BorderLayout.CENTER);
        bottom.add(h);
        p.add(stats, BorderLayout.NORTH);
        p.add(bottom, BorderLayout.CENTER);
        return p;
    }

    private JPanel patientsPage() {
        JPanel p = panel();
        JPanel form = grid();
        form.add(label("Patient Name", TEXT, 13, true)); form.add(pName);
        form.add(label("Age", TEXT, 13, true)); form.add(pAge);
        form.add(label("Gender", TEXT, 13, true)); form.add(pGender);
        form.add(label("Phone", TEXT, 13, true)); form.add(pPhone);
        form.add(label("Condition", TEXT, 13, true)); form.add(pDisease);
        JPanel buttons = row(button("Add Patient", PINK, this::addPatient), ghost("Clear Form", this::clearPatient), ghost("Remove Selected", this::removePatient));
        p.add(formCard("Patient Intake", "Add and manage patient records with a fresher layout.", form, buttons, 390), BorderLayout.WEST);
        p.add(tableCard("Patient Records", patientTable, patientToolbar()), BorderLayout.CENTER);
        return p;
    }

    private JPanel doctorsPage() {
        JPanel p = panel();
        JPanel form = grid();
        form.add(label("Doctor Name", TEXT, 13, true)); form.add(dName);
        form.add(label("Department", TEXT, 13, true)); form.add(dDept);
        form.add(label("Availability", TEXT, 13, true)); form.add(dAvail);
        JPanel buttons = row(button("Add Doctor", BLUE, this::addDoctor), ghost("Clear Form", this::clearDoctor), ghost("Remove Selected", this::removeDoctor));
        p.add(formCard("Doctor Roster", "Keep the doctor directory current and demo-friendly.", form, buttons, 390), BorderLayout.WEST);
        p.add(tableCard("Doctor Directory", doctorTable, doctorToolbar()), BorderLayout.CENTER);
        return p;
    }

    private JPanel appointmentsPage() {
        JPanel p = panel();
        JPanel form = grid();
        form.add(label("Patient Name", TEXT, 13, true)); form.add(aPatient);
        form.add(label("Doctor", TEXT, 13, true)); form.add(aDoctor);
        form.add(label("Visit Date", TEXT, 13, true)); form.add(aDate);
        form.add(label("Status", TEXT, 13, true)); form.add(aStatus);
        JPanel buttons = row(button("Book Appointment", MINT, this::addAppointment), ghost("Clear Form", this::clearAppointment), ghost("Remove Selected", this::removeAppointment));
        p.add(formCard("Visit Planner", "Schedule visits and keep the queue moving.", form, buttons, 390), BorderLayout.WEST);
        p.add(tableCard("Appointment Timeline", appointmentTable, appointmentToolbar()), BorderLayout.CENTER);
        return p;
    }

    private void addPatient() {
        if (pName.getText().trim().isEmpty() || pAge.getText().trim().isEmpty() || pPhone.getText().trim().isEmpty() || pDisease.getText().trim().isEmpty()) { warn("Please fill all patient fields."); return; }
        int age;
        try { age = Integer.parseInt(pAge.getText().trim()); } catch (NumberFormatException ex) { warn("Age must be a valid number."); return; }
        Patient patient = new Patient("P" + patientCounter++, pName.getText().trim(), age, pGender.getSelectedItem().toString(), pPhone.getText().trim(), pDisease.getText().trim());
        patients.add(patient);
        patientModel.addRow(new Object[] {patient.getId(), patient.getName(), patient.getAge(), patient.getGender(), patient.getPhone(), patient.getDisease()});
        clearPatient();
        refreshMetrics();
        JOptionPane.showMessageDialog(this, "Patient added successfully.");
    }

    private void addDoctor() {
        if (dName.getText().trim().isEmpty() || dDept.getText().trim().isEmpty() || dAvail.getText().trim().isEmpty()) { warn("Please fill all doctor fields."); return; }
        Doctor doctor = new Doctor("D" + doctorCounter++, dName.getText().trim(), dDept.getText().trim(), dAvail.getText().trim());
        doctors.add(doctor);
        doctorModel.addRow(new Object[] {doctor.getId(), doctor.getName(), doctor.getDepartment(), doctor.getAvailability()});
        fillDoctors();
        refreshDoctorFilterOptions();
        clearDoctor();
        applyDoctorFilter();
        refreshMetrics();
        JOptionPane.showMessageDialog(this, "Doctor added successfully.");
    }

    private void addAppointment() {
        if (aPatient.getText().trim().isEmpty() || aDate.getText().trim().isEmpty() || aDoctor.getSelectedItem() == null) { warn("Please complete the appointment form."); return; }
        Appointment a = new Appointment("A" + appointmentCounter++, aPatient.getText().trim(), aDoctor.getSelectedItem().toString(), aDate.getText().trim(), aStatus.getSelectedItem().toString());
        appointments.add(a);
        appointmentModel.addRow(new Object[] {a.getAppointmentId(), a.getPatientName(), a.getDoctorName(), a.getDate(), a.getStatus()});
        clearAppointment();
        refreshMetrics();
        JOptionPane.showMessageDialog(this, "Appointment booked successfully.");
    }

    private void removePatient() {
        int row = patientTable.getSelectedRow();
        if (row < 0) { warn("Select a patient row to remove."); return; }
        int modelRow = patientTable.convertRowIndexToModel(row);
        patients.remove(modelRow);
        patientModel.removeRow(modelRow);
        refreshMetrics();
    }

    private void removeDoctor() {
        int row = doctorTable.getSelectedRow();
        if (row < 0) { warn("Select a doctor row to remove."); return; }
        int modelRow = doctorTable.convertRowIndexToModel(row);
        String name = doctors.get(modelRow).getName();
        doctors.remove(modelRow);
        doctorModel.removeRow(modelRow);
        appointments.removeIf(a -> a.getDoctorName().equals(name));
        refreshAppointmentTable();
        fillDoctors();
        refreshDoctorFilterOptions();
        applyDoctorFilter();
        applyAppointmentFilter();
        refreshMetrics();
    }

    private void removeAppointment() {
        int row = appointmentTable.getSelectedRow();
        if (row < 0) { warn("Select an appointment row to remove."); return; }
        int modelRow = appointmentTable.convertRowIndexToModel(row);
        appointments.remove(modelRow);
        appointmentModel.removeRow(modelRow);
        refreshMetrics();
    }

    private void clearPatient() { pName.setText(""); pAge.setText(""); pPhone.setText(""); pDisease.setText(""); pGender.setSelectedIndex(0); }
    private void clearDoctor() { dName.setText(""); dDept.setText(""); dAvail.setText(""); }
    private void clearAppointment() { aPatient.setText(""); aDate.setText(""); if (aDoctor.getItemCount() > 0) aDoctor.setSelectedIndex(0); aStatus.setSelectedIndex(0); }

    private void seed() {
        doctors.add(new Doctor("D101", "Dr. Aisha Khan", "Cardiology", "Mon - Fri, 10 AM to 4 PM"));
        doctors.add(new Doctor("D102", "Dr. Raj Mehta", "Neurology", "Mon - Sat, 11 AM to 5 PM"));
        doctors.add(new Doctor("D103", "Dr. Emily Joseph", "Orthopedics", "Tue - Sun, 9 AM to 2 PM"));
        doctors.add(new Doctor("D104", "Dr. Neha Verma", "Dermatology", "Mon - Fri, 1 PM to 6 PM"));
        doctors.add(new Doctor("D105", "Dr. Arjun Patel", "Pediatrics", "Mon - Sat, 9 AM to 3 PM"));
        doctors.add(new Doctor("D106", "Dr. Sana Iqbal", "Gynecology", "Tue - Sun, 10 AM to 4 PM"));
        doctors.add(new Doctor("D107", "Dr. Kabir Nair", "ENT", "Mon - Fri, 11 AM to 7 PM"));
        doctors.add(new Doctor("D108", "Dr. Meera Joshi", "Ophthalmology", "Mon - Sat, 8 AM to 1 PM"));
        doctors.add(new Doctor("D109", "Dr. Vikram Rao", "Urology", "Tue - Sat, 12 PM to 6 PM"));
        doctors.add(new Doctor("D110", "Dr. Priya Sen", "Psychiatry", "Mon - Fri, 10 AM to 5 PM"));
        doctors.add(new Doctor("D111", "Dr. Aditya Bose", "Oncology", "Mon - Thu, 9 AM to 2 PM"));
        doctors.add(new Doctor("D112", "Dr. Nisha Kapoor", "Endocrinology", "Wed - Sun, 10 AM to 3 PM"));
        doctors.add(new Doctor("D113", "Dr. Rohit Das", "Pulmonology", "Mon - Sat, 2 PM to 8 PM"));
        doctors.add(new Doctor("D114", "Dr. Simran Gill", "Radiology", "Mon - Fri, 9 AM to 1 PM"));
        doctors.add(new Doctor("D115", "Dr. Harsh Malhotra", "General Surgery", "Tue - Sun, 11 AM to 5 PM"));
        doctors.add(new Doctor("D116", "Dr. Kavya Reddy", "Nephrology", "Mon - Fri, 8 AM to 12 PM"));
        doctors.add(new Doctor("D117", "Dr. Aman Chawla", "Gastroenterology", "Mon - Sat, 12 PM to 5 PM"));
        doctors.add(new Doctor("D118", "Dr. Isha Menon", "Anesthesiology", "Mon - Fri, 7 AM to 1 PM"));
        doctors.add(new Doctor("D119", "Dr. Dev Khanna", "Emergency Medicine", "Daily, 6 PM to 12 AM"));
        doctors.add(new Doctor("D120", "Dr. Ritu Arora", "Physiotherapy", "Mon - Sat, 9 AM to 6 PM"));
        patients.add(new Patient("P1001", "Rohan Sharma", 34, "Male", "9876543210", "Fever"));
        patients.add(new Patient("P1002", "Ananya Roy", 27, "Female", "9123456780", "Migraine"));
        appointments.add(new Appointment("A5001", "Rohan Sharma", "Dr. Aisha Khan", "28-03-2026", "Scheduled"));
        appointments.add(new Appointment("A5002", "Ananya Roy", "Dr. Raj Mehta", "29-03-2026", "Pending"));
    }

    private void refreshAll() { refreshPatientTable(); refreshDoctorTable(); refreshAppointmentTable(); fillDoctors(); refreshDoctorFilterOptions(); applyPatientFilter(); applyDoctorFilter(); applyAppointmentFilter(); refreshMetrics(); }
    private void refreshPatientTable() { patientModel.setRowCount(0); for (Patient x : patients) patientModel.addRow(new Object[] {x.getId(), x.getName(), x.getAge(), x.getGender(), x.getPhone(), x.getDisease()}); }
    private void refreshDoctorTable() { doctorModel.setRowCount(0); for (Doctor x : doctors) doctorModel.addRow(new Object[] {x.getId(), x.getName(), x.getDepartment(), x.getAvailability()}); }
    private void refreshAppointmentTable() { appointmentModel.setRowCount(0); for (Appointment x : appointments) appointmentModel.addRow(new Object[] {x.getAppointmentId(), x.getPatientName(), x.getDoctorName(), x.getDate(), x.getStatus()}); }
    private void fillDoctors() { aDoctor.removeAllItems(); for (Doctor x : doctors) aDoctor.addItem(x.getName()); }
    private void refreshDoctorFilterOptions() {
        String selected = doctorFilter.getSelectedItem() == null ? "All Departments" : doctorFilter.getSelectedItem().toString();
        doctorFilter.removeAllItems();
        doctorFilter.addItem("All Departments");
        for (Doctor doctor : doctors) {
            boolean exists = false;
            for (int i = 0; i < doctorFilter.getItemCount(); i++) {
                if (doctor.getDepartment().equals(doctorFilter.getItemAt(i))) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                doctorFilter.addItem(doctor.getDepartment());
            }
        }
        doctorFilter.setSelectedItem(selected);
        if (doctorFilter.getSelectedIndex() < 0) doctorFilter.setSelectedIndex(0);
    }

    private void refreshMetrics() {
        String p = String.valueOf(patients.size()), d = String.valueOf(doctors.size()), a = String.valueOf(appointments.size()), o = Math.min(98, 48 + patients.size() * 7) + "%";
        patientCount.setText(p); doctorCount.setText(d); appointmentCount.setText(a); occupancy.setText(o);
        dashPatients.setText(p); dashDoctors.setText(d); dashAppointments.setText(a);
        deskNote.setText("<html><div style='font-size:13px;color:#6f6494;line-height:1.6;'>" + appointments.size() + " visits lined up, " + doctors.size() + " doctors visible, and the desk still looks chill.</div></html>");
    }

    private JPanel patientToolbar() { return searchToolbar(patientSearch, patientFilter, "Gender", this::applyPatientFilter, this::clearPatientFilters); }
    private JPanel doctorToolbar() { return searchToolbar(doctorSearch, doctorFilter, "Department", this::applyDoctorFilter, this::clearDoctorFilters); }
    private JPanel appointmentToolbar() { return searchToolbar(appointmentSearch, appointmentFilter, "Status", this::applyAppointmentFilter, this::clearAppointmentFilters); }

    private JPanel searchToolbar(JTextField searchField, JComboBox<String> filterBox, String filterLabel, Runnable applyAction, Runnable clearAction) {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        searchField.setPreferredSize(new Dimension(170, 34));
        filterBox.setPreferredSize(new Dimension(160, 34));
        toolbar.add(label("Search", MUTED, 12, true));
        toolbar.add(searchField);
        toolbar.add(label(filterLabel, MUTED, 12, true));
        toolbar.add(filterBox);
        toolbar.add(smallButton("Apply", applyAction));
        toolbar.add(smallGhost("Reset", clearAction));
        return toolbar;
    }

    private void applyPatientFilter() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        String search = patientSearch.getText().trim();
        String gender = patientFilter.getSelectedItem() == null ? "All Genders" : patientFilter.getSelectedItem().toString();
        if (!search.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(search)));
        if (!"All Genders".equals(gender)) filters.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(gender) + "$", 3));
        patientSorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void applyDoctorFilter() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        String search = doctorSearch.getText().trim();
        String department = doctorFilter.getSelectedItem() == null ? "All Departments" : doctorFilter.getSelectedItem().toString();
        if (!search.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(search)));
        if (!"All Departments".equals(department)) filters.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(department) + "$", 2));
        doctorSorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void applyAppointmentFilter() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        String search = appointmentSearch.getText().trim();
        String status = appointmentFilter.getSelectedItem() == null ? "All Status" : appointmentFilter.getSelectedItem().toString();
        if (!search.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(search)));
        if (!"All Status".equals(status)) filters.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(status) + "$", 4));
        appointmentSorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void clearPatientFilters() { patientSearch.setText(""); patientFilter.setSelectedIndex(0); applyPatientFilter(); }
    private void clearDoctorFilters() { doctorSearch.setText(""); doctorFilter.setSelectedIndex(0); applyDoctorFilter(); }
    private void clearAppointmentFilters() { appointmentSearch.setText(""); appointmentFilter.setSelectedIndex(0); applyAppointmentFilter(); }
    private void openDepartment(String department) {
        tabs.setSelectedIndex(2);
        doctorSearch.setText("");
        doctorFilter.setSelectedItem(department);
        if (doctorFilter.getSelectedIndex() < 0) {
            doctorFilter.setSelectedIndex(0);
            doctorSearch.setText(department);
        }
        applyDoctorFilter();
    }

    private JPanel panel() { JPanel p = new JPanel(new BorderLayout(18, 18)); p.setOpaque(false); return p; }
    private JPanel grid() { JPanel p = new JPanel(new GridLayout(0, 2, 12, 12)); p.setOpaque(false); return p; }
    private JPanel row(JButton... buttons) { JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); p.setOpaque(false); for (JButton b : buttons) p.add(b); return p; }
    private JPanel card(BorderLayout layout, int width) { JPanel p = new JPanel(layout); p.setBackground(CARD); p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(20, 20, 20, 20))); if (width > 0) p.setPreferredSize(new Dimension(width, 100)); return p; }
    private JPanel glassCard() { JPanel p = new JPanel(new BorderLayout(0, 10)); p.setOpaque(false); p.setBorder(new EmptyBorder(18, 18, 18, 18)); return p; }
    private JPanel mini(String title, JLabel value, Color fill, Color accent) { JPanel p = card(new BorderLayout(0, 8), 0); p.setBackground(fill); p.add(label(title, MUTED, 13, true), BorderLayout.NORTH); value.setForeground(accent); p.add(value, BorderLayout.CENTER); return p; }
    private JPanel stat(String title, String sub, JLabel value, Color accent) { JPanel p = card(new BorderLayout(0, 12), 0); p.add(label(title, TEXT, 18, true), BorderLayout.NORTH); JPanel c = new JPanel(); c.setOpaque(false); c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS)); c.add(label(sub, MUTED, 13, false)); value.setForeground(accent); c.add(Box.createVerticalStrut(12)); c.add(value); p.add(c, BorderLayout.CENTER); return p; }
    private JPanel infoCard(String title, String text) { JPanel p = card(new BorderLayout(0, 12), 0); p.add(label(title, TEXT, 22, true), BorderLayout.NORTH); p.add(html(text), BorderLayout.CENTER); return p; }
    private JPanel highlightButton(String name, String desc, String tag, Color fill, Color accent) {
        JButton b = new JButton();
        b.setLayout(new BorderLayout(10, 0));
        b.setBackground(fill);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.addActionListener(e -> openDepartment(name));
        JLabel text = new JLabel("<html><b style='font-size:14px;color:#261e41;'>" + name + "</b><br><span style='font-size:12px;color:#6f6494;'>" + desc + "</span></html>");
        JLabel badge = new JLabel(tag, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(Color.WHITE);
        badge.setForeground(accent);
        badge.setBorder(new EmptyBorder(10, 14, 10, 14));
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.add(text, BorderLayout.CENTER);
        b.add(badge, BorderLayout.EAST);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(b, BorderLayout.CENTER);
        return wrap;
    }
    private JPanel formCard(String title, String sub, JPanel form, JPanel buttons, int width) { JPanel p = card(new BorderLayout(0, 14), width); p.add(label(title, TEXT, 23, true), BorderLayout.NORTH); JPanel c = new JPanel(new BorderLayout(0, 14)); c.setOpaque(false); c.add(html(sub), BorderLayout.NORTH); c.add(form, BorderLayout.CENTER); p.add(c, BorderLayout.CENTER); p.add(buttons, BorderLayout.SOUTH); return p; }
    private JPanel tableCard(String title, JTable table, JPanel toolbar) {
        styleTable(table);
        JPanel p = card(new BorderLayout(0, 12), 0);
        p.add(label(title, TEXT, 23, true), BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(toolbar, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        center.add(sp, BorderLayout.CENTER);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(239, 233, 249));
        table.setSelectionBackground(new Color(255, 226, 240));
        table.setSelectionForeground(TEXT);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setOpaque(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(247, 240, 255));
        table.getTableHeader().setForeground(TEXT);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 12, 0, 12));

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                    return c;
                }

                c.setForeground(TEXT);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(252, 248, 255));

                if (table == appointmentTable && column == 4) {
                    String status = value == null ? "" : value.toString();
                    if ("Scheduled".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(232, 255, 244));
                        c.setForeground(new Color(22, 134, 96));
                    } else if ("Pending".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(255, 245, 221));
                        c.setForeground(new Color(186, 120, 20));
                    } else if ("Completed".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(234, 241, 255));
                        c.setForeground(new Color(57, 101, 196));
                    }
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private DefaultTableModel model(String[] cols) { return new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } }; }
    private JTextField field() { JTextField f = new JTextField(); f.setFont(new Font("Segoe UI", Font.PLAIN, 13)); f.setMargin(new Insets(8, 10, 8, 10)); return f; }
    private JComboBox<String> combo(String[] vals) { JComboBox<String> c = new JComboBox<>(vals); c.setFont(new Font("Segoe UI", Font.PLAIN, 13)); return c; }
    private JLabel label(String text, Color color, int size, boolean bold) { JLabel l = new JLabel(text); l.setForeground(color); l.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size)); return l; }
    private JLabel metric() { return label("0", TEXT, 30, true); }
    private JLabel html(String text) { return new JLabel("<html><div style='font-size:13px;color:#6f6494;line-height:1.6;'>" + text + "</div></html>"); }
    private JLabel htmlWhite(String text) { return new JLabel("<html><div style='font-size:13px;color:#fff2f8;line-height:1.6;'>" + text + "</div></html>"); }
    private JLabel htmlTitle(String text) { JLabel l = new JLabel("<html>" + text + "</html>"); l.setForeground(Color.WHITE); l.setFont(new Font("Segoe UI", Font.BOLD, 34)); return l; }
    private JButton button(String text, Color color, Runnable run) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 42));
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.addActionListener(e -> run.run());
        return b;
    }

    private JButton ghost(String text, Runnable run) {
        JButton b = new JButton(text);
        b.setBackground(new Color(239, 232, 255));
        b.setForeground(TEXT);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(true);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 42));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 190, 235)),
                new EmptyBorder(10, 18, 10, 18)));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.addActionListener(e -> run.run());
        return b;
    }
    private JButton smallButton(String text, Runnable run) { JButton b = button(text, new Color(130, 92, 244), run); b.setPreferredSize(new Dimension(88, 34)); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); return b; }
    private JButton smallGhost(String text, Runnable run) { JButton b = ghost(text, run); b.setPreferredSize(new Dimension(88, 34)); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); return b; }
    private void warn(String text) { JOptionPane.showMessageDialog(this, text, "Validation", JOptionPane.WARNING_MESSAGE); }

    private static class GradientPanel extends JPanel {
        private final Color start, end;
        GradientPanel(BorderLayout layout, Color start, Color end) { super(layout); this.start = start; this.end = end; setOpaque(false); }
        protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end)); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36); g2.dispose(); super.paintComponent(g); }
    }
}
