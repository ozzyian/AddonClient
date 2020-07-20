package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// import java.nio.file.Paths;
/**
 * Test class for AddonInitializer.
 */
public class AddonInitializerTest {
    /**
     * Test for getExistingAddonsMethod.
     */
    @Test
    void getExistingAddonsTest(@TempDir Path tempDir) {

        Path classicAddonPath = Paths.get(tempDir.toString(), "_classic_/interface/addOns");
        final int numberOfAddons = 5;
        Path[] expected = new Path[numberOfAddons];

        try {

            Files.createDirectories(classicAddonPath);
            for (int i = 0; i < numberOfAddons; i++) {
                expected[i] = Files.createFile(classicAddonPath.resolve("file" + i));
            }

            AddonInitializer aI = new AddonInitializer(tempDir, true);
            Path[] actual;
            actual = aI.getExistingAddons();

            for (int i = 0; i < actual.length; i++) {
                System.out.println(i + ": " + actual[i]);
            }

            for (int i = 0; i < expected.length; i++) {
                System.out.println(i + ": " + expected[i]);
            }
            assertNotNull(actual);
            assertNotNull(expected);
            Arrays.sort(expected);
            Arrays.sort(actual);
            assertArrayEquals(expected, actual);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void validatePathTest() {

    }
}
