package andrefch.udacity.com.br.popularmovies.utilities;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: andrech
 * Date: 21/12/17
 */

public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    private DateUtils() {

    }

    public static String dateToString(Context context, final Date value) {
        return DateFormat.getDateFormat(context)
                .format(value);
    }

    public static Date stringToDate(final String format, final String value) {
        if (TextUtils.isEmpty(format) || TextUtils.isEmpty(value)) {
            return null;
        }

        final SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            Log.e(TAG, "Failed to convert string to date.", e);
            return null;
        }
    }
}
