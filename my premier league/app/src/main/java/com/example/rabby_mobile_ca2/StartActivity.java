package com.example.rabby_mobile_ca2;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class StartActivity extends Activity implements TableAdapter.OnItemClickListener {

    private TextView mTextView = null;

    //constant key for  image to send to detail team actvivty
    public static final String EXTRA_URL = "team_badge";

    public static final String EXTRA_ID = "team_id";

    public  static final String EXTRA_TEAM_NAME = "team_name";

    private RecyclerView mRecyclerView;
    private TableAdapter mTeamadapter;
    private ArrayList<Item> mItemList;

//    private HashMap<String, String> badges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recycler_view);
        //view wont change width and height
        mRecyclerView.setHasFixedSize(true);

        // need items to be layed in a linear way
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItemList = new ArrayList<>();
//        badges = new HashMap<>();

        //new HttpGetTask2().execute();
        new HttpGetTask().execute();
    }
    //handle click events on recycle view items
    //start detail activity and pass values of clciked item to it
    @Override
    public void onItemClick(int position) {


        Intent detailIntent = new Intent(this, DisplayFixturesActivity.class);

        //get item at clicked poistion from itemlist
        Item clickedItem = mItemList.get(position);


        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_ID, clickedItem.getTeamId());
        detailIntent.putExtra(EXTRA_TEAM_NAME, clickedItem.getTeamName());
        startActivity(detailIntent);
    }

    public void launchActivity(View view) {
        Intent intent = new Intent(StartActivity.this, SearchTeamActivity.class);
        startActivity(intent);
    }


    private class HttpGetTask extends AsyncTask<Void, Void, ArrayList<Item>> {


        private static final String API_KEY = "30cdf7ee4ae0c47a082c2c127d3adc313ec7baee045b9c870d20cb5d8b824b07";

        private static final String URL = "https://api.myjson.com/bins/yanvg"; //+ API_KEY;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected ArrayList<Item> doInBackground(Void... params) {

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
        protected void onPostExecute(ArrayList<Item> result) {

            mTeamadapter = new TableAdapter(StartActivity.this, mItemList);
            mRecyclerView.setAdapter(mTeamadapter);
            mTeamadapter.setOnItemClickListener(StartActivity.this);

        }

        private class JSONResponseHandler implements ResponseHandler<ArrayList<Item>> {

            private static final String TEAM_NAME_TAG = "team_name";
            private static final String TEAM_BADGE_TAG = "team_badge";
            private static final String TEAM_WINS_TAG = "overall_league_W";
            private static final String TEAM_DRAWS_TAG = "overall_league_D";
            private static final String TEAM_LOSSES_TAG = "overall_league_L";
            private static final String TEAM_POINTS_TAG = "overall_league_PTS";
            private static final String TEAM_ID_TAG = "team_id";


            @Override
            public ArrayList<Item> handleResponse(HttpResponse response) throws IOException {

                String JSONResponse = new BasicResponseHandler().handleResponse(response);

                try {
                    /// Get top-level JSON Object -
                    JSONArray teams = new JSONArray(JSONResponse);

                    // Iterate over list
                    for (int i = 0; i < teams.length(); i++) {

                        // Get single earthquake data - a Map
                        JSONObject team = (JSONObject) teams.get(i);

                        // Summarize earthquake data as a string and add it to list of items

                        String teamId = team.getString(TEAM_ID_TAG);
                        String badge = team.getString(TEAM_BADGE_TAG);
                        String name = team.getString(TEAM_NAME_TAG);
                        String wins = team.getString(TEAM_WINS_TAG);
                        String draws = team.getString(TEAM_DRAWS_TAG);
                        String losses = team.getString(TEAM_LOSSES_TAG);
                        String points = team.getString(TEAM_POINTS_TAG);


                        //mItemList.add(new Item(teamId, badges.get(teamId),name,wins,draws,losses,points));
                        mItemList.add(new Item(teamId, badge,name,wins,draws,losses,points));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return mItemList;
            }

        }
    }

}


