package fr.cefim.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.InetSocketAddress;
import java.util.List;

import fr.cefim.android.R;
import fr.cefim.android.activities.MovieActivity;
import fr.cefim.android.models.MovieGson;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context mContext;
    private List<MovieGson> mMovieGsonList;
    public static final String EXTRA_MOVIE_ID = "movie_id";

    public SearchAdapter(Context pContext, List<MovieGson> pMovieGsonList) {
        mContext = pContext;
        mMovieGsonList = pMovieGsonList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieGson movieGson = mMovieGsonList.get(position);
        Picasso.get().load(movieGson.getPoster()).into(holder.mImageViewMoviePoster);
        holder.mTextViewMovieTitle.setText(movieGson.getTitle());
        holder.mTextViewMovieYear.setText(movieGson.getReleased());
        holder.mMovieGson = movieGson;
    }

    @Override
    public int getItemCount() {
        return mMovieGsonList.size();
    }

    // Classe holder qui contient la vue d’un item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImageViewMoviePoster;
        public TextView mTextViewMovieTitle;
        public TextView mTextViewMovieYear;
        public MovieGson mMovieGson;

        public ViewHolder(View view) {
            super (view);
            mImageViewMoviePoster = (ImageView) view.findViewById(R.id.image_view_movie_poster);
            mTextViewMovieTitle = (TextView) view.findViewById(R.id.text_view_movie_title);
            mTextViewMovieYear = (TextView) view.findViewById(R.id.text_view_movie_year);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, MovieActivity.class);
            intent.putExtra(EXTRA_MOVIE_ID, mMovieGson.getImdbID());
            mContext.startActivity(intent);
        }
    }
}
