package com.bobdv.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class AppDataBase extends SQLiteOpenHelper {

    //DataBase Settings
    private SQLiteDatabase iDB = null;
    private Context iContext;
    private static final String iDBName = "favoriteMovies.sqlite";
    private String iDBPath;
    //DataBase Settings

    //DataBase Table
    private static final String Fav_Movies_Table = "Fav_Movies";


    //DataBase Table

    //DataBase Settings
    public AppDataBase(Context context) {
        super(context, iDBName, null, 1);

        iContext = context;
        iDBPath = "/data/data/" + iContext.getPackageName() + "/databases/";

        try {

            createAppDataBase();

        } catch (Exception ex) {

        }

    }

    private void createAppDataBase() throws IOException {
        boolean mAppDataBaseExist = checkAppDataBase();

        if (!mAppDataBaseExist) {
            try {

                copyDataBase();

            } catch (SQLiteException ex) {
                throw new Error("Error SQLiteException copying database");

            } catch (IOException e) {
                throw new Error("Error IOException copying database");
            }

            try {

                openDataBase();

            } catch (SQLiteException ex) {
                throw new Error("Error SQLiteException opening database");

            } catch (Exception e) {
                throw new Error("Error Exception opening database");
            }
        }
    }

    private boolean checkAppDataBase() {
        String mPath = iContext.getDatabasePath(iDBName).toString();

        try {

            iDB = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);

        } catch (Exception ex) {

            return false;

        }

        return iDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        File dbDir = new File(iDBPath);
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }

        //Open your local db as the input stream
        InputStream myInput = iContext.getAssets().open(iDBName);

        // Path to the just created empty db
        String outFileName = iDBPath + iDBName;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLiteException {
        //Open the database
        String myPath = iContext.getDatabasePath(iDBName).toString();

        if (iDB != null) {

            iDB.close();
            iDB = null;
            iDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        } else {

            iDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        }
    }

    public void closeDataBase() {

        if (iDB != null) {
            iDB.close();
            iDB = null;
        }

    }
    //DataBase Settings

    //Default Implemetntation
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void insertMovieDetails(String MovieID, String PosterPath, String MovieOrignalTitle,
                                   String mDate, String vote_average, String MovieOverView) {


        ContentValues cv = new ContentValues();

        cv.put("MovieID", MovieID);
        cv.put("PosterPath", PosterPath);
        cv.put("MovieOrignalTitle", MovieOrignalTitle);
        cv.put("mDate", mDate);
        cv.put("vote_average", vote_average);
        cv.put("MovieOverView", MovieOverView);


        iDB.insertWithOnConflict(Fav_Movies_Table, null, cv, iDB.CONFLICT_IGNORE);

        Log.d("Movie", "Insert successfully !! ");
    }

    public boolean CheckIfFavorite(String MovieId) {


        Cursor c = iDB.rawQuery("SELECT  MovieOrignalTitle FROM " + Fav_Movies_Table + " WHERE MovieID = "+MovieId, null);
        if (c.getCount() <= 0) {
            c.close();
            return false;

        }
        c.close();
        return true;
    }


    public boolean deleteFavorite(String MovieID)
    {
        return iDB.delete(Fav_Movies_Table, "MovieID" + "=" + MovieID, null) > 0;
    }
    ArrayList<String> arr_MovieID = new ArrayList<>();
    ArrayList<String> arr_PosterPath = new ArrayList<>();
    ArrayList<String> arr_MovieOrignalTitle = new ArrayList<>();
    ArrayList<String> arr_mDate = new ArrayList<>();
    ArrayList<String> arr_vote_average = new ArrayList<>();
    ArrayList<String> arr_MovieOverView = new ArrayList<>();

    public HashMap<String, ArrayList<String>> GetMovieDetails() {



        HashMap<String, ArrayList<String>> mResult = new HashMap<String, ArrayList<String>>();

        Cursor c = iDB.rawQuery("SELECT  * FROM " + Fav_Movies_Table, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                arr_MovieID.add(c.getString(c.getColumnIndex("MovieID")));
                arr_PosterPath.add(c.getString(c.getColumnIndex("PosterPath")));
                arr_MovieOrignalTitle.add(c.getString(c.getColumnIndex("MovieOrignalTitle")));
                arr_mDate.add(c.getString(c.getColumnIndex("mDate")));
                arr_vote_average.add(c.getString(c.getColumnIndex("vote_average")));
                arr_MovieOverView.add(c.getString(c.getColumnIndex("MovieOverView")));

            } while (c.moveToNext());

        }


        mResult.put("arr_MovieID", arr_MovieID);
        mResult.put("arr_PosterPath", arr_PosterPath);
        mResult.put("arr_MovieOrignalTitle", arr_MovieOrignalTitle);
        mResult.put("arr_mDate", arr_mDate);
        mResult.put("arr_vote_average", arr_vote_average);
        mResult.put("arr_MovieOverView", arr_MovieOverView);


        return mResult;

    }


}