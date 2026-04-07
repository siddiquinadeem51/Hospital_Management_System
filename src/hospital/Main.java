package hospital;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fall back to the default look and feel if the system theme is unavailable.
        }

        SwingUtilities.invokeLater(() -> {
            HospitalManagementUI ui = new HospitalManagementUI();
            ui.setVisible(true);
        });
    }
}
