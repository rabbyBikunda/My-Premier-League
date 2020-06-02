package com.example.rabby_mobile_ca2;

import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchTeamActivity extends AppCompatActivity implements TeamAdapter.OnItemClickListener{

    public static final String EXTRA_URL2 = "team_badge";

    public static final String EXTRA_ID2 = "team_id";

    public static final String EXTRA_NAME2 = "team_name";


    private ArrayList<Item> mItemList;
    private TeamAdapter mTeamadapter;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_team);



        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerViewTeam);
        //view wont change width and height
        mRecyclerView.setHasFixedSize(true);

        // need items to be layed in a linear way
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItemList = new ArrayList<>();
        new HttpGetTask().execute();


    }

    @Override
    public void onItemClick(int position) {


        Intent detailIntent = new Intent(this, DisplayFixturesActivity.class);

        //get item at clicked poistion from itemlist
        Item clickedItem = mItemList.get(position);

        detailIntent.putExtra(EXTRA_NAME2, clickedItem.getTeamName());
        detailIntent.putExtra(EXTRA_URL2, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_ID2, clickedItem.getTeamId());
        startActivity(detailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        //activate options menu
        inflater.inflate(R.menu.example_menu, menu);

        //add reference to search menu
        MenuItem searchItem = menu.findItem(R.id.action_search);

        //get reference to search view
        SearchView searchView = (SearchView) searchItem.getActionView();

        //
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //filter list in real time
            @Override
            public boolean onQueryTextChange(String newText) {
                //take adapter which is variable of recylcer view
                //use the getfilter that u made and pass text in the text field
                mTeamadapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private class HttpGetTask extends AsyncTask<Void, Void,  ArrayList<Item>> {

        private static final String API_KEY = "30cdf7ee4ae0c47a082c2c127d3adc313ec7baee045b9c870d20cb5d8b824b07";

        private static final String URL = "https://api.myjson.com/bins/yanvg"; //"https://apiv2.apifootball.com/?action=get_teams&league_id=148&APIkey=" + API_KEY;


        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected  ArrayList<Item>  doInBackground(Void... params) {
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
            mTeamadapter = new TeamAdapter(SearchTeamActivity.this, mItemList);
            mRecyclerView.setAdapter(mTeamadapter);
            mTeamadapter.setOnItemClickListener(SearchTeamActivity.this);

        }


        private class JSONResponseHandler implements ResponseHandler<ArrayList<Item>> {

            private static final String TEAM_BADGE_TAG = "team_badge";
            private static final String TEAM_NAME_TAG = "team_name";
            private static final String TEAM_ID_TAG = "team_id";

            @Override
            public ArrayList<Item> handleResponse(HttpResponse response) throws IOException {

                String JSONResponse = new BasicResponseHandler().handleResponse(response);

                try {
                    // Get top-level JSON Object -
                    JSONArray teams = new JSONArray(JSONResponse);

                    // Iterate over list
                    for (int i = 0; i < teams.length(); i++) {

                        // Get single earthquake data - a Map
                        JSONObject team = (JSONObject) teams.get(i);

                        // Summarize earthquake data as a string and add it to list of items
                        String id = team.getString(TEAM_ID_TAG);
                        String badge = team.getString(TEAM_BADGE_TAG);
                        String teamName = team.getString(TEAM_NAME_TAG);

                        mItemList.add(new Item(id,badge,teamName));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return mItemList;

            }

        }
    }
}
