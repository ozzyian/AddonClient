package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
     * Queries the API for an addon.
     *
     * @param id The id of an addon.
     * @return Reference to the api response.
     * @throws IOException
     * @throws InterruptedException
     */
    public CompletableFuture<Addon> getAddonData(String id) {
        AddonMapper addonMapper = new AddonMapper();
        HttpRequest request = HttpRequest.newBuilder().GET().header("accept", "application/json")
                .uri(URI.create(CURSE_API_URI + id)).build();

        CompletableFuture<Addon> response = client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body).thenApply(addonMapper::readValue);

        return response;
    }

    /**
     * Queries the API for all addons in a list.
     *
     * @param addons The list of addons to be queried.
     * @return List with reference to the responses.
     */
    public List<CompletableFuture<Addon>> getAddonsData(List<Addon> addons) {

        List<CompletableFuture<Addon>> requests = addons.stream().map(addon -> getAddonData(addon.getId() + ""))
                .collect(Collectors.toList());

        return requests;
    }

}
