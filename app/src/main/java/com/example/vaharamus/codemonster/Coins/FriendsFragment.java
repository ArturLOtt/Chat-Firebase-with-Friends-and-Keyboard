package com.example.vaharamus.codemonster.Coins;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vaharamus.codemonster.Crown.ChatActivity;
import com.example.vaharamus.codemonster.Crown.ProfileActivity;
import com.example.vaharamus.codemonster.R;
import com.example.vaharamus.codemonster.Model.Friends;
import com.example.vaharamus.codemonster.Pigeon_Post.FriendsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView Freundenx;

    private DatabaseReference FreundenRefx;
    private DatabaseReference UsersRefx;
    private FirebaseAuth reich;

    String online_user_id;
    private View myMainViewx;




    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         myMainViewx =  inflater.inflate(R.layout.fragment_friends, container, false);

        //    to display all the users in the recyclerview it have to be linked to the myMainViewx
         Freundenx = (RecyclerView) myMainViewx.findViewById(R.id.friendslistks);

         reich = FirebaseAuth.getInstance();
         online_user_id = reich.getCurrentUser().getUid();

         FreundenRefx = FirebaseDatabase
                 .getInstance()
                 .getReference()
                 .child("Friends")
                 .child(online_user_id);

         FreundenRefx.keepSynced(true);

         UsersRefx = FirebaseDatabase.getInstance().getReference().child("Users");

         UsersRefx.keepSynced(true);

//         manager to the friend list
        Freundenx.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return myMainViewx;
    }

    //        to retrieve all the users on the friend list

    @Override
    public void onStart() {
        super.onStart();
//        firebase Recycler needs 2 parameter: module class and the static class
        FirebaseRecyclerAdapter<Friends, FriendsViewHolder>firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>
                       (Friends.class,
                        R.layout.all_users_display_layout,
                        FriendsViewHolder.class,
                        FreundenRefx){

                    @Override
                    protected void populateViewHolder(final FriendsViewHolder viewHolder,
                                                      Friends model, int position) {
                        viewHolder.setDatex(model.getDatex());

//                        depois do =, o código vai pegar a key e storar no list_user_id
                        final String list_user_id = getRef(position).getKey();

                        UsersRefx.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {

                                final String userName = dataSnapshot.child("user_name").getValue().toString();
                                String thumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();

                                if(dataSnapshot.hasChild("online")){
                                    String online_status = (String) dataSnapshot
                                            .child("online").getValue()
                                            .toString();
                                    viewHolder.setUserOnline(online_status);
                                }

                                viewHolder.setUserName(userName);
                                viewHolder.setThumbImage(thumbImage, getContext());

                                viewHolder.vizarx.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CharSequence options[] = new CharSequence[]{
                                                userName + "'s Profile",
                                                "Send Message"
                                        };
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Select Options");

                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int position) {
                                                if(position == 0){
//                                                    it means when someone click on username profile it will send the suer to the suer profile

                                                    Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                                    profileIntent.putExtra("visit_user_id", list_user_id);
                                                    startActivity(profileIntent);
                                                }

                                                if(position == 1){
//                                                    it means when someone click on username profile it will send the suer to the suer profile

                                                    if(dataSnapshot.child("online").exists()){
                                                        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                        chatIntent.putExtra("visit_user_id", list_user_id);
                                                        chatIntent.putExtra("user_name", userName);
                                                        startActivity(chatIntent);
                                                    }
                                                    else{

                                                        UsersRefx.child(list_user_id).child("online")
                                                                .setValue(ServerValue.TIMESTAMP)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                                chatIntent.putExtra("visit_user_id", list_user_id);
                                                                chatIntent.putExtra("user_name", userName);
                                                                startActivity(chatIntent);
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                };

        Freundenx.setAdapter(firebaseRecyclerAdapter);

    }



}
