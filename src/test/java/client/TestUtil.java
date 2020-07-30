package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class that handles testing utilities.
 */
public final class TestUtil {

    private TestUtil() {
    }

    public static String readJsonResource(String file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

}
