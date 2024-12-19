import java.util.ArrayList;
import java.util.List;

public class AndroidDevice {
    private final String id;
    private String model;
    private String androidVersion;
    private String batteryLevel;
    private WifiInfo wifiInfo;
    private List<AppInfo> installedApps;
    private String state;

    public AndroidDevice(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public WifiInfo getWifiInfo() {
        return wifiInfo;
    }

    public List<AppInfo> getInstalledApps() {
        return installedApps;
    }

    public String getState() {
        return state;
    }

    public void setDeviceInfo() throws Exception {
        this.state = executeCommand("adb -s " + id + " get-state").trim();
        this.model = executeCommand("adb -s " + id + " shell getprop ro.product.model").trim();
        this.androidVersion = executeCommand("adb -s " + id + " shell getprop ro.build.version.release").trim();
        this.batteryLevel = extractBatteryLevel();
        this.wifiInfo = new WifiInfo();
        this.wifiInfo.extractWifiInfo(id);
        this.installedApps = extractInstalledApps();
    }

    private String executeCommand(String command) throws Exception {
        return AndroidDeviceManager.executeCommand(command);
    }

    private String extractBatteryLevel() throws Exception {
        String batteryInfo = executeCommand("adb -s " + id + " shell dumpsys battery | grep level").trim();
        return batteryInfo.replace("level: ", "").trim();
    }

    private List<AppInfo> extractInstalledApps() throws Exception {
        String appsOutput = executeCommand("adb -s " + id + " shell pm list packages -3").trim();
        List<AppInfo> apps = new ArrayList<>();
        for (String line : appsOutput.split("\n")) {
            String packageName = line.replace("package:", "").trim();
            String appVersion = extractAppVersion(packageName);
            apps.add(new AppInfo(packageName, appVersion));
        }
        return apps;
    }

    private String extractAppVersion(String packageName) throws Exception {
        String versionInfo = executeCommand("adb -s " + id + " shell dumpsys package " + packageName + " | grep versionName").trim();
        return versionInfo.isEmpty() ? "Non spécifiée" : versionInfo.split("=")[1].trim();
    }

    @Override
    public String toString() {
        return "AndroidDevice {" +
                "ID='" + id + '\'' +
                ", Modèle='" + model + '\'' +
                ", Version Android='" + androidVersion + '\'' +
                ", Batterie='" + batteryLevel + "%'" +
                ", Wi-Fi=" + wifiInfo +
                ", Apps Installées=" + installedApps.toString() +
                ", État='" + state + '\'' +
                '}';
    }
}
