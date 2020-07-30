package client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.io.IOException;
import java.util.concurrent.CompletionException;

/**
 * Custom mapper class to map JSON string to Addon object.
 */
public class AddonMapper extends com.fasterxml.jackson.databind.ObjectMapper {

    /**
     * Matches the contents of a JSON string to an addon object.
     *
     * @param content The JSON string.
     * @return The resulting addon object from the JSON string.
     */
    Addon readValue(String content) {
        try {
            this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return this.readValue(content, new TypeReference<Addon>() {
            });
        } catch (IOException ioe) {
            throw new CompletionException(ioe);
        }
    }
}
