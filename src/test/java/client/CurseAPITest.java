package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Does not do any real tests, practicing purposes.
 */
public class CurseAPITest {

    private static CompletableFuture<Addon> addonResponse;
    private static final int ADDON_ID = 326516;
    private static final String ADDON_NAME = "AtlasLootClassic";

    @BeforeAll
    static void setUp() {
        AddonMapper addonMapper = new AddonMapper();
        String response;
        try {
            response = TestUtil.readJsonResource("src/test/java/client/resources/json_response.json");
            addonResponse = CompletableFuture.completedFuture(addonMapper.readValue(response));

        } catch (IOException e) {
            fail("IOException was thrown");
            e.printStackTrace();
        }

    }

    @Test
    void getAddonDataTest() {
        CurseAPI apiMock = Mockito.mock(CurseAPI.class);
        when(apiMock.getAddonData("")).thenReturn(addonResponse);
        try {
            Addon response = apiMock.getAddonData(anyString()).get();
            assertNotNull(response);
            assertEquals(ADDON_ID, response.getId());
            assertEquals(ADDON_NAME, response.getName());
        } catch (InterruptedException e) {
            fail("Exception thrown");
            e.printStackTrace();
        } catch (ExecutionException e) {
            fail("Exception thrown");
            e.printStackTrace();
        }

    }

    @Test
    void getAddonsDataTest() {
        try {
            List<CompletableFuture<Addon>> mockListExpected = new ArrayList<>();
            mockListExpected.add(addonResponse);
            mockListExpected.add(addonResponse);
            CurseAPI apiMock = Mockito.mock(CurseAPI.class);
            when(apiMock.getAddonsData(anyList())).thenReturn(mockListExpected);
            List<CompletableFuture<Addon>> actualList = apiMock.getAddonsData(new ArrayList<Addon>());

            for (int i = 0; i < actualList.size(); i++) {
                Addon actual;
                actual = actualList.get(i).get();

                assertNotNull(actual);
                assertEquals(ADDON_ID, actual.getId());
                assertEquals(ADDON_NAME, actual.getName());

            }

        } catch (InterruptedException e) {
            fail("InterruptedException was thrown");
            e.printStackTrace();
        } catch (ExecutionException e) {
            fail("ExcecutionException was thrown");
            e.printStackTrace();
        }

    }

}
