package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
// import java.nio.file.Paths;

/**
 * Handles the initialization of already existing addons.
 */
public class AddonInitializer {

    private static final String CLASSIC_PATH = "_classic_/interface/addOns";
    private static final String RETAIL_PATH = "interface/addOns";
    private Path wowPath;
    private boolean classicMode;

    public AddonInitializer(Path path, boolean classicMode) {
        wowPath = path;
        this.classicMode = classicMode;
    }

    /**
     * @return All path objects representing addon folders.
     * @throws IOException
     */
    public Path[] getExistingAddons() throws IOException {

        Path addonPath = classicMode ? Paths.get(wowPath.toString(), CLASSIC_PATH)
                : Paths.get(wowPath.toString(), RETAIL_PATH);


        try (Stream<Path> files = Files.list(addonPath)) {
            return files.collect(Collectors.toList()).toArray(new Path[0]);
        }

    }

    /**
     *
     * @return true of false if wow.exe exist in dir.
     */
    public boolean validatePath() {



        return false;
    }

}
