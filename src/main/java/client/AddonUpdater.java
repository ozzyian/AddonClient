package client;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Handles the updating of addons.
 */
public class AddonUpdater {

    private CurseAPI curseAPI;

    public AddonUpdater(CurseAPI curseAPI) {
        this.curseAPI = curseAPI;
    }

    /**
     *
     * @param check
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean isUpToDate(Addon check) throws IOException, InterruptedException {

        LocalDateTime currentFileDate = check.getLatestFileDate();
        LocalDateTime otherFileDate = curseAPI.getAddonData(check.getId() + "").getLatestFileDate();

        return currentFileDate.isEqual(otherFileDate);
    }
    
    /**
     * Updates a specific addon object with new data.
     * 
     * @param toUpdate The addon that should be updated
     * @return Addon with the new data.
     * @throws InterruptedException
     * @throws IOException
     */
    public Addon updateAddon(Addon toUpdate) throws IOException, InterruptedException {

        Addon updated = curseAPI.getAddonData(toUpdate.getId()+"");

        return updated;
    }

}
