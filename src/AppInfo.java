class AppInfo {
    private String packageName;
    private String version;

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

    @Override
    public String toString() {
        return "Package: " + packageName + ", Version: " + version;
    }
}