package com.example.practiceandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "myLogs";
    ArrayList<Result> movieList = new ArrayList<>();
    PostsAdapter adapter;
    RecyclerView recyclerView;
    DBHelper dbHelper;
    ContentValues contentValues;
    SQLiteDatabase database;
    Cursor cursor;
    boolean k = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerView = (RecyclerView) findViewById(R.id.posts_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        dbHelper = new DBHelper(this);
        contentValues = new ContentValues();
        database = dbHelper.getWritableDatabase();
        cursor = database.query(DBHelper.TABLE_POPULARMOVIE, null, null, null, null, null, null);

        adapter = new PostsAdapter(this, movieList);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        MovieApi movieApi = retrofit.create(MovieApi.class);



        if (isOnline(this) ){
            database.delete(DBHelper.TABLE_POPULARMOVIE, null, null);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Нет подключения к Интернету", Toast.LENGTH_LONG);
            toast.show();
        }


            for (int page = 1; page <= 5; page++) {

                Call<MovieMain> movie = movieApi.getMovies(page);

                movie.enqueue(new Callback<MovieMain>() {

                    @Override
                    public void onResponse(Call<MovieMain> call, Response<MovieMain> response) {


                        if (response.isSuccessful() && response.body() != null) {
                            movieList.addAll(response.body().getResults());
                            recyclerView.getAdapter().notifyDataSetChanged();

                            if (k) {
                                for (int i = 0; i < response.body().getResults().size(); i++) {
                                    contentValues.put(DBHelper.ID_MOVIE, response.body().getResults().get(i).getId());
                                    contentValues.put(DBHelper.KEY_TITLE, response.body().getResults().get(i).getTitle());
                                    contentValues.put(DBHelper.KEY_POPULARITY, response.body().getResults().get(i).getPopularity());
                                    contentValues.put(DBHelper.KEY_VOTECOUNT, response.body().getResults().get(i).getVoteCount());
                                    contentValues.put(DBHelper.KEY_VOTEAVARAGE, response.body().getResults().get(i).getVoteAverage());
                                    contentValues.put(DBHelper.KEY_ORIGINALTITLE, response.body().getResults().get(i).getOriginalTitle());
                                    contentValues.put(DBHelper.KEY_OROGINALLANGUAGE, response.body().getResults().get(i).getOriginalLanguage());
                                    contentValues.put(DBHelper.KEY_GENREIDS, response.body().getResults().get(i).getGenreIds().toString());
                                    contentValues.put(DBHelper.KEY_OVERVIEW, response.body().getResults().get(i).getOverview());
                                    contentValues.put(DBHelper.KEY_RELEASEDATE, response.body().getResults().get(i).getReleaseDate());
                                    contentValues.put(DBHelper.KEY_VIDEO, response.body().getResults().get(i).getVideo());
                                    contentValues.put(DBHelper.KEY_ADULT, response.body().getResults().get(i).getAdult());
                                    contentValues.put(DBHelper.KEY_POSTERPATH, response.body().getResults().get(i).getPosterPath());
                                    contentValues.put(DBHelper.KEY_BACKDROPPATH, response.body().getResults().get(i).getBackdropPath());
                                    database.insert(DBHelper.TABLE_POPULARMOVIE, null, contentValues);
                                    k = false;
                                }
                            }


                        } else {
                            Log.d(TAG, "response code " + response.code());
                        }

                    }
                    @Override
                    public void onFailure(Call<MovieMain> call, Throwable t) {
                        if (cursor.moveToFirst()) {
                            int id = cursor.getColumnIndex(DBHelper.ID_MOVIE);
                            int title = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                            int overview = cursor.getColumnIndex(DBHelper.KEY_OVERVIEW);
                            int data_release = cursor.getColumnIndex(dbHelper.KEY_RELEASEDATE);

                            do {
                                Result result = new Result();
                                result.setTitle(cursor.getString(title));
                                result.setOverview(cursor.getString(overview));
                                result.setReleaseDate(cursor.getString(data_release));
                                movieList.add(result);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            } while (cursor.moveToNext());
                        } else
                            Log.d(TAG,"0 rows");

                    }
                });
            }
    }




    public interface MovieApi {
        @GET("popular?api_key=23c8ac4e2c71a50cc524672c8dfcd08a&language=ru")
        Call <MovieMain> getMovies(@Query("page") int page);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)item.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
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


