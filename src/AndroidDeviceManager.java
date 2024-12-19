import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AndroidDeviceManager {

    public static List<AndroidDevice> getConnectedDevices() throws Exception {
        System.out.println("Recherche des appareils connectés...");
        String devicesOutput = executeCommand("adb devices");
        String[] lines = devicesOutput.split("\n");
        System.out.println(lines.length - 1 + " appareils détectés.");
        List<AndroidDevice> devices = new ArrayList<>();

        for (String line : lines) {
            if (line.contains("device") && !line.contains("List")) {
                System.out.println("Appareil connecté : " + line.split("\t")[0]);
                String deviceId = line.split("\t")[0];
                AndroidDevice device = new AndroidDevice(deviceId);
                device.setDeviceInfo();
                devices.add(device);
            }
        }

        return devices;
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
