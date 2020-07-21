package client;

import client.exceptions.UnsupportedAddonException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

// import java.nio.file.Paths;
/**
 * Test class for AddonInitializer.
 */
public class AddonInitializerTest {

    /**
     * @param tempDir path where files should be generated.
     * @return Array with the generated paths.
     * @throws IOException
     */
    Path[] generateTempFiles(Path tempDir) throws IOException {
        final int numberOfAddons = 5;
        Path[] expected = new Path[numberOfAddons];

        Files.createDirectories(tempDir);
        for (int i = 0; i < numberOfAddons; i++) {
            expected[i] = Files.createFile(tempDir.resolve("file" + i));
        }

        return expected;

    }

    /**
     * @param tempDir tempDir for testing purposes.
     */
    @Test
    void getExistingAddonsClassicTest(@TempDir Path tempDir) {

        Path classicAddonPath = Paths.get(tempDir.toString(), "_classic_/interface/addOns");

        try {
            AddonInitializer aI = new AddonInitializer(tempDir, true);
            Path[] expected = generateTempFiles(classicAddonPath);
            Path[] actual = aI.getExistingAddons();
            assertNotNull(actual);
            assertNotNull(expected);
            Arrays.sort(expected);
            Arrays.sort(actual);
            assertArrayEquals(expected, actual, "Arrays should be of equal lenght and same elements");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param tempDir tempDir for testing purposes.
     */
    @Test
    void getExistingAddonsRetailTest(@TempDir Path tempDir) {

        Path retailAddonPath = Paths.get(tempDir.toString(), "interface/addOns");
        try {
            AddonInitializer aI = new AddonInitializer(tempDir, false);
            Path[] expected = generateTempFiles(retailAddonPath);
            Path[] actual = aI.getExistingAddons();

            assertNotNull(actual);
            assertNotNull(expected);
            Arrays.sort(expected);
            Arrays.sort(actual);
            assertArrayEquals(expected, actual, "Arrays should be of equal lenght and same elements");
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

    /**
     * Test for evaluating an existing addOn.
     */
    @Test
    void getCurseIdTest(@TempDir Path tempDir) {

        try {
            Random rand = new Random();
            final int baseLineNumber = 10;
            int randomLinesTowrite = rand.nextInt(baseLineNumber);
            AddonInitializer aInitializer = new AddonInitializer(tempDir, false);
            Path addonToc = Files.createFile(tempDir.resolve("testToc.toc"));
            String idLine = "## X-Curse-Project-ID: 3358";
            FileWriter writer = new FileWriter(new File(addonToc.toString()));

            for (int i = 0; i < rand.nextInt(randomLinesTowrite); i++) {
                writer.write("## DUMMMYLINE");
            }
            writer.write(idLine);
            for (int i = 0; i < rand.nextInt(randomLinesTowrite); i++) {
                writer.write("## DUMMMYLINE");
            }
            writer.close();
            assertEquals("3358", aInitializer.getCurseId(addonToc));
        } catch (IOException | UnsupportedAddonException e) {
            fail("Test should not throw any exception...");
            e.printStackTrace();
        }

    }

}
