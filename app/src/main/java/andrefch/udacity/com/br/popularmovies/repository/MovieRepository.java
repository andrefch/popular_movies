package andrefch.udacity.com.br.popularmovies.repository;

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import andrefch.udacity.com.br.popularmovies.BuildConfig;
import andrefch.udacity.com.br.popularmovies.model.Movie;
import andrefch.udacity.com.br.popularmovies.utilities.NetworkUtils;

/**
 * Author: andrech
 * Date: 25/12/17
 */

public class MovieRepository {

    public static final String EXTRA_ORDER_BY_POPULAR = "popular";
    public static final String EXTRA_ORDER_BY_TOP_RATED = "top_rated";

    private static final String QUERY_PARAM_API_KEY = "api_key";
    private static final String QUERY_PARAM_PAGE = "page";
    private static final String QUERY_PARAM_LANGUAGE = "language";

    private static final String PATH_MOVIE = "movie";

    public ArrayList<Movie> getMovies(@MovieOrderBy String orderBy, int page) throws MovieRepositoryException {
        final ArrayList<Movie> movies;
        try {
            final URL url = buildMovieURL(orderBy, page);
            final JSONObject json = NetworkUtils.getJsonFromUrl(url);

            movies = new ArrayList<>();

            final JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                final JSONObject jsonMovie = results.getJSONObject(i);
                final Movie movie = new Movie(jsonMovie);
                movies.add(movie);
            }
        } catch (Exception e) {
            throw new MovieRepositoryException(e);
        }

        return movies;
    }

    private URL buildMovieURL(@MovieOrderBy String orderBy, int page) throws MalformedURLException {
        return new URL(Uri.parse(BuildConfig.SERVER_BASE_URL)
                .buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(orderBy)
                .appendQueryParameter(QUERY_PARAM_API_KEY, BuildConfig.SERVER_API_KEY)
                .appendQueryParameter(QUERY_PARAM_PAGE, String.valueOf(page))
                .appendQueryParameter(QUERY_PARAM_LANGUAGE, getLanguage())
                .build()
                .toString());
    }

    private String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        if (TextUtils.isEmpty(language)) {
            language = "en-us";
        }
        return language;
    }
}
