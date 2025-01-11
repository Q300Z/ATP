public class DeviceAutomation extends DeviceActions {

    public DeviceAutomation(AndroidDevice device) {
        super(device);  // Appelle le constructeur de DeviceActions
    }

    public void checkAndUpdateSystem() {
        openApp("com.android.settings");  // Ouvre les paramètres système
    }
}
