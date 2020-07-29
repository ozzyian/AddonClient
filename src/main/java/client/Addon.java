package client;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Holds the necessary data for the addon.
 */
public class Addon {

    private int id;
    private String name;
    private List<LatestFile> latestFiles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LatestFile> getLatestFiles() {
        return latestFiles;
    }

    public void setLatestFiles(List<LatestFile> latestFiles) {
        this.latestFiles = latestFiles;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getLatestFileDate() {

        String latestFileString = latestFiles.get(latestFiles.size() - 1).getFileDate();
        return LocalDateTime.parse(latestFileString.replace("Z", ""));
    }

    @Override
    public String toString() {
        return "Addon [" + name + "]";
    }

}
