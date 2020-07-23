package client.exceptions;

/**
 * Exception to handle unsupported Addons.
 */
public class UnsupportedAddonException extends Exception {

    private static final long serialVersionUID = -1274480633298615857L;

    public UnsupportedAddonException(String message) {
        super(message);
    }
}
