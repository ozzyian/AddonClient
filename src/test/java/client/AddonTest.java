package client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for Addon class. Simple get or setters not included.
 */
public class AddonTest {

    @Test
    void getLatestFileDateTest() {

        Addon mockAddon = new Addon();
        List<LatestFile> mockLatestFilesList = new ArrayList<LatestFile>();
        LatestFile mockLatestFile = new LatestFile();
        mockLatestFile.setFileDate("2020-07-10T19:29:57.473Z");
        mockLatestFilesList.add(mockLatestFile);
        mockAddon.setLatestFiles(mockLatestFilesList);
        LocalDateTime expected = LocalDateTime.parse("2020-07-10T19:29:57.473");
        LocalDateTime actual = mockAddon.getLatestFileDate();

        assertEquals(expected, actual);
    }
}
