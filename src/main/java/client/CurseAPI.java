package client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * Handles the api requests for cure addon data.
 */
public class CurseAPI {

    private static final String CURSE_API_URI = "https://addons-ecs.forgesvc.net/api/v2/addon/";
    private HttpClient client;

    public CurseAPI() {
        client = HttpClient.newHttpClient();
    }

    /**
     * HttpRequest to get addon data.
     *
     * @param id the id of an addon.
     * @return addon object containing the relevant data.
     * @throws IOException
     * @throws InterruptedException
     */
    public Addon getAddon(String id) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .header("accept", "application/json")
                                         .uri(URI.create(CURSE_API_URI + id))
                                         .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Addon addon = mapper.readValue(response.body(), new TypeReference<Addon>() {
        });
        return addon;
    }

}
