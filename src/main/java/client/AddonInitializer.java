package client;

import client.exceptions.UnsupportedAddonException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles the initialization of already existing addons.
 */
public class AddonInitializer {

    private static final String CLASSIC_PATH = "_classic_/interface/addOns";
    private static final String RETAIL_PATH = "interface/addOns";
    private final Path wowPath;
    private final boolean classicMode;

    public AddonInitializer(final Path path, final boolean classicMode) {
        wowPath = path;
        this.classicMode = classicMode;
    }

    /**
     * Finds all folders of addon folder and returns them.
     *
     * @return All path objects representing addon folders.
     * @throws IOException
     */
    public Path[] getExistingAddons() throws IOException {

        final Path addonPath = classicMode ? Paths.get(wowPath.toString(), CLASSIC_PATH)
                : Paths.get(wowPath.toString(), RETAIL_PATH);

        try (Stream<Path> files = Files.list(addonPath)) {
            return files.collect(Collectors.toList())
                        .toArray(new Path[0]);
        }

    }

    /**
     * Evaluates if existing addOn can be imported.
     *
     * @param addonPath the path to the addOn.
     * @return The result of querying the curse API for said Addon.
     * @throws IOException
     * @throws UnsupportedAddonException
     * @throws InterruptedException
     */
    public Addon evaluateExistingAddon(Path addonPath)
            throws IOException, UnsupportedAddonException, InterruptedException {

        List<Path> tocPath;
        try (Stream<Path> files = Files.list(addonPath)) {
            tocPath = files.filter(path -> path.toString()
                                               .endsWith(".toc"))
                           .limit(2)
                           .collect(Collectors.toList());
        }

        if (tocPath.size() == 1) {
            String id = getCurseId(tocPath.get(0));
            CurseAPI api = new CurseAPI();
            return api.getAddon(id);
        } else {
            throw new UnsupportedAddonException("Does not support addOns with more or less than one .toc file");
        }

    }

    /**
     * Tries to find the curse ID inside the .toc file.
     *
     * @param file the path to the .toc file.
     * @return the extracted id as an int.
     * @throws IOException
     * @throws UnsupportedAddonException
     */
    public String getCurseId(Path file) throws IOException, UnsupportedAddonException {

        BufferedReader reader = Files.newBufferedReader(file);

        String line = "";
        while (line != null) {
            line = reader.readLine();
            if (line != null && line.contains("X-Curse-Project-ID")) {
                reader.close();
                return line.replaceAll("[^0-9]", "")
                           .trim();
            }
        }
        reader.close();
        throw new UnsupportedAddonException("Addon does not contain a curse ID");
    }

    /**
     * Validates a selected path.
     *
     * @return true of false if wow.exe exist in dir.
     * @throws IOException
     */
    public boolean validatePath() throws IOException {

        final Path exePath = Paths.get(wowPath.toString() + "/World of Warcraft Launcher.exe");
        return Files.exists(exePath);

    }

}
