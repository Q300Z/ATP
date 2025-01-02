import java.io.File;

public class AppInfo {
    private String packageName;
    private String version;
    private long size = -1;
    private File apkFile = null;

    public AppInfo(String packageName, String version) {
        if (packageName == null || packageName.isEmpty()) {
            throw new IllegalArgumentException("Le nom du package ne peut pas être vide.");
        }
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("La version ne peut pas être vide.");
        }

        this.packageName = packageName;
        this.version = version;
    }

    public AppInfo(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Fichier APK invalide.");
        }

        this.apkFile = file;
        this.size = file.length();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersion() {
        return version;
    }

    public long getSize() {
        return size;
    }

    public File getApkFile() {
        return apkFile;
    }

    @Override
    public String toString() {
        return "Package: " + packageName + ", Version: " + version;
    }
}