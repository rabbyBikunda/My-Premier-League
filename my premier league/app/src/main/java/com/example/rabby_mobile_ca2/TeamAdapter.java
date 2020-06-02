package com.example.rabby_mobile_ca2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//adapter to connect data with View items in a list.
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> implements Filterable {

    //member variables
    private Context mContext;

    //hold data in the adapter
    private ArrayList<Item> mItemList;

    //create copy of list
    private ArrayList<Item> itemListFull;

    // listeners represent the interfaces responsible to handle events eg onclick
    private OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        //constaroint is the input text
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Item> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(itemListFull);
            }else{
                //create new string from input which is lowercase  and removes empty spaces at the begining
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Item i : itemListFull){
                    //checks if item in list matches patteren
                    if(i.getTeamName().toLowerCase().contains(filterPattern)){
                        filteredList.add(i);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

                    return results;//return filtered list to publish resu;lts method
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //removee items cuz we only want to get items from filtered list
            mItemList.clear();
            mItemList.addAll((List)results.values);

            //tell adapter to refresh its ist
            notifyDataSetChanged();

        }
    };

    //interface method will forward click from adapter to main activity
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //method will be called in main activity and pass the mainactvity as the listener
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    //constructor for adapter where we will pass values
    public TeamAdapter(Context context, ArrayList<Item> itemList){
        mContext = context;
        mItemList = itemList;
        //create new arraylist which has sameitems as og list - copy,we can use this independantly
        itemListFull = new ArrayList<>(itemList);
    }


    //inflates the item layout, and returns a ViewHolder with the layout and the adapter
    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_team, parent, false);
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


        //set views to the values
        Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mImageView);
        holder.mTextViewTeamName.setText(teamName);


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



            //use itemView to get references to views on in the layout
            //onclick listeneer will be set on the itemView - whcih is the whole row
            public TeamViewHolder(View itemView) {
            super(itemView);

            //find the views
                mImageView = itemView.findViewById(R.id.imageView);
                mTextViewTeamName = itemView.findViewById(R.id.textView);




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
