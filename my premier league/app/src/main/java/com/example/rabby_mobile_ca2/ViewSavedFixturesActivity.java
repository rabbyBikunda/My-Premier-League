package com.example.rabby_mobile_ca2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ViewSavedFixturesActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private FixtureAdapterdb mAdapter;

    private RecyclerView mRecyclerView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_fixtures);

        final Toast toast2 = Toast.makeText(this, "Fixture Deleted", Toast.LENGTH_LONG);


        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewSavedFixturesActivity.this,StartActivity.class));
            }
        });




        FixtureDbHelper dbHelper = new FixtureDbHelper(this);
        mDatabase = dbHelper.getWritableDatabase();


        mRecyclerView = findViewById(R.id.recycler_view_saved);

        //view wont change width and height
        mRecyclerView.setHasFixedSize(true);

        // need items to be layed in a linear way
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter =  new FixtureAdapterdb(ViewSavedFixturesActivity.this,getAllItems());

        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove( RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                toast2.show();
                return false;

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long)viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(mRecyclerView);

    }


    private void removeItem(long id) {
        //go to table u created,  find the id that is le memme with item id and remove it
        mDatabase.delete(FixtureContract.FixturesTable.TABLE_NAME,
                FixtureContract.FixturesTable._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }

    //queries whole table
    public Cursor getAllItems() {
        return mDatabase.query(FixtureContract.FixturesTable.TABLE_NAME,null,null,null,
                null,null,FixtureContract.FixturesTable._ID );


    }
}
