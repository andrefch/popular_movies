package andrefch.udacity.com.br.popularmovies.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Author: andrech
 * Date: 21/12/17
 */

public class NetworkUtils {

    private NetworkUtils() {

    }

    public static JSONObject getJsonFromUrl(final URL url) throws IOException, JSONException {
        final String json = getResponseFromUrl(url);
        return new JSONObject(json);
    }

    private static String getResponseFromUrl(final URL url) throws IOException {
        if (url == null) {
            return  null;
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            }

            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
