package com.example.android.myappportfolio.topMovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.android.myappportfolio.R;
import com.example.android.myappportfolio.topMovies.MainActivity;
import com.example.android.myappportfolio.topMovies.Movie;
import com.example.android.myappportfolio.topMovies.MovieLab;
import com.example.android.myappportfolio.topMovies.MovieListFragment;
import com.example.android.myappportfolio.topMovies.Utility;
import com.example.android.myappportfolio.topMovies.data.MovieContract;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lk235 on 2017/5/13.
 */

public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {


    public static final String SORT_SETTING = "sort_setting";
    private static final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    private static final String COLLECT = "收藏";

    // Interval at which to sync with the movie, in seconds.
//    public static final int SYNC_INTERVAL = 60 * 60 * 24;  // 3 hours
//    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public static final String SYNC_DONE = "sync_done";


    //private final Context mContext;
    // private MovieAdapter mMovieAdapter;
    private MovieLab mMovieLab;
    private String mCategroySetting ;

    private final String TAG = "FetchMovieData";
    private final String MOVIE_URL = "http://api.themoviedb.org/3/movie";

    private final String IMAGE_URL = "https://image.tmdb.org/t/p/w185";
    private final String VOTE = "VOTE: ";
    private final String API_KEY = "api_key";
    private final String LANGUAGE = "language";

    public static final String [] MOVIE_NOTIFY_PROJECTION = {MovieContract.MovieEntry.COLUMN_TITLE};



    public static final int INDEX_MOVIE_TITLE = 0;

    public static final int MOVIE_NOTIFICATION_ID = 1000;

    @Retention(RetentionPolicy.SOURCE)
            @IntDef({SERVER_STATUS_OK, SERVER_STATUS_DOWN, SERVER_STATUS_INVALID, SERVER_STATUS_UNKNOWN })
    public @interface ServerStatus{}

    public static final int SERVER_STATUS_OK = 0;
    public static final int SERVER_STATUS_DOWN = 1;
    public static final int SERVER_STATUS_INVALID = 2;
    public static final int SERVER_STATUS_UNKNOWN = 3;


    String movieJsonStr = null;
    String apiKey = "3ec36d13c40b8c13f44a956ac6b7f785";
    String language = "zh";
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }


    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public MovieSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");
        String sortSetting = Utility.getPrefSortSetting(getContext());
        mMovieLab = MovieLab.get(getContext());
        ArrayList<String[]> stringArrayList = null;

        Uri uri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(sortSetting)
                .appendQueryParameter(LANGUAGE, language)
                .appendQueryParameter(API_KEY, apiKey)
                .build();

        getDataFromHttp(uri);


        try

        {
            stringArrayList = getMovieDataFromJson(movieJsonStr);

        } catch (JSONException js) {
//            setServerStatus(getContext(), SERVER_STATUS_INVALID);
            Log.e(TAG, "JSON ERROR");
        }

        mCategroySetting = MainActivity.mLastCateGroySetting;

        mMovieLab.deleteMovies(mCategroySetting);

        String movieId;
        int bulkInserted = 0;
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
                urlStr[j] = retTrailer.get(j)[1];


            }
            movie.setTrailerName(Utility.convertArrayToString(nameStr));
            movie.setTrailerUrl(Utility.convertArrayToString(urlStr));
            ArrayList<String[]> retReviews = getReviewsFromUri(movieId);
            String[] reviewAuthor = new String[retReviews.size()];
            String[] reviewContent = new String[retReviews.size()];
            for(int k = 0; k < retReviews.size(); k++  ){
                reviewAuthor[k] = retReviews.get(k)[0] ;
                reviewContent[k] = retReviews.get(k)[1];

            }
            movie.setReviewAuthor(Utility.convertArrayToString(reviewAuthor));
            movie.setReviewContent(Utility.convertArrayToString(reviewContent));

            ContentValues values = new ContentValues();
            Vector<ContentValues> cVVector = new Vector<ContentValues>(stringArrayList.size());
            values.put(MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING, movie.getCategroy());
            values.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL, movie.getImageUrl());
            values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
            values.put(MovieContract.MovieEntry.COLUMN_VOTE, movie.getVote());
            values.put(MovieContract.MovieEntry.COLUMN_OVER_VIEW, movie.getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_RUNTIME, movie.getRuntime());
            values.put(MovieContract.MovieEntry.COLUMN_TRAILER_NAME, movie.getTrailerName());
            values.put(MovieContract.MovieEntry.COLUMN_TRAILER_URL, movie.getTrailerUrl());
            values.put(MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR, movie.getReviewAuthor());
            values.put(MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT, movie.getReviewContent());
            values.put(MovieContract.MovieEntry.COLUMN_COLLECTED, movie.getColledted());
            cVVector.add(values);
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                bulkInserted = getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
            }




        }

        Log.d(LOG_TAG, "FetchMovieTask Complete. "+ ""+bulkInserted );
        Intent intent = new Intent(SYNC_DONE);
        getContext().sendBroadcast(intent);
//        setServerStatus(getContext(), SERVER_STATUS_OK);

            notifyMovie();




        return;
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

            for (int i = 0; i < jsonArray.length(); i++ ){

                trailerNmae = jsonArray.getJSONObject(i).getString(OWM_NAME);

                trailerUrl = YOUTUBE_URL + jsonArray.getJSONObject(i).getString(OWM_KEY);
                movieTrailers.add(new String[] {trailerNmae, trailerUrl});




            }



        } catch (JSONException js) {
            Log.e(TAG, "JSON ERROR");
        }


        return  movieTrailers;



    }

    private ArrayList<String[]> getReviewsFromUri (String movieId){
        final String REVIEWS = "reviews";

        ArrayList<String[]> movieReviews = new ArrayList<>();
        String author;
        String content;
        String url;
        Uri uri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(REVIEWS)
                .appendQueryParameter(API_KEY, apiKey)
                .build();

        getDataFromHttp(uri);

        final String OWM_RESULTS = "results";
        final String OWM_AUTHOR = "author";
        final String OWM_CONTENT = "content";
        final String OWN_URL = "url";

        try

        {
            JSONObject jsonString = new JSONObject(movieJsonStr);
            JSONArray jsonArray = jsonString.getJSONArray(OWM_RESULTS);

            for (int i = 0; i < jsonArray.length(); i++ ){

                author = jsonArray.getJSONObject(i).getString(OWM_AUTHOR);
                content = jsonArray.getJSONObject(i).getString(OWM_CONTENT);
                url = jsonArray.getJSONObject(i).getString(OWN_URL);
                movieReviews.add(new String[]{author, content, url});

            }



        } catch (JSONException js) {
            Log.e(TAG, "JSON ERROR");
        }


        return  movieReviews;



    }

    private void getDataFromHttp( Uri uri) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try

        {


            URL url = new URL(uri.toString());
            Log.i(TAG, url + "");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
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
//                setServerStatus(getContext(), SERVER_STATUS_DOWN);
                movieJsonStr = null;
            }
            movieJsonStr = stringBuffer.toString();


        } catch (
                IOException ioe)

        {
//            setServerStatus(getContext(), SERVER_STATUS_DOWN);
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



    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);

        }
        return newAccount;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        int sync_interval = Utility.getPrefSyncSetting(context);
        int sync_flextime = sync_interval / 3;
        Log.i("INTERVAL", sync_interval + "");
        MovieSyncAdapter.configurePeriodicSync(context, sync_interval, sync_flextime);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {

        getSyncAccount(context);

    }

    private void notifyMovie() {
        Context context = getContext();
        //checking the last update and notify if it' the most populat movie
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        String lastMovieTitle = prefs.getString(lastNotificationKey, "");
        String notificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean isNoticationEnabled = prefs.getBoolean(notificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

            Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, MOVIE_NOTIFY_PROJECTION, null, null, null);

        if(isNoticationEnabled){
            if (cursor.moveToFirst()) {

                String movieTitle = cursor.getString(INDEX_MOVIE_TITLE);
                Log.i("DATABASETITLE", movieTitle);
                boolean test = Utility.getPrefSortSetting(context).equals(R.string.pref_sort_default) ;
                Log.i("CUSOR" , "" + Utility.getPrefSortSetting(context) + R.string.pref_sort_default);

                if(Utility.getPrefSortSetting(context).equals(context.getString(R.string.pref_sort_default))
                        && !movieTitle.equals(lastMovieTitle)){

                    int iconId = R.drawable.ic_logo;
                    String title = context.getString(R.string.hot_movie);

                    // Define the text of the forecast.
                    String contentText = String.format(context.getString(R.string.format_notification),movieTitle);


                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setSmallIcon(iconId)
                                    .setContentTitle(title)
                                    .setAutoCancel(true)
                                    .setContentText(contentText);

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(context, MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify(MOVIE_NOTIFICATION_ID, mBuilder.build());


                    //refreshing last sync
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(lastNotificationKey, movieTitle);
                    editor.commit();

                }
            }


        }
        }

//        static private void setServerStatus(Context context, @ServerStatus int serverStatus){
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//            SharedPreferences.Editor spEditor = sharedPreferences.edit();
//            spEditor.putInt(context.getString(R.string.pref_server_status_key), serverStatus);
//            spEditor.commit();
//        }


    }


