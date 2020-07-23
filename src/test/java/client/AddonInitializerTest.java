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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for AddonInitializer and contains VERY SIMPLE TESTING, needs more.
 */
public class AddonInitializerTest {

    /**
     * @param tempDir path where files should be generated.
     * @return Array with the generated paths.
     * @throws IOException
     */
    Path[] generateTempFiles(Path tempDir) throws IOException {
        Random rand = new Random();
        final int baseLineNumber = 10;
        final int numberOfAddons = rand.nextInt(baseLineNumber) + 1;
        Path[] expected = new Path[numberOfAddons];
        Files.createDirectories(tempDir);
        for (int i = 0; i < numberOfAddons; i++) {
            expected[i] = Files.createFile(tempDir.resolve("file" + i));
        }

        return expected;

    }

    /**
     * Generates a temporary .toc file with random amount of lines.
     *
     * @param tempDir Path to generate the .toc
     * @param line    Line to be added inside file.
     * @return the path to the generated .toc file.
     * @throws IOException
     */
    Path generateTempTocFile(Path tempDir, String line) throws IOException {
        Path addonToc = Files.createFile(tempDir.resolve("tempToc.toc"));

        try (FileWriter writer = new FileWriter(new File(addonToc.toString()))) {
            Random rand = new Random();
            final int baseLineNumber = 10;
            int randomLinesTowrite = rand.nextInt(baseLineNumber) + 1;

            for (int i = 0; i < rand.nextInt(randomLinesTowrite); i++) {
                writer.write("## DUMMMYLINE");
            }
            writer.write(line);
            for (int i = 0; i < rand.nextInt(randomLinesTowrite); i++) {
                writer.write("## DUMMMYLINE");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addonToc;

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
            Path[] actual = aI.getExistingAddonsPaths();
            assertNotNull(actual, "Actual was null.");
            assertNotNull(expected, "Expected was null.");
            Arrays.sort(expected);
            Arrays.sort(actual);
            assertArrayEquals(expected, actual, "Arrays should be of equal lenght and same elements");

        } catch (IOException e) {
            fail("IOException was thrown.");
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
            Path[] actual = aI.getExistingAddonsPaths();

            assertNotNull(actual);
            assertNotNull(expected);
            Arrays.sort(expected);
            Arrays.sort(actual);
            assertArrayEquals(expected, actual, "Arrays should be of equal lenght and same elements");
        } catch (IOException e) {
            fail("IOException was thrown.");
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
            fail("IOException was thrown.");
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
            fail("IOException was thrown.");
            e.printStackTrace();
        }
    }

    /**
     * Test for evaluating an existing addOn.
     */
    @Test
    void getCurseIdTestSuccess(@TempDir Path tempDir) {
        try {

            String correctLine = "## X-Curse-Project-ID: 3358";
            Path tocFile = generateTempTocFile(tempDir, correctLine);
            AddonInitializer aInitializer = new AddonInitializer(tempDir, false);

            assertEquals("3358", aInitializer.getCurseId(tocFile));
        } catch (IOException | UnsupportedAddonException | NullPointerException e) {
            fail("IOException was thrown.");
            e.printStackTrace();
        }

    }

    @Test
    void getCurseIdTestNotSupportedException(@TempDir Path tempDir) {
        try {
            String faultyLine = "## X-Curse-Proje-ID: 3358";
            Path tocFileWithoutId;
            tocFileWithoutId = generateTempTocFile(tempDir, faultyLine);

            AddonInitializer aInitializer = new AddonInitializer(tempDir, false);

            Assertions.assertThrows(UnsupportedAddonException.class, () -> {
                aInitializer.getCurseId(tocFileWithoutId);
            });

        } catch (IOException e) {
            fail("IOException was thrown.");
            e.printStackTrace();
        }
    }

    @Test
    void evaluateExistingAddonTest(@TempDir Path tempDir) {
        try {
            AddonInitializer aInitializer = new AddonInitializer(tempDir, false);
            Path tempToc = generateTempTocFile(tempDir, "## X-Curse-Proje-ID: 3358");
            assertTrue(aInitializer.evaluateExistingAddon(tempDir));
            Files.delete(tempToc);
            assertFalse(aInitializer.evaluateExistingAddon(tempDir));
        } catch (IOException e) {
            fail("IOException was thrown.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            fail("InterruptedException was thrown.");
            e.printStackTrace();
        }
    }

}
