package andrefch.udacity.com.br.popularmovies.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import andrefch.udacity.com.br.popularmovies.R;
import andrefch.udacity.com.br.popularmovies.model.Movie;

/**
 * Author: andrech
 * Date: 19/12/17
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private OnMovieSelectedListener mListener;
    private ArrayList<Movie> mMovies;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_list, parent, false);
        return new MovieViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return mMovies != null ? mMovies.size() : 0;
    }

    private Movie getItem(final int position) {
        if ((position >= 0) && (position < getItemCount())) {
            return mMovies.get(position);
        }
        return null;
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void setListener(OnMovieSelectedListener listener) {
        mListener = listener;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mPosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            Picasso.with(itemView.getContext())
                    .load(movie.getCompletePosterURL())
                    .into(mPosterImageView);
        }

        @Override
        public void onClick(View view) {
            if (mListener == null) {
                return;
            }
            mListener.onMovieSelected(getItem(getAdapterPosition()));
        }
    }

    interface OnMovieSelectedListener {
        void onMovieSelected(Movie movie);
    }
}
