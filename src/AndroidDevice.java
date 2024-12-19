import java.util.ArrayList;
import java.util.List;

class AndroidDevice {
    private final String id;
    private String model;
    private String androidVersion;
    private String batteryLevel;
    private WifiInfo wifiInfo;
    private List<AppInfo> installedApps;
    private String state;

    public AndroidDevice(String id) {
        this.id = id;
        this.installedApps = new ArrayList<>();
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public void setWifiInfo(WifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

    public void setInstalledApps(List<AppInfo> installedApps) {
        this.installedApps = installedApps;
    }

    public void setState(String state) {
        this.state = state;
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
