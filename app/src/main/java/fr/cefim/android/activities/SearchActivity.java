package fr.cefim.android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import fr.cefim.android.R;
import fr.cefim.android.adapters.SearchAdapter;
import fr.cefim.android.models.MovieGson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private List<MovieGson> mMovieGsonList;
    private RecyclerView mRecyclerViewMovie;
    private RecyclerView.Adapter mAdapter;
    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mMovieGsonList = new ArrayList<>();
//        Gson gson = new Gson();
//        MovieGson movieGson1 = gson.fromJson(movie1, MovieGson.class);
//        mMovieGsonList.add(movieGson1);
//        MovieGson movieGson2 = gson.fromJson(movie2, MovieGson.class);
//        mMovieGsonList.add(movieGson2);
//        MovieGson movieGson3 = gson.fromJson(movie3, MovieGson.class);
//        mMovieGsonList.add(movieGson3);
//        MovieGson movieGson4 = gson.fromJson(movie4, MovieGson.class);
//        mMovieGsonList.add(movieGson4);

        mRecyclerViewMovie = findViewById(R.id.recycler_view_movie);
        mRecyclerViewMovie.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new SearchAdapter(this, mMovieGsonList);
        mRecyclerViewMovie.setAdapter(mAdapter);

    }

    public void onClickSearchMovie(View v) {
        EditText editText = findViewById(R.id.edit_text_movie_search);
        String movieTitle = editText.getText().toString();
        callAPI(movieTitle);
    }

    private void callAPI(String pMovieTitle) {
        String omdbapi = "http://omdbapi.com/";
        String apiKey = "f2e8e55c";
        String movieTitle = pMovieTitle.trim().toLowerCase().replaceAll(" ", "_");
        String searchUrl = String.format("%s?apikey=%s&s=%s&plot=full", omdbapi, apiKey, movieTitle);
        mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(searchUrl).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBodyJson = Objects.requireNonNull(response.body()).string();
                    mMovieGsonList.clear();
                    runOnUiThread(() -> {
                        try {
                            JSONObject responseJsonObject = new JSONObject(responseBodyJson);
                            JSONArray searchResultList = responseJsonObject.getJSONArray("Search");
                            for (int i = 0; i < searchResultList.length(); i++) {
                                JSONObject json = searchResultList.getJSONObject(i);
                                MovieGson movieGson = new MovieGson();
                                movieGson.setPoster(json.getString("Poster"));
                                movieGson.setTitle(json.getString("Title"));
                                movieGson.setReleased(json.getString("Year"));
                                movieGson.setImdbID(json.getString("imdbID"));
                                mMovieGsonList.add(movieGson);
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
    }

    String movie1 = "{\n" +
            "\"Title\": \"Toto le héros\",\n" +
            "\"Released\": \"1991\",\n" +
            "\"imdbID\": \"tt0103105\",\n" +
            "\"Type\": \"movie\",\n" +
            "\"Poster\": \"https://m.media-amazon.com/images/M/MV5BZTkzZmRhYWYtMjBmMC00N2U4LTlmNWYtZDM3NTY3NDE4OTk0XkEyXkFqcGdeQXVyNjMwMjk0MTQ@._V1_SX300.jpg\"\n" +
            "}";
    String movie2 = "{\n" +
            "\"Title\": \"Totò, Peppino e la... malafemmina\",\n" +
            "\"Released\": \"1956\",\n" +
            "\"imdbID\": \"tt0049866\",\n" +
            "\"Type\": \"movie\",\n" +
            "\"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjczOTFiOTEtNzFlOC00NDhjLWJkZmYtOTkxNzk2YjY1Nzg0XkEyXkFqcGdeQXVyMTQ3Njg3MQ@@._V1_SX300.jpg\"\n" +
            "}";
    String movie3 = "{\n" +
            "\"Title\": \"Totò diabolicus\",\n" +
            "\"Released\": \"1962\",\n" +
            "\"imdbID\": \"tt0056604\",\n" +
            "\"Type\": \"movie\",\n" +
            "\"Poster\": \"https://m.media-amazon.com/images/M/MV5BMGU5Y2FlYzEtOWQ4NS00YzAwLWFmYTktYmUwYzE3YTlmNGVmL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyNzA0ODU0NDI@._V1_SX300.jpg\"\n" +
            "}";
    String movie4 = "{\n" +
            "\"Title\": \"Toto: Africa\",\n" +
            "\"Released\": \"1982\",\n" +
            "\"imdbID\": \"tt6730638\",\n" +
            "\"Type\": \"movie\",\n" +
            "\"Poster\": \"https://m.media-amazon.com/images/M/MV5BZmIwMTQ2NWEtNzhmNC00YTliLTkxNGYtNzg3NmRmZTU0Y2JhXkEyXkFqcGdeQXVyNzA5MzkyOTM@._V1_SX300.jpg\"\n" +
            "}";


}