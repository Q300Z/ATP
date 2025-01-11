public class DeviceActions {

    private final AndroidDevice device;

    public DeviceActions(AndroidDevice device) {
        this.device = device;
    }

    private void executeCommand(String command) throws Exception {
        AndroidDeviceManager.executeCommand(command);
    }

    public void openApp(String packageName) {
        try {
            executeCommand("adb -s " + device.getId() + " shell monkey -p " + packageName + " -c android.intent.category.LAUNCHER 1");  // Ouvre l'application via AndroidDevice
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture de l'application : " + e.getMessage());
        }
    }

    public void click(int x, int y) {
        try {
            executeCommand("adb -s " + device.getId() + " shell input tap " + x + " " + y);  // Clique sur l'élément via AndroidDevice
        } catch (Exception e) {
            System.out.println("Erreur lors du clic sur l'élément : " + e.getMessage());
        }
    }

    public boolean slideElement(int xA, int yA, int xB, int yB) {
        try {
            executeCommand("adb -s " + device.getId() + " shell input swipe " + xA + " " + yA + " " + xB + " " + yB);  // Glisse l'élément via AndroidDevice
            return true;
        } catch (Exception e) {
            System.out.println("Erreur lors du glissement de l'élément : " + e.getMessage());
            return false;
        }
    }

    public void pressBack() {
        try {
            executeCommand("adb -s " + device.getId() + " shell input keyevent 4");  // Appuie sur le bouton retour via AndroidDevice
        } catch (Exception e) {
            System.out.println("Erreur lors de l'appui sur le bouton retour : " + e.getMessage());
        }
    }

    public void pressHome() {
        try {
            executeCommand("adb -s " + device.getId() + " shell input keyevent 3");  // Appuie sur le bouton d'accueil via AndroidDevice
        } catch (Exception e) {
            System.out.println("Erreur lors de l'appui sur le bouton d'accueil : " + e.getMessage());
        }
    }

    public void pressPower() {
        try {
            executeCommand("adb -s " + device.getId() + " shell input keyevent 26");  // Appuie sur le bouton d'alimentation via AndroidDevice
        } catch (Exception e) {
            System.out.println("Erreur lors de l'appui sur le bouton d'alimentation : " + e.getMessage());
        }
    }

    public void pressVolumeUp() {
        try {
            executeCommand("adb -s " + device.getId() + " shell input keyevent 24");  // Appuie sur le bouton de volume + via AndroidDevice
        } catch (Exception e) {
            System.out.println("Erreur lors de l'appui sur le bouton de volume + : " + e.getMessage());
        }
    }

    public void pressVolumeDown() {
        try {
            executeCommand("adb -s " + device.getId() + " shell input keyevent 25");  // Appuie sur le bouton de volume - via AndroidDevice
        } catch (Exception e) {
            System.out.println("Erreur lors de l'appui sur le bouton de volume - : " + e.getMessage());
        }
    }

}
