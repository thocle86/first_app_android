package fr.cefim.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fr.cefim.android.R;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewTitlePage;
    private Button mButtonSearch;
    private TextView mTextViewMovieTitle;
    public static final String EXTRA_MOVIE_TITLE = "movie_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewTitlePage = (TextView) findViewById(R.id.text_view_page_title);
        mTextViewTitlePage.setText(R.string.title_page);
        Toast.makeText(this, mTextViewTitlePage.getText().toString(), Toast.LENGTH_SHORT).show();

//        mButtonSearch = (Button) findViewById(R.id.button_searh);
//        View.OnClickListener onClickListener = view -> Log.d("", "Clic sur le boutton Rechercher");
//        mButtonSearch.setOnClickListener(onClickListener);

    }

    public void onClickMovie(View v) {
        LinearLayout parentLinearLayout = (LinearLayout) findViewById(v.getId());
        LinearLayout childLinearLayout = (LinearLayout) parentLinearLayout.getChildAt(1);
        mTextViewMovieTitle = (TextView) childLinearLayout.getChildAt(0);
        Toast.makeText(this, mTextViewMovieTitle.getText().toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MovieActivity.class);
        String movieTitle = mTextViewMovieTitle.getText().toString();
        intent.putExtra(EXTRA_MOVIE_TITLE, movieTitle);
        startActivity(intent);
    }

    public void onClickSearch(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

}