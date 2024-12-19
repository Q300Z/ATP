import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AndroidDeviceManagerGUI {

    public static void main(String[] args) {
        // Créer la fenêtre principale
        JFrame frame = new JFrame("Gestion des Appareils Android");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        // Barre de chargement
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        // Créer un panneau avec des onglets
        JTabbedPane tabbedPane = new JTabbedPane();

        // Bouton pour récupérer les informations
        JButton fetchButton = new JButton("Récupérer les Infos des Appareils");
        fetchButton.addActionListener(e -> {
            progressBar.setVisible(true); // Afficher la barre de chargement

            // Effacer les anciens onglets
            tabbedPane.removeAll();

            // Utiliser un SwingWorker pour exécuter l'opération en arrière-plan
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    try {
                        // Récupérer les informations des appareils connectés
                        List<AndroidDevice> connectedDevices = AndroidDeviceManager.getConnectedDevices();

                        if (connectedDevices.isEmpty()) {
                            JPanel noDevicePanel = new JPanel();
                            noDevicePanel.add(new JLabel("Aucun appareil Android connecté."));
                            tabbedPane.add("Aucun Appareil", noDevicePanel);
                        } else {
                            for (AndroidDevice device : connectedDevices) {
                                // Créer un panneau pour cet appareil
                                JPanel devicePanel = new JPanel();
                                devicePanel.setLayout(new GridLayout(3, 1, 10, 10)); // Trois sections

                                // Section 1 : Informations de base
                                JTextArea basicInfoArea = new JTextArea();
                                basicInfoArea.setEditable(false);
                                WifiInfo wifiInfo = device.getWifiInfo();
                                basicInfoArea.append("Nom : " + device.getModel() + "\n");
                                basicInfoArea.append("Version Android : " + device.getAndroidVersion() + "\n");
                                basicInfoArea.append("Batterie : " + device.getBatteryLevel() + "%\n");
                                basicInfoArea.append("Réseau Wi-Fi : " + wifiInfo.getSsid() + "\n");
                                basicInfoArea.append("Adresse IP : " + (wifiInfo.getIpAddress().length > 0 ? wifiInfo.getIpAddress()[0] : "Non spécifiée") + "\n");
                                JScrollPane basicScrollPane = new JScrollPane(basicInfoArea);

                                // Section 2 : Informations réseau avancées
                                JTextArea networkInfoArea = new JTextArea();
                                networkInfoArea.setEditable(false);
                                networkInfoArea.append("Adresse MAC : " + wifiInfo.getMacAddress() + "\n");
                                networkInfoArea.append("Vitesse du lien : " + wifiInfo.getLinkSpeed() + "\n");
                                networkInfoArea.append("DNS : " + String.join(", ", wifiInfo.getDnsAddress()) + "\n");
                                networkInfoArea.append("Adresse du serveur : " + wifiInfo.getServerAddress() + "\n");
                                JScrollPane networkScrollPane = new JScrollPane(networkInfoArea);

                                // Section 3 : Applications installées
                                JTextArea appsInfoArea = new JTextArea();
                                appsInfoArea.setEditable(false);
                                for (AppInfo app : device.getInstalledApps()) {
                                    appsInfoArea.append(app.toString() + "\n");
                                }
                                JScrollPane appsScrollPane = new JScrollPane(appsInfoArea);

                                // Ajouter les trois sections au panneau de l'appareil
                                devicePanel.add(basicScrollPane);
                                devicePanel.add(networkScrollPane);
                                devicePanel.add(appsScrollPane);

                                // Ajouter ce panneau dans un onglet
                                tabbedPane.add("Appareil : " + device.getModel(), devicePanel);
                            }
                        }
                    } catch (Exception ex) {
                        JPanel errorPanel = new JPanel();
                        errorPanel.add(new JLabel("Erreur : " + ex.getMessage()));
                        tabbedPane.add("Erreur", errorPanel);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    progressBar.setVisible(false); // Masquer la barre de chargement
                }
            }.execute();
        });

        // Ajouter les composants à la fenêtre
        frame.setLayout(new BorderLayout());
        frame.add(progressBar, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(fetchButton, BorderLayout.SOUTH);

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }
}
