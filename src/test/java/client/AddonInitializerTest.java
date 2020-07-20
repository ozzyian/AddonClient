package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

            assertNotNull(actual);
            assertNotNull(expected);
            Arrays.sort(expected);
            Arrays.sort(actual);
            assertArrayEquals(expected, actual,
                    "Arrays should be of equal lenght and same elements");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test for correct folder choice.
     */
    @Test
    void validatePathCorrectFolderTest(@TempDir Path tempDir) {

        try {

            Files.createFile(tempDir.resolve("World of Warcraft Launcher.exe"));
            AddonInitializer aiRetail = new AddonInitializer(tempDir, false);
            AddonInitializer aiClassic = new AddonInitializer(tempDir, true);
            assertTrue(aiRetail.validatePath(), "File should exist inside directory");
            assertTrue(aiClassic.validatePath(), "File should exist inside directory");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for wrong folder choice.
     */
    @Test
    void validatePathWrongFolderTest(@TempDir Path tempDir) {
        try {

            Files.createFile(tempDir.resolve("World of Warcraft Launcher"));
            AddonInitializer aiRetail = new AddonInitializer(tempDir, false);
            AddonInitializer aiClassic = new AddonInitializer(tempDir, true);
            assertFalse(aiRetail.validatePath(), "Should not be true for wrong file");
            assertFalse(aiClassic.validatePath(), "Should not be true for wrong file");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
