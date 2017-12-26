package andrefch.udacity.com.br.popularmovies.ui.list;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import andrefch.udacity.com.br.popularmovies.R;
import andrefch.udacity.com.br.popularmovies.model.Movie;
import andrefch.udacity.com.br.popularmovies.repository.MovieOrderBy;
import andrefch.udacity.com.br.popularmovies.repository.MovieRepository;
import andrefch.udacity.com.br.popularmovies.repository.MovieRepositoryException;
import andrefch.udacity.com.br.popularmovies.ui.detail.DetailMovieActivity;

public class ListMovieActivity extends AppCompatActivity implements MovieAdapter.OnMovieSelectedListener {

    private static final String EXTRA_LIST_MOVIES = "EXTRA_LIST_MOVIES";
    private static final String EXTRA_ORDER_BY = "EXTRA_ORDER_BY";

    private RecyclerView mMoviesRecyclerView;
    private View mLoadingStateView;
    private View mErrorStateView;

    private MovieAdapter mAdapter;
    private MovieTask mMovieTask;

    @MovieOrderBy
    private String mOrderBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);

        initViews();
        setupRecyclerView();

        mOrderBy = MovieRepository.EXTRA_ORDER_BY_POPULAR;

        if (savedInstanceState != null) {
            mOrderBy = savedInstanceState.getString(EXTRA_ORDER_BY, mOrderBy);

            ArrayList<Movie> movies = savedInstanceState
                    .getParcelableArrayList(EXTRA_LIST_MOVIES);
            mAdapter.setMovies(movies);
        }

        if (mAdapter.getMovies() == null) {
            loadMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_ORDER_BY, mOrderBy);
        if ((mAdapter != null) && (mAdapter.getMovies() != null)) {
            outState.putParcelableArrayList(EXTRA_LIST_MOVIES,
                    mAdapter.getMovies());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.list_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_change_order_popular:
                mOrderBy = MovieRepository.EXTRA_ORDER_BY_POPULAR;
                loadMovies();
                return true;
            case R.id.action_change_order_top_rated:
                mOrderBy = MovieRepository.EXTRA_ORDER_BY_TOP_RATED;
                loadMovies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initViews() {
        mMoviesRecyclerView = findViewById(R.id.movies_recyclerview);
        mLoadingStateView = findViewById(R.id.movies_loading_state);
        mErrorStateView = findViewById(R.id.movies_error_state);
    }

    private void setupRecyclerView() {
        final GridLayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.number_of_columns),
                GridLayoutManager.VERTICAL,
                false);
        mMoviesRecyclerView.setLayoutManager(layoutManager);

        mMoviesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MovieAdapter();
        mAdapter.setListener(this);
        mMoviesRecyclerView.setAdapter(mAdapter);
    }

    private void loadMovies() {
        if ((mMovieTask != null) && (mMovieTask.getStatus() == AsyncTask.Status.RUNNING)) {
            mMovieTask.cancel(true);
            mMovieTask = null;
        }
        mMovieTask = new MovieTask(mOrderBy, 1);
        mMovieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void changeLayouts(@IdRes int visibleViewResId) {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingStateView.setVisibility(View.INVISIBLE);
        mErrorStateView.setVisibility(View.INVISIBLE);
        switch (visibleViewResId) {
            case R.id.movies_loading_state:
                mLoadingStateView.setVisibility(View.VISIBLE);
                break;
            case R.id.movies_error_state:
                mErrorStateView.setVisibility(View.VISIBLE);
                break;
            default:
                mMoviesRecyclerView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showData(ArrayList<Movie> movies) {
        if (mAdapter != null) {
            mAdapter.setMovies(movies);
        }
        changeLayouts(R.id.movies_recyclerview);
    }

    private void showError() {
        changeLayouts(R.id.movies_error_state);
    }

    private void showLoading() {
        changeLayouts(R.id.movies_loading_state);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        final Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    private class MovieTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @MovieOrderBy
        private final String orderBy;
        private final int page;

        MovieTask(@MovieOrderBy String orderBy, int page) {
            super();
            this.orderBy = orderBy;
            this.page = page;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            ArrayList<Movie> movies;

            final MovieRepository repository = new MovieRepository();
            try {
                movies = repository.getMovies(orderBy, page);
            } catch (MovieRepositoryException e) {
                e.printStackTrace();
                movies = null;
            }

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            if (movies != null) {
                showData(movies);
            } else {
                showError();
            }
        }
    }
}
