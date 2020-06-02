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

//adapter to connect data with View items in a list.
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TeamViewHolder> {

    //member variables
    private Context mContext;

    //hold data in the adapter
    private ArrayList<Item> mItemList;

    // listeners represent the interfaces responsible to handle events eg onclick
    private OnItemClickListener mListener;

    //interface method will forward click from adapter to main activity
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //method will be called in main activity and pass the mainactvity as the listener
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    //constructor for adapter where we will pass values
    public TableAdapter(Context context, ArrayList<Item> itemList){
        mContext = context;
        mItemList = itemList;
    }


    //inflates the item layout, and returns a ViewHolder with the layout and the adapter
    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new TeamViewHolder(v);
    }

    //method connects your data to the view holder.
    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        //item in list at position will be saved in the currentItem variable
        //then u can use currentItem to get the text and the image view
        Item currentItem = mItemList.get(position);

        //String teamId = currentItem.getTeamId();
        String imageUrl = currentItem.getImageUrl();
        String teamName= currentItem.getTeamName();
        String wins = currentItem.getWins();
        String draws = currentItem.getDraws();
        String losses = currentItem.getLosses();
        String points = currentItem.getPoints();

        //set views to the values
        Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mImageView);
        holder.mTextViewTeamName.setText(teamName);
        holder.mTextViewWins.setText(wins);
        holder.mTextViewDraws.setText(draws);
        holder.mTextViewLosses.setText(losses);
        holder.mTextViewPoints.setText(points);
    }

    @Override
    public int getItemCount() {
        //the adapter will have the same number of items as the itemlist
        return mItemList.size();
    }

    //ViewHolder that describes a View item and its position within the RecyclerView
    public class TeamViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewTeamName;
        public TextView mTextViewWins;
        public TextView mTextViewDraws;
        public TextView mTextViewLosses;
        public TextView mTextViewPoints;

            //use itemView to get references to views on in the layout
            //onclick listeneer will be set on the itemView - whcih is the whole row
            public TeamViewHolder(View itemView) {
            super(itemView);

            //find the views
                mImageView = itemView.findViewById(R.id.imageView);
                mTextViewTeamName = itemView.findViewById(R.id.text_view_team);
                mTextViewWins = itemView.findViewById(R.id.wins);
                mTextViewDraws = itemView.findViewById(R.id.draws);
                mTextViewLosses = itemView.findViewById(R.id.losses);
                mTextViewPoints = itemView.findViewById(R.id.points);



                itemView.setOnClickListener(new View.OnClickListener() {
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
