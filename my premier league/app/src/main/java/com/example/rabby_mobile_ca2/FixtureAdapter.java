package com.example.rabby_mobile_ca2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.FixtureViewHolder> {

    //member variables
    private Context mContext;

    //hold data in the adapter
    private List<Fixture> mFixtureList;

    private FixtureAdapter.OnItemClickListener mListener;

    //interface method will forward click from adapter to main activity
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //method will be called in  activity and pass the actvity as the listener
    public void setOnItemClickListener(FixtureAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    //constructor for adapter where we will pass values
    public FixtureAdapter(Context context, List<Fixture> fixtureList) {
        mContext = context;
        mFixtureList = fixtureList;
    }

    //inflates the item layout, and returns a ViewHolder with the layout and the adapter
    @Override
    public FixtureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_fixture, parent, false);
        return new FixtureViewHolder(v);
    }

    //method connects your data to the view holder.
    @Override
    public void onBindViewHolder(FixtureViewHolder holder, int position) {
        //item in list at position will be saved in the currentItem variable
        //then u can use currentItem to get the text and the image view
        Fixture currentFixture = mFixtureList.get(position);

        //String teamId = currentItem.getTeamId();
        String date = currentFixture.getDate();
        String homeTeamName = currentFixture.getHomeTeamName();
        String awayTeamName = currentFixture.getAwayTeamName();
        String homeTeamBadge = currentFixture.getHomeTeamBadge();
        String awayTeamBadge = currentFixture.getAwayTeamBadge();


        //set views to the values
        holder.mTextViewDate.setText(date);
        holder.mTextViewHomeTeam.setText(homeTeamName);
        holder.mTextViewAwayTeam.setText(awayTeamName);
        Picasso.with(mContext).load(homeTeamBadge).fit().centerInside().into(holder.mImageViewHome);
        Picasso.with(mContext).load(awayTeamBadge).fit().centerInside().into(holder.mImageViewAway);


    }

    @Override
    public int getItemCount() {
        return mFixtureList.size();
    }

    //ViewHolder that describes a View item and its position within the RecyclerView
    public class FixtureViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewDate;
        public TextView mTextViewHomeTeam;
        public TextView mTextViewAwayTeam;
        public ImageView mImageViewHome;
        public ImageView mImageViewAway;

        public FixtureViewHolder(View fixtureView) {
            super(fixtureView);

            mTextViewDate = fixtureView.findViewById(R.id.date);
            mTextViewHomeTeam = fixtureView.findViewById(R.id.home_team_name);
            mTextViewAwayTeam = fixtureView.findViewById(R.id.away_team_name);
            mImageViewHome = fixtureView.findViewById(R.id.homeTeamBadge);
            mImageViewAway = fixtureView.findViewById(R.id.awayTeamBadge);

            fixtureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check if listener for interface is not null
                    if(mListener != null){
                        //position of item
                        int position = getAdapterPosition();
                        //checks if position is still valid
                        if (position != RecyclerView.NO_POSITION){
                            //
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }

    }

}
