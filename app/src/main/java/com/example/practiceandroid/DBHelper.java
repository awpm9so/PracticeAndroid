package com.example.practiceandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "MovieDB";
    public static final String TABLE_POPULARMOVIE = "PopularMovieTable";


    public static final String KEY_ID = "_id";
    public static final String ID_MOVIE = "id";
    public static final String KEY_VOTECOUNT= "voteCount";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_VOTEAVARAGE = "voteAverage";
    public static final String KEY_POPULARITY = "popularity";
    public static final String KEY_POSTERPATH = "posterPath";
    public static final String KEY_OROGINALLANGUAGE = "originalLanguage";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ORIGINALTITLE = "originalTitle";
    public static final String KEY_GENREIDS = "genreIds";
    public static final String KEY_BACKDROPPATH = "backdropPath";
    public static final String KEY_ADULT = "adult";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_RELEASEDATE = "releaseDate";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_POPULARMOVIE + "(" +
                KEY_ID   + " integer primary key autoincrement," +
                ID_MOVIE + " int," +
                KEY_TITLE + " text,"  +
                KEY_POPULARITY + " double," +
                KEY_VOTECOUNT + " int," +
                KEY_VOTEAVARAGE + " double,"+
                KEY_ORIGINALTITLE   + " text,"+
                KEY_OROGINALLANGUAGE + " text,"  +
                KEY_GENREIDS +  " int,"+
                KEY_OVERVIEW + " text,"+
                KEY_RELEASEDATE + " text,"+
                KEY_VIDEO + " bool,"+
                KEY_ADULT + " bool," +
                KEY_POSTERPATH  + " text,"+
                KEY_BACKDROPPATH  + " text"+
                ")");

        Log.d("myLogs", "BD CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_POPULARMOVIE);

        onCreate(db);
    }

}
