import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidDeviceManager {

    public static void main(String[] args) {
        try {
            // Détecter les appareils Android connectés
            System.out.println("Recherche des appareils connectés...");
            String devicesOutput = executeCommand("adb devices");
            System.out.println("Liste des appareils détectés :");

            // Extraire les appareils connectés
            String[] lines = devicesOutput.split("\n");
            List<AndroidDevice> devices = new ArrayList<>();

            for (String line : lines) {
                if (line.contains("device") && !line.contains("List")) {
                    String deviceId = line.split("\t")[0];
                    System.out.println("Appareil détecté : " + deviceId);

                    // Créer un objet AndroidDevice
                    AndroidDevice device = new AndroidDevice(deviceId);

                    // Récupérer les informations de l'appareil
                    device.setState(executeCommand("adb -s " + deviceId + " get-state").trim());
                    device.setModel(executeCommand("adb -s " + deviceId + " shell getprop ro.product.model").trim());
                    device.setAndroidVersion(executeCommand("adb -s " + deviceId + " shell getprop ro.build.version.release").trim());
                    device.setBatteryLevel(extractBatteryLevel(deviceId));
                    device.setWifiInfo(extractWifiInfo(deviceId));
                    device.setInstalledApps(extractInstalledApps(deviceId));

                    // Ajouter l'appareil à la liste
                    devices.add(device);

                    // Afficher les informations de l'appareil
                    System.out.println(device);
                }
            }

            if (devices.isEmpty()) {
                System.out.println("Aucun appareil Android connecté.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la détection des appareils : " + e.getMessage());
        }
    }

    /**
     * Exécute une commande système et retourne la sortie.
     */
    private static String executeCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        reader.close();
        process.waitFor();
        return output.toString();
    }

    private static String extractBatteryLevel(String deviceId) throws Exception {
        System.out.println("Récupération du niveau de la batterie...");
        String batteryInfo = executeCommand("adb -s " + deviceId + " shell dumpsys battery | grep level").trim();
        return batteryInfo.replace("level: ", "").trim();
    }

    private static WifiInfo extractWifiInfo(String deviceId) throws Exception {
        System.out.println("Récupération des informations Wi-Fi...");
        String wifiInfo = executeCommand("adb -s " + deviceId + " shell dumpsys wifi");
        String ssid = "Wifi déconnecté";
        String macAddress = "Non spécifié";
        String linkSpeed = "Non spécifié";
        String linkAddresses = "Non spécifié";
        String dnsAddresses = "Non spécifié";
        String serverAddress = "Non spécifié";

        // Vérifier si le Wi-Fi est activé
        if (!wifiInfo.contains("Wi-Fi is enabled")) {
            return new WifiInfo(ssid, new String[]{}, macAddress, linkSpeed, new String[]{}, serverAddress);
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
        linkAddresses = linkAddressMatcher.find() ? linkAddressMatcher.group(1) : "Non spécifié";

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
        dnsAddresses = dnsMatcher.find() ? dnsMatcher.group(1) : "Non spécifié";

        // Extraire ServerAddress
        System.out.println("Extraction de l'adresse du serveur...");
        Pattern serverPattern = Pattern.compile("ServerAddress: /([\\d.]+)");
        Matcher serverMatcher = serverPattern.matcher(wifiInfo);
        serverAddress = serverMatcher.find() ? serverMatcher.group(1) : "Non spécifié";

        String[] linkAddressesArray = linkAddresses.split(",");
        String[] dnsAddressesArray = dnsAddresses.replace("/", "").trim().split(",");

        return new WifiInfo(ssid, linkAddressesArray, macAddress, linkSpeed, dnsAddressesArray, serverAddress);
    }

    /**
     * Extrait la liste des applications installées avec leurs versions.
     */
    private static List<AppInfo> extractInstalledApps(String deviceId) throws Exception {
        System.out.println("Récupération des applications installées...");
        String appsOutput = executeCommand("adb -s " + deviceId + " shell pm list packages -3").trim();
        List<AppInfo> apps = new ArrayList<>();

        // Parcours des applications installées
        for (String line : appsOutput.split("\n")) {
            String packageName = line.replace("package:", "").trim();
            String appVersion = extractAppVersion(deviceId, packageName);
            apps.add(new AppInfo(packageName, appVersion));
        }
        return apps;
    }

    /**
     * Extrait la version d'une application installée.
     */
    private static String extractAppVersion(String deviceId, String packageName) throws Exception {
        String versionInfo = executeCommand("adb -s " + deviceId + " shell dumpsys package " + packageName + " | grep versionName").trim();
        if (versionInfo.isEmpty()) {
            return "Non spécifiée";
        }
        return versionInfo.split("=")[1].trim();
    }
}