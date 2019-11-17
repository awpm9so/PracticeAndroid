package com.example.practiceandroid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class InfoMovie extends AppCompatActivity {

    TextView title;
    ImageView poster;
    TextView overview;
    TextView data_release;
    private static final String URL_IMAGE = "https://image.tmdb.org/t/p/w500";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_movie);

        title = findViewById(R.id.tv_title);
        poster = findViewById(R.id.iv_poster);
        overview = findViewById(R.id.tv_overview);
        data_release = findViewById(R.id.tv_release_date);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)){
            String textTitle = intent.getStringExtra(Intent.EXTRA_TEXT);
            title.setText(textTitle);
        }
        String poster_path = intent.getStringExtra("poster_path");
        String overview_text = intent.getStringExtra("overview");
        String data_release_text = intent.getStringExtra("data_release");
        overview.setText(overview_text);
        data_release.setText("Дата выхода: " + data_release_text);
        Picasso.get().load(URL_IMAGE+poster_path).error(R.drawable.ic_warning).resize(252,400).centerCrop().into(poster);

        if(!isOnline(this)){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Нет подключения к Интернету", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
