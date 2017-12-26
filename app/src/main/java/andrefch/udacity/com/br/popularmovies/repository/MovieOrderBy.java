package andrefch.udacity.com.br.popularmovies.repository;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: andrech
 * Date: 25/12/17
 */

@StringDef({ MovieRepository.EXTRA_ORDER_BY_POPULAR, MovieRepository.EXTRA_ORDER_BY_TOP_RATED })
@Retention(RetentionPolicy.SOURCE)
public @interface MovieOrderBy {
}
