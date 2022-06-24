package fr.cefim.android.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Objects;

import fr.cefim.android.R;
import fr.cefim.android.adapters.SearchAdapter;
import fr.cefim.android.databinding.ActivityMovieBinding;
import fr.cefim.android.models.MovieGson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {

    private ActivityMovieBinding binding;
    private TextView mTextViewMovieTitle;
    private OkHttpClient mOkHttpClient;
    private LinearLayout mLinearLayoutMovie;
    private ProgressBar mProgressBarMovie;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mLinearLayoutMovie = findViewById(R.id.linear_layout_movie);
        mProgressBarMovie = findViewById(R.id.progress_bar_movie);
        Intent intent = getIntent();
        String movieId = intent.getStringExtra(SearchAdapter.EXTRA_MOVIE_ID);
        System.out.println(":::::::::::::::::::::::::");
        System.out.println(movieId);
        System.out.println(":::::::::::::::::::::::::");
        openMovieActivity(movieId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openMovieActivity(String pMovieId) {
        if (isNetworkConnected()) {
            System.out.println("Yes, j'ai internet !!!");
            mLinearLayoutMovie.setVisibility(View.INVISIBLE);
            mProgressBarMovie.setVisibility(View.VISIBLE);
            callAPI(pMovieId);
        } else {
            System.out.println("Noooonnnnnnnnnnn, internet est mort !!!");
            mLinearLayoutMovie.setVisibility(View.INVISIBLE);
            mProgressBarMovie.setVisibility(View.INVISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Aucune connexion internet !!!");
            builder.setMessage("Voulez-vous ouvrir les paramètres de connexion ?");
            builder.setPositiveButton(android.R.string.ok,
                    (dialog, id) -> {
                        Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                        startActivity(panelIntent);
                    });
            builder.setNegativeButton(android.R.string.cancel,
                    (dialog, id) -> {
                        Intent mainActivityIntent = new Intent(this, MainActivity.class);
                        startActivity(mainActivityIntent);
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        Network currentNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);
        return currentNetwork != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    private void callAPI(String pMovieId) {
        String omdbapi = "http://www.omdbapi.com/";
        String apiKey = "f2e8e55c";
        String movieId = pMovieId.trim().toLowerCase().replaceAll(" ", "_");
        mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(String.format("%s?apikey=%s&i=%s&plot=full", omdbapi, apiKey, movieId)).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String stringJson = Objects.requireNonNull(response.body()).string();
                    runOnUiThread(() -> {
                        Gson gson = new Gson();
                        MovieGson movieGson = gson.fromJson(stringJson, MovieGson.class);
                        updateUI(movieGson);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
    }

    public void onClickSeeMoreOrLess(View v) {
        TextView textViewMovieSynopsis = findViewById(R.id.text_view_movie_synopsis);
        TextView textViewSynopsisSeeMoreOrLess = findViewById(R.id.text_view_synopsis_see_more_or_less);
        if (textViewMovieSynopsis.getMaxLines() == 3) {
            textViewMovieSynopsis.setMaxLines(Integer.MAX_VALUE);
            textViewSynopsisSeeMoreOrLess.setText(R.string.movie_see_less);
        } else {
            textViewMovieSynopsis.setMaxLines(3);
            textViewSynopsisSeeMoreOrLess.setText(R.string.movie_see_more);
        }
    }

    public void updateUI(MovieGson movie) {
        mProgressBarMovie.setVisibility(View.INVISIBLE);
        mLinearLayoutMovie.setVisibility(View.VISIBLE);

        ImageView imageViewMoviePoster = findViewById(R.id.image_view_movie_poster);
        Picasso.get().load(movie.getPoster()).into(imageViewMoviePoster);

        TextView textViewMovieTitle = findViewById(R.id.text_view_movie_title);
        textViewMovieTitle.setText(movie.getTitle());

        TextView textViewMovieReleaseDate = findViewById(R.id.text_view_movie_release_date);
        textViewMovieReleaseDate.setText(movie.getReleased());

        TextView textViewMovieGenres = findViewById(R.id.text_view_movie_genres);
        textViewMovieGenres.setText(movie.getGenre());

        TextView textViewMovieSynopsis = findViewById(R.id.text_view_movie_synopsis);
        textViewMovieSynopsis.setText(movie.getPlot());

        TextView textViewMovieDirector = findViewById(R.id.text_view_movie_director);
        textViewMovieDirector.setText(movie.getDirector());

        TextView textViewMovieActors = findViewById(R.id.text_view_movie_actors);
        textViewMovieActors.setText(movie.getActors());

        TextView textViewMovieAwards = findViewById(R.id.text_view_movie_awards);
        textViewMovieAwards.setText(movie.getAwards());
    }

//    public Movie updateMovieFromJsonString(String jsonString) {
//        Movie movie = new Movie();
//        JSONObject json;
//        try {
//            json = new JSONObject(jsonString);
//            movie.setPoster(json.getString("Poster"));
//            movie.setTitle(json.getString("Title"));
//            movie.setReleaseDate(json.getString("Released"));
//            movie.setGenres(json.getString("Genre"));
//            movie.setSynopsis(json.getString("Plot"));
//            movie.setDirector(json.getString("Director"));
//            movie.setActors(json.getString("Actors"));
//            movie.setAwards(json.getString("Awards"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return movie;
//    }

}