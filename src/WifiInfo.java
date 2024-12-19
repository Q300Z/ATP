import java.util.Arrays;

/**
 * Classe représentant les informations Wi-Fi d'un appareil Android.
 */
class WifiInfo {
    private final String ssid;
    private final String[] ipAddress;
    private final String macAddress;
    private final String linkSpeed;
    private final String[] dnsAddress;
    private final String serverAddress;

    public WifiInfo(String ssid, String[] ipAddress, String macAddress, String linkSpeed, String[] dnsAddress, String serverAddress) {
        this.ssid = ssid;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.linkSpeed = linkSpeed;
        this.dnsAddress = dnsAddress;
        this.serverAddress = serverAddress;
    }

    @Override
    public String toString() {
        return "WifiInfo{" +
                "ssid='" + ssid + '\'' +
                ", ipAddress=" + Arrays.toString(ipAddress) +
                ", macAddress='" + macAddress + '\'' +
                ", linkSpeed='" + linkSpeed + '\'' +
                ", dnsAddress=" + Arrays.toString(dnsAddress) +
                ", serverAddress='" + serverAddress + '\'' +
                '}';
    }
}