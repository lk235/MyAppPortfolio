package com.example.android.myappportfolio.topMovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;

import com.example.android.myappportfolio.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lk235 on 2017/4/11.
 */

public class FetchMovieTask extends AsyncTask<String, Void, MovieLab> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private static final String COLLECT = "收藏";
    private static final String strSeparator = ",";
    private final Context mContext;
   // private MovieAdapter mMovieAdapter;
    private MovieLab mMovieLab;
    private String mCategroySetting ;

    private final String TAG = "FetchMovieData";
    private final String MOVIE_URL = "http://api.themoviedb.org/3/movie";
    private final String IMAGE_URL = "https://image.tmdb.org/t/p/w185";
    private final String VOTE = "VOTE: ";
    private final String API_KEY = "api_key";
    private final String LANGUAGE = "language";
    String movieJsonStr = null;
    String apiKey = "3ec36d13c40b8c13f44a956ac6b7f785";
    String language = "zh";


    public FetchMovieTask(Context context, MovieLab movieLab){
        mContext = context;
        //mMovieAdapter = moiveAdapter;
        mMovieLab = movieLab;

    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        //get sharedPreferences here
//
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//
//        String key = Resources.getSystem().getString(R.string.pref_sort_key);
//        String value = Resources.getSystem().getString(R.string.pref_sort_default);
//        mCategroySetting = sharedPreferences.getString(key, value);
//
//    }



    protected MovieLab doInBackground(String... queryType) {

        ArrayList<String[]> stringArrayList = null;

        Uri uri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(queryType[0])
                .appendQueryParameter(LANGUAGE, language)
                .appendQueryParameter(API_KEY, apiKey)
                .build();

        getDataFromHttp(uri);


        try

        {
            stringArrayList = getMovieDataFromJson(movieJsonStr);
        } catch (JSONException js) {
            Log.e(TAG, "JSON ERROR");
        }

        mCategroySetting = MainActivity.mLastCateGroySetting;

                mMovieLab.deleteMovies(mCategroySetting);


        String movieId;
        for (int i = 0; i < stringArrayList.size(); i++)

        {
            Movie movie = new Movie();
            movie.setCategroy(mCategroySetting);
            movie.setTitle(stringArrayList.get(i)[0]);
            movie.setImageUrl(IMAGE_URL + stringArrayList.get(i)[1]);
            movie.setRelease_date(stringArrayList.get(i)[2]);
            movie.setVote(VOTE + stringArrayList.get(i)[3]);
            movie.setOverview(stringArrayList.get(i)[4]);
            movie.setColledted(COLLECT);
            movieId = stringArrayList.get(i)[5];
            movie.setRuntime(getRunTimeFromUri(movieId));
            ArrayList<String[]> retTrailer = getTrailerFromUri(movieId);
            String [] nameStr = new String[retTrailer.size()];
            String [] urlStr = new String[retTrailer.size()];
           for(int j =0; j < retTrailer.size(); j++){
               nameStr[j] = retTrailer.get(j)[0];
               Log.i("nameStr", nameStr[j]);
               Log.i("HERE2","HERE2");
               urlStr[j] = retTrailer.get(j)[1];


           }
           movie.setTrailerName(convertArrayToString(nameStr));
            movie.setTrailerUrl(convertArrayToString(urlStr));



            Log.e(TAG,stringArrayList.get(i)[1] );

            mMovieLab.addMovie(movie);


        }





        return mMovieLab;
    }



    private ArrayList<String[]> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.

        final String OWM_RESULTS = "results";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_OVERVIEW = "overview";
        final String OWM_TITLE = "title";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWN_ID = "id";

        ArrayList<String[]> resultStrs = new ArrayList<>();


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);


        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject singleMovie = movieArray.getJSONObject(i);

            String[] moiveItem = new String[]{

                    singleMovie.getString(OWM_TITLE),
                    singleMovie.getString(OWM_POSTER_PATH),
                    singleMovie.getString(OWM_RELEASE_DATE),
                    singleMovie.getString(OWM_VOTE_AVERAGE),
                    singleMovie.getString(OWM_OVERVIEW),
                    singleMovie.getString(OWN_ID)};

            resultStrs.add(moiveItem);

        }

        return resultStrs;

    }

    private String getRunTimeFromUri (String movieId){


        String movieRunTime = null;
        Uri uri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(movieId)
                .appendQueryParameter(API_KEY, apiKey)
                .build();

        getDataFromHttp(uri);

        final String OWM_RUNTIME = "runtime";

        try

        {
            JSONObject jsonString = new JSONObject(movieJsonStr);
            int runtime = jsonString.getInt(OWM_RUNTIME);
            int runHour = runtime / 60 ;
            int runMin = runtime % 60;
            movieRunTime = "RUNTIME: " +  runHour + " h " + runMin + " m";


        } catch (JSONException js) {
            Log.e(TAG, "JSON ERROR");
        }



        return movieRunTime;

    }


    private ArrayList<String[]> getTrailerFromUri (String movieId){
        final String VIDEOS = "videos";
        final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";


        ArrayList<String[]> movieTrailers = new ArrayList<>();
        String trailerNmae;
        String trailerUrl;
        Uri uri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(VIDEOS)
                .appendQueryParameter(API_KEY, apiKey)
                .build();

        getDataFromHttp(uri);


        final String OWM_NAME = "name";
        final String OWM_RESULTS = "results";
        final String OWM_KEY = "key";

        try

        {
            JSONObject jsonString = new JSONObject(movieJsonStr);
            JSONArray jsonArray = jsonString.getJSONArray(OWM_RESULTS);
            Log.i("HERE","HERE");


            for (int i = 0; i < jsonArray.length(); i++ ){

                trailerNmae = jsonArray.getJSONObject(i).getString(OWM_NAME);

                trailerUrl = YOUTUBE_URL + jsonArray.getJSONObject(i).getString(OWM_KEY);
                movieTrailers.add(new String[] {trailerNmae, trailerUrl});




            }



        } catch (JSONException js) {
            Log.e(TAG, "JSON ERROR");
        }




       Log.i("TRAILER", movieTrailers.get(0)[0]);
        return  movieTrailers;



    }

    private void getDataFromHttp( Uri uri) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try

        {


            URL url = new URL(uri.toString());
            Log.i(TAG, url + "");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            Log.i(TAG, "code= " + responseCode);
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null) {

                movieJsonStr = null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {

                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0) {

                movieJsonStr = null;
            }
            movieJsonStr = stringBuffer.toString();

        } catch (
                IOException ioe)

        {
            movieJsonStr = null;
            Log.i(TAG, "" + ioe);
        } finally

        {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }

        }
    }

    public static String convertArrayToString(String[] array){


        String str = "";
        for (int i = 0; i<array.length; i++) {
            str = str+array[i];
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }

}
