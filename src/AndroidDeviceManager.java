import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AndroidDeviceManager {

    public static void main(String[] args) {
        try {
            System.out.println("Recherche des appareils connectés...");
            String devicesOutput = executeCommand("adb devices");
            System.out.println("Liste des appareils détectés :");

            String[] lines = devicesOutput.split("\n");
            List<AndroidDevice> devices = new ArrayList<>();

            for (String line : lines) {
                if (line.contains("device") && !line.contains("List")) {
                    String deviceId = line.split("\t")[0];
                    System.out.println("Appareil détecté : " + deviceId);

                    AndroidDevice device = new AndroidDevice(deviceId);

                    // Récupérer toutes les informations en une seule fois
                    device.setDeviceInfo();

                    devices.add(device);
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

    public static String executeCommand(String command) throws Exception {
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
}
