package andrefch.udacity.com.br.popularmovies.ui.detail;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import andrefch.udacity.com.br.popularmovies.R;
import andrefch.udacity.com.br.popularmovies.model.Movie;
import andrefch.udacity.com.br.popularmovies.utilities.DateUtils;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mOriginalTextView;
    private RatingBar mAverageVoteRatingBar;
    private TextView mAverageVoteTextView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        setupToolbar();
        loadMovie(savedInstanceState);
        initViews();
        showMovie();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadMovie(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(EXTRA_MOVIE);
        }

        if ((mMovie == null) && (getIntent() != null)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        }
    }

    private void initViews() {
        mPosterImageView = findViewById(R.id.iv_poster);
        mTitleTextView = findViewById(R.id.tv_title);
        mOriginalTextView = findViewById(R.id.tv_original_title);
        mAverageVoteRatingBar = findViewById(R.id.rt_average_vote);
        mAverageVoteTextView = findViewById(R.id.tv_average_vote);
        mReleaseDateTextView = findViewById(R.id.tv_release_date_value);
        mOverviewTextView = findViewById(R.id.tv_overview);
    }

    private void showMovie() {
        if (mMovie == null) {
            return;
        }
        Picasso.with(this)
                .load(mMovie.getCompletePosterURL())
                .into(mPosterImageView);
        mTitleTextView.setText(mMovie.getTitle() != null
                ? mMovie.getTitle() : "");
        mOriginalTextView.setText(String.format("(%s)",
                mMovie.getOriginalTitle() != null
                        ? mMovie.getOriginalTitle() : ""));
        mAverageVoteRatingBar.setRating((float) (Math.floor(mMovie.getVoteAverage()) / 2));
        mAverageVoteTextView.setText(getAverageVoteValue());
        mReleaseDateTextView.setText(DateUtils.dateToString(this,
                mMovie.getReleaseDate()));
        mOverviewTextView.setText(mMovie.getOverview() != null
                ? mMovie.getOverview() : "");
    }

    private CharSequence getAverageVoteValue() {
        final String value = getString(R.string.detail_movie_average_vote_value, mMovie.getVoteAverage());
        final SpannableStringBuilder builder = new SpannableStringBuilder(value);
        builder.setSpan(new StyleSpan(Typeface.BOLD),
                value.length() - 2,
                value.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }
}
