package com.example.vaharamus.codemonster;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Vaharamus on 23/01/2018.
 */

public class chatOffline extends Application {

    private DatabaseReference userRefx;
    private FirebaseAuth mauth;
    private FirebaseUser currentUserx;




    @Override
    public void onCreate() {
        super.onCreate();


// this line will load the string types of variables
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

////        to load a image offline
//        Picasso.builder = new Picasso.Builder(this);
//        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
//        Picasso built = builder.build();
//        built.setIndicatorEnabled(true);
////        this enable the user to login in while offline
//        built.setLoggingEnabled(true);
//        Picasso.setSengletonInstance(built);

//
        mauth = FirebaseAuth.getInstance();
//        to get the current user
        currentUserx = mauth.getCurrentUser();

        //if the user is login into his account
        if (currentUserx != null){
            String online_user_id = mauth.getCurrentUser().getUid();
            userRefx = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Users")
                    .child(online_user_id);

            userRefx.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if the user close the app it will change the value from true to false
                    userRefx.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }




}

