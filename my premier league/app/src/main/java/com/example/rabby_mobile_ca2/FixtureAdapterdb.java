package com.example.rabby_mobile_ca2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FixtureAdapterdb extends RecyclerView.Adapter<FixtureAdapterdb.FixturedbViewHolder> {

    //member variables
    private Context mContext;

    private Cursor mCursor;

    //hold data in the adapter
    private List<Fixture> mFixtureList;


    //constructor for adapter where we will pass values
    public FixtureAdapterdb(Context context, Cursor cursor) {
        mContext = context;

        mCursor = cursor;
    }

    //ViewHolder that describes a View item and its position within the RecyclerView
    public class FixturedbViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewDate;
        public TextView mTextViewHomeTeam;
        public TextView mTextViewAwayTeam;
        public ImageView mImageViewHomeBadge;
        public ImageView mImageViewAwayBadge;


        public FixturedbViewHolder(View fixtureView) {
            super(fixtureView);

            mTextViewDate = fixtureView.findViewById(R.id.date);
            mTextViewHomeTeam = fixtureView.findViewById(R.id.home_team_name);
            mTextViewAwayTeam = fixtureView.findViewById(R.id.away_team_name);
            mImageViewHomeBadge = fixtureView.findViewById(R.id.homeTeamBadge);
            mImageViewAwayBadge = fixtureView.findViewById(R.id.awayTeamBadge);



        }

    }

    //inflates the item layout, and returns a ViewHolder with the layout and the adapter
    @Override
    public FixturedbViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_fixture,parent,false);
        return new FixturedbViewHolder(view);
    }

    //method connects your data to the view holder.
    @Override
    public void onBindViewHolder(FixturedbViewHolder holder, int position) {

        //check if cursor exists
        if(!mCursor.moveToPosition(position))
        {
            return;
        }

        //USE CURSOR TO READ VALUES FROM DATABASE
        String date = mCursor.getString(mCursor.getColumnIndex(FixtureContract.FixturesTable.COLUMN_DATE));
        String homeTeamName = mCursor.getString(mCursor.getColumnIndex(FixtureContract.FixturesTable.COLUMN_HOME_TEAM));
        String awayTeamName = mCursor.getString(mCursor.getColumnIndex(FixtureContract.FixturesTable.COLUMN_AWAY_TEAM));
        String homeTeamBadge = mCursor.getString(mCursor.getColumnIndex(FixtureContract.FixturesTable.COLUMN_HOME_TEAM_BADGE));
        String awayTeamBadge = mCursor.getString(mCursor.getColumnIndex(FixtureContract.FixturesTable.COLUMN_AWAY_TEAM_BADGE));

        long id = mCursor.getLong(mCursor.getColumnIndex(FixtureContract.FixturesTable._ID));




        //set views to the values - pass to activities
        holder.mTextViewDate.setText(date);
        holder.mTextViewHomeTeam.setText(homeTeamName);
        holder.mTextViewAwayTeam.setText(awayTeamName);
        Picasso.with(mContext).load(homeTeamBadge).fit().centerInside().into(holder.mImageViewHomeBadge);
        Picasso.with(mContext).load(awayTeamBadge).fit().centerInside().into(holder.mImageViewAwayBadge);


        //id wont be visible but it can be used to identify item in a actvity
        holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        //when u create adapter u will pass a cursor to it
        return mCursor.getCount();
    }

    //when u update database u pass a new cursor
    public void swapCursor(Cursor newCursor)
    {
        if(mCursor != null)
        {
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null)
        {
            //update recycler viewer
            notifyDataSetChanged();
        }
    }


}
