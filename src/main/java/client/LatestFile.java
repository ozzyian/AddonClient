package client;

/**
 * Holds the data for certain files for an addon.
 */
public class LatestFile {
    private String id;
    private String displayName;
    private String fileDate;
    private String downloadUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "LatestFiles [displayName=" + displayName + ", downloadUrl=" + downloadUrl + ", fileDate=" + fileDate
                + ", id=" + id + "]";
    }
}
