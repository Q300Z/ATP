import javax.swing.*;
import java.awt.*;
import java.io.File;
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
                                devicePanel.setLayout(new GridLayout(2, 1, 10, 10)); // Trois sections

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
                                JPanel appsPanel = new JPanel();
                                appsPanel.setLayout(new BoxLayout(appsPanel, BoxLayout.Y_AXIS));

                                // Liste des applications installées
                                for (AppInfo app : device.getInstalledApps()) {
                                    JPanel appPanel = new JPanel();
                                    appPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                                    // Afficher les informations de l'application
                                    JLabel appInfoLabel = new JLabel(app.toString());
                                    appPanel.add(appInfoLabel);

                                    // Bouton pour désinstaller une application
                                    JButton uninstallButton = getUninstallPart(device, app);
                                    appPanel.add(uninstallButton);

                                    // Ajouter ce panneau d'application au panneau des applications
                                    appsPanel.add(appPanel);
                                }
                                JScrollPane appsScrollPane = new JScrollPane(appsPanel);

                                // Section 4 : Installer une application
                                JPanel installAppPanel = new JPanel();
                                installAppPanel.setLayout(new BorderLayout());

                                // Titre de la section
                                JLabel installAppLabel = new JLabel("Installer une application APK :");
                                installAppPanel.add(installAppLabel, BorderLayout.NORTH);

                                // Zone de sélection de fichier et bouton d'installation
                                JPanel fileSelectionPanel = getInstallPart(device);

                                installAppPanel.add(fileSelectionPanel, BorderLayout.CENTER);

                                // Ajouter les trois sections au panneau de l'appareil
                                devicePanel.add(basicScrollPane);
                                devicePanel.add(networkScrollPane);
                                devicePanel.add(appsScrollPane);
                                devicePanel.add(installAppPanel);

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

    private static JButton getUninstallPart(AndroidDevice device, AppInfo app) {
        JButton uninstallButton = new JButton("Désinstaller");
        uninstallButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Voulez-vous vraiment désinstaller " + app.getPackageName() + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    device.uninstallApp(app);
                    JOptionPane.showMessageDialog(null, "Application désinstallée avec succès : " + app.getPackageName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la désinstallation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return uninstallButton;
    }

    private static JPanel getInstallPart(AndroidDevice device) {
        JPanel fileSelectionPanel = new JPanel();
        fileSelectionPanel.setLayout(new FlowLayout());

        JTextField apkFilePathField = new JTextField(30);
        apkFilePathField.setEditable(false);
        fileSelectionPanel.add(apkFilePathField);

        JButton browseButton = new JButton("Parcourir...");
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers APK", "apk"));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                apkFilePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        fileSelectionPanel.add(browseButton);

        JButton installApkButton = new JButton("Installer");
        installApkButton.addActionListener(e -> {
            String apkFilePath = apkFilePathField.getText();
            if (apkFilePath.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner un fichier APK.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File apkFile = new File(apkFilePath);
            if (!apkFile.exists() || !apkFile.isFile()) {
                JOptionPane.showMessageDialog(null, "Le fichier APK sélectionné est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                AppInfo appInfo = new AppInfo(apkFile);
                device.installApp(appInfo);
                JOptionPane.showMessageDialog(null, "Application installée avec succès");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'installation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        fileSelectionPanel.add(installApkButton);
        return fileSelectionPanel;
    }
}
