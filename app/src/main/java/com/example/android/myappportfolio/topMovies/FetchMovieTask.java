package com.example.android.myappportfolio.topMovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lk235 on 2017/4/11.
 */

public class FetchMovieTask extends AsyncTask<String, Void, MovieLab> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private final Context mContext;
   // private MovieAdapter mMovieAdapter;
    private MovieLab mMovieLab;

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



    protected MovieLab doInBackground(String... queryType) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        ArrayList<String[]> stringArrayList = null;

        try

        {
            Uri uri = Uri.parse(MOVIE_URL).buildUpon()
                    .appendPath(queryType[0])
                    .appendQueryParameter(LANGUAGE, language)
                    .appendQueryParameter(API_KEY, apiKey)
                    .build();

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


        try

        {
            stringArrayList = getMovieDataFromJson(movieJsonStr);
        } catch (JSONException js) {
            Log.e(TAG, "JSON ERROR");
        }
         //mMovieLab = MovieLab.get(mContext);



        for (int i = 0; i < stringArrayList.size(); i++)

        {
            Movie movie = new Movie();
            movie.setTitle(stringArrayList.get(i)[0]);
            movie.setImageUrl(IMAGE_URL + stringArrayList.get(i)[1]);
            movie.setRelease_date(stringArrayList.get(i)[2]);
            movie.setVote(VOTE + stringArrayList.get(i)[3]);
            movie.setOverview(stringArrayList.get(i)[4]);

            Log.e(TAG,stringArrayList.get(i)[1] );

            mMovieLab.addMovie(movie);


        }


        return mMovieLab;
    }


//    protected void onPostExecute(MovieLab result) {
//
//
//        //mMovieAdapter = new MovieListFragment.MoiveAdapter(result.getmMovies());
//        //mMovieAdapter.addMovie(result.getmMovies());
//        mMovieAdapter.clear();
//        mMovieAdapter.addAll(result.getmMovies());
//        Log.e("NOW", "" + mMovieAdapter);
//        //mMovieAdapter.notifyDataSetChanged();
//        //mMovieListRecylerView.setAdapter(mMovieAdapter);
//
//
//    }

    private ArrayList<String[]> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_OVERVIEW = "overview";
        final String OWM_TITLE = "title";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_VOTE_AVERAGE = "vote_average";

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
                    singleMovie.getString(OWM_OVERVIEW)};

            resultStrs.add(moiveItem);

        }

        return resultStrs;

    }
}
