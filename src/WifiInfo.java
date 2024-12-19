import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WifiInfo {
    private String ssid;
    private String[] ipAddress;
    private String macAddress;
    private String linkSpeed;
    private String[] dnsAddress;
    private String serverAddress;

    public WifiInfo() {
    }

    public void extractWifiInfo(String deviceId) throws Exception {
        System.out.println("Récupération des informations Wi-Fi...");
        String wifiInfo = executeCommand("adb -s " + deviceId + " shell dumpsys wifi");

        // Vérifier si le Wi-Fi est activé
        if (!wifiInfo.contains("Wi-Fi is enabled")) {
            this.ssid = "Wi-Fi désactivé";
            return;
        }

        // Extraire SSID
        System.out.println("Extraction du SSID...");
        Pattern SSIDPattern = Pattern.compile("SSID:\\s+\"([^\"]+)\"");
        Matcher SSIDMatcher = SSIDPattern.matcher(wifiInfo);
        ssid = SSIDMatcher.find() ? SSIDMatcher.group(1) : "Non spécifié";

        // Extraire les Adresse IP
        System.out.println("Extraction des adresses de lien...");
        Pattern linkAddressPattern = Pattern.compile("LinkAddresses: \\[([\\s\\S]*?)]");
        Matcher linkAddressMatcher = linkAddressPattern.matcher(wifiInfo);
        String linkAddresses = linkAddressMatcher.find() ? linkAddressMatcher.group(1) : "Non spécifié";

        // Extraire Adresse MAC avec regex
        System.out.println("Extraction de l'adresse MAC...");
        Pattern macPattern = Pattern.compile("MAC: ([0-9A-Fa-f:]+)");
        Matcher macMatcher = macPattern.matcher(wifiInfo);
        macAddress = macMatcher.find() ? macMatcher.group(1) : "Non spécifié";

        // Extraire Vitesse du lien
        System.out.println("Extraction de la vitesse du lien...");
        Pattern linkSpeedPattern = Pattern.compile("Link speed:\\s*(\\d+Mbps)");
        Matcher linkSpeedMatcher = linkSpeedPattern.matcher(wifiInfo);
        linkSpeed = linkSpeedMatcher.find() ? linkSpeedMatcher.group(1) : "Non spécifié";

        // Extraire DnsAddresses
        System.out.println("Extraction des adresses DNS...");
        Pattern dnsPattern = Pattern.compile("DnsAddresses: \\[([\\s\\S]*?)]");
        Matcher dnsMatcher = dnsPattern.matcher(wifiInfo);
        String dnsAddresses = dnsMatcher.find() ? dnsMatcher.group(1) : "Non spécifié";

        // Extraire ServerAddress
        System.out.println("Extraction de l'adresse du serveur...");
        Pattern serverPattern = Pattern.compile("ServerAddress: /([\\d.]+)");
        Matcher serverMatcher = serverPattern.matcher(wifiInfo);
        serverAddress = serverMatcher.find() ? serverMatcher.group(1) : "Non spécifié";

        ipAddress = linkAddresses.split(",");
        dnsAddress = dnsAddresses.replace("/", "").trim().split(",");
    }

    private String executeCommand(String command) throws Exception {
        return AndroidDeviceManager.executeCommand(command);
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
