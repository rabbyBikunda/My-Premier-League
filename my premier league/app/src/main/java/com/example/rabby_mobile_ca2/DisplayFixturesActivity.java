package com.example.rabby_mobile_ca2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import org.apache.http.client.ResponseHandler;

import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.rabby_mobile_ca2.StartActivity.EXTRA_TEAM_NAME;
import static com.example.rabby_mobile_ca2.StartActivity.EXTRA_URL;
import static com.example.rabby_mobile_ca2.StartActivity.EXTRA_ID;
import static com.example.rabby_mobile_ca2.SearchTeamActivity.EXTRA_NAME2;





public class DisplayFixturesActivity extends AppCompatActivity implements FixtureAdapter.OnItemClickListener {

    private String teamId;
    private RecyclerView mRecyclerView;
    private FixtureAdapter mFixtureAdapter;
    private ArrayList<Fixture> mFixtureList;
    private FixtureAdapterdb mAdapter;

    private HashMap<String, String> badges;

    private ProgressDialog simpleWaitDialog;



    private SQLiteDatabase mDatabase;

    private String teamName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_team);

         Intent intent = getIntent();
        teamName =intent.getStringExtra(EXTRA_TEAM_NAME);
         if(teamName == null)
         {
             teamName =intent.getStringExtra(EXTRA_NAME2);

         }

         TextView textView = findViewById(R.id.teamFixtures);

         String fixtures = " Fixtures";

         textView.setText(teamName + fixtures);



        teamId = intent.getStringExtra(EXTRA_ID);


        //ImageView imageView = findViewById(R.id.image_view_detail);


        mRecyclerView = findViewById(R.id.recycler_view_fixture);

        //view wont change width and height
        mRecyclerView.setHasFixedSize(true);

        // need items to be layed in a linear way
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFixtureList = new ArrayList<>();

        badges = new HashMap<>();

        new HttpGetTask2().execute();
        new HttpGetTask().execute();


    }

//    public Cursor getAllItems() {
//        return mDatabase.query(FixtureContract.FixturesTable.TABLE_NAME,null,null,null,
//                null,null,FixtureContract.FixturesTable._ID );
//    }

    @Override
    public void onItemClick(int position) {

        Fixture clickedItem = mFixtureList.get(position);

        FixtureDbHelper dbHelper = new FixtureDbHelper(this);
        //mAdapter =  new FixtureAdapterdb(DisplayFixturesActivity.this,getAllItems());

        mDatabase = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        //USED TO ADD ITEM
        cv.put(FixtureContract.FixturesTable.COLUMN_DATE,clickedItem.getDate());
        cv.put(FixtureContract.FixturesTable.COLUMN_HOME_TEAM,clickedItem.getHomeTeamName());
        cv.put(FixtureContract.FixturesTable.COLUMN_AWAY_TEAM,clickedItem.getAwayTeamName());
        cv.put(FixtureContract.FixturesTable.COLUMN_AWAY_TEAM_BADGE,clickedItem.getAwayTeamBadge());
        cv.put(FixtureContract.FixturesTable.COLUMN_HOME_TEAM_BADGE,clickedItem.getHomeTeamBadge());



        mDatabase.insert(FixtureContract.FixturesTable.TABLE_NAME,null,cv);

        Toast toast = Toast.makeText(this, "Fixture Added", Toast.LENGTH_LONG);
        toast.show();

    }

    public void launchSecondActivity(View view) {

        Intent intent = new Intent(DisplayFixturesActivity.this, ViewSavedFixturesActivity.class);
        startActivity(intent);
    }

    private class HttpGetTask2 extends AsyncTask<Void, Void, HashMap<String, String>> {

        private static final String API_KEY = "30cdf7ee4ae0c47a082c2c127d3adc313ec7baee045b9c870d20cb5d8b824b07";

        private static final String URL = "https://api.myjson.com/bins/yanvg";


        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected void onPreExecute() {

            simpleWaitDialog =   ProgressDialog.show(DisplayFixturesActivity.this,
                    "Wait", "Getting Fixtures");

        }

        @Override
        protected HashMap<String, String>  doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);

            JSONResponseHandler responseHandler = new JSONResponseHandler();

            try {
                mClient.execute(request, responseHandler);
                if (null != mClient)
                    mClient.close();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(HashMap<String, String>  result) {
            simpleWaitDialog.dismiss();

        }


        private class JSONResponseHandler implements ResponseHandler<HashMap<String, String>> {

            private static final String TEAM_BADGE_TAG = "team_badge";
            private static final String TEAM_NAME_TAG = "team_name";

            @Override
            public HashMap<String, String> handleResponse(HttpResponse response) throws IOException {

                String JSONResponse = new BasicResponseHandler().handleResponse(response);

                try {
                    // Get top-level JSON Object -
                    JSONArray teams = new JSONArray(JSONResponse);

                    // Iterate over list
                    for (int i = 0; i < teams.length(); i++) {

                        // Get single earthquake data - a Map
                        JSONObject team = (JSONObject) teams.get(i);

                        // Summarize earthquake data as a string and add it to list of items
                        String teamName = team.getString(TEAM_NAME_TAG);
                        String badge = team.getString(TEAM_BADGE_TAG);

                        badges.put(teamName,badge);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return badges;

            }

        }
    }

    private class HttpGetTask extends AsyncTask<Void, Void, ArrayList<Fixture>> {

        private static final String API_KEY = "30cdf7ee4ae0c47a082c2c127d3adc313ec7baee045b9c870d20cb5d8b824b07";

        String URL = "https://apiv2.apifootball.com/?action=get_events&from=2019-12-17&to=2020-05-18&league_id=148&team_id="+
                teamId + "&APIkey=" + API_KEY;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected ArrayList<Fixture> doInBackground(Void... params) {

            HttpGet request = new HttpGet(URL);

          JSONResponseHandler responseHandler = new JSONResponseHandler();

            try {
                mClient.execute(request, responseHandler);
                if (null != mClient)
                    mClient.close();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Fixture> result) {

            mFixtureAdapter = new FixtureAdapter(DisplayFixturesActivity.this,mFixtureList);
            mRecyclerView.setAdapter(mFixtureAdapter);
            mFixtureAdapter.setOnItemClickListener(DisplayFixturesActivity.this);
        }

        private class JSONResponseHandler implements ResponseHandler<ArrayList<Fixture>> {

            private static final String MATCH_ID_TAG = "match_id";
            private static final String DATE_TAG = "match_date";
            private static final String HOME_TEAM_NAME_TAG = "match_hometeam_name";
            private static final String AWAY_TEAM_NAME_TAG = "match_awayteam_name";

            @Override
            public ArrayList<Fixture> handleResponse(HttpResponse response) throws IOException {

                String JSONResponse = new BasicResponseHandler().handleResponse(response);


                try {
                    /// Get top-level JSON Object -
                    JSONArray teams = new JSONArray(JSONResponse);

                    // Iterate over list
                    for (int i = 0; i < teams.length(); i++) {

                        // Get single earthquake data - a Map
                        JSONObject team = (JSONObject) teams.get(i);

                        // Summarize earthquake data as a string and add it to list of items
                        String date = team.getString(DATE_TAG);
                        String homeTeamName = team.getString(HOME_TEAM_NAME_TAG);
                        String awayTeamName = team.getString(AWAY_TEAM_NAME_TAG);

                        //if(badges.get(homeTeamName).equals())
                        mFixtureList.add(new Fixture(date,awayTeamName,badges.get(homeTeamName),homeTeamName,badges.get(awayTeamName)));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return mFixtureList;
            }

        }

    }

}


