package com.example.vaharamus.codemonster.Crown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.vaharamus.codemonster.Pigeon_Post.AllUsersViewHolder;
import com.example.vaharamus.codemonster.R;
import com.example.vaharamus.codemonster.Model.AllUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AllUsersActivity extends AppCompatActivity {

    private Toolbar zeugx;
    private RecyclerView allRecyclerx;
    private DatabaseReference allDatabaseUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        zeugx = (Toolbar) findViewById(R.id.all_barks);
        setSupportActionBar(zeugx);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allRecyclerx = (RecyclerView) findViewById(R.id.all_recyclerks);
        allRecyclerx.setHasFixedSize(true);
        allRecyclerx.setLayoutManager(new LinearLayoutManager(this));

//        this reference is stored in this variable
        allDatabaseUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
//        it will load the data offline
        allDatabaseUserReference.keepSynced(true);


    }


    @Override
    protected void onStart() {
        super.onStart();

//        firebase recycler adapter to retrieve data from our
//        firebase database and to display this data to our recycler view

        FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>
                (AllUsers.class,
                 R.layout.all_users_display_layout,
                 AllUsersViewHolder.class,
                 allDatabaseUserReference) {


//            esse populate View holder sera usado para setar valores para o recycler view
            @Override
            protected void populateViewHolder(AllUsersViewHolder viewHolder,
                                              AllUsers model, final int position) {

                viewHolder.setUser_name(model.getUser_name());
                viewHolder.setUser_status(model.getUser_status());
//                viewHolder.setUser_thumb_image(getApplicationContext().model.getUser_thumb_image());

                viewHolder.vx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        first it get the position, then it will get the key from firebase
                        String visit_user_id = getRef(position).getKey();

                        Intent profileIntent = new Intent(AllUsersActivity.this, ProfileActivity.class);
//                      will be able to retireve all the data from the user we clicked
                        profileIntent.putExtra("visit_user_id", visit_user_id);
                        startActivity(profileIntent);

                    }
                });

            }
        };
//      to set the user name, status, image
        allRecyclerx.setAdapter(firebaseRecyclerAdapter);




    }
}
