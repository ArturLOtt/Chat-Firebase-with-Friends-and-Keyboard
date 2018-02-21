package com.example.vaharamus.codemonster.Coins;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vaharamus.codemonster.Crown.ChatActivity;
import com.example.vaharamus.codemonster.Crown.ProfileActivity;
import com.example.vaharamus.codemonster.Model.Request;
import com.example.vaharamus.codemonster.Pigeon_Post.RequestViewHolder;
import com.example.vaharamus.codemonster.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    private RecyclerView codeRequestList;
    private View codeMainView;

    private DatabaseReference FriendsRequestReference;
    private FirebaseAuth maximus;
    String online_user_id;

    private DatabaseReference UsersReference;

    private DatabaseReference FriendsDatabaseRef;
    private DatabaseReference FriendsReqDatabaseRef;


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        codeMainView = inflater.inflate(R.layout.fragment_request, container, false);

        codeRequestList = (RecyclerView) codeMainView.findViewById(R.id.rvRequestListks);

        maximus = FirebaseAuth.getInstance();
//        to get the Id
        online_user_id = maximus.getCurrentUser().getUid();

        FriendsRequestReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Friend_Requests")
                .child(online_user_id);

        UsersReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users");

        FriendsDatabaseRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Friends");

        FriendsReqDatabaseRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Friend_Requests");


        codeRequestList.setHasFixedSize(true);

//        it will show the new friends request that it will get from the user
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        codeRequestList.setLayoutManager(linearLayoutManager);



        // Inflate the layout for this fragment
        return codeMainView;
    }

//    to create a database referece to the nod Friends_Requests
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Request, RequestViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Request, RequestViewHolder>
                (Request.class,
                 R.layout.friend_request_all_users_layout,
                 RequestViewHolder.class,
                 FriendsRequestReference)

        {
            @Override
            protected void populateViewHolder(final RequestViewHolder viewHolder, Request model, int position) {

//                list_user_id gets the users' unique id from firebase database (like PiKnHJmg...)
                final String list_users_id = getRef(position).getKey();

                DatabaseReference get_type_ref = getRef(position).child("request_type").getRef();

                get_type_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            String request_type = dataSnapshot.getValue().toString();

                            if (request_type.equals("received")){

                                UsersReference.child(list_users_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

//  retrieving the user name, thumb image and  status
                                        final String userName = dataSnapshot
                                                .child("user_name")
                                                .getValue()
                                                .toString();

                                        final String thumbImage = dataSnapshot
                                                .child("user_thumb_image")
                                                .getValue()
                                                .toString();

                                        final String userStatus = dataSnapshot
                                                .child("user_status")
                                                .getValue()
                                                .toString();

                                        viewHolder.setUserName(userName);
                                        viewHolder.setThumb_user_image(thumbImage, getContext());
                                        viewHolder.setUser_Status(userStatus);

                                        viewHolder.maviw.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                CharSequence options[] = new CharSequence[]{
                                                        "Accept Friend Request",
                                                        "Cancel Friend Request"
                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Friend Request Options");

                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int position) {
                                                        if(position == 0){
//
                                                            Calendar forDatex = Calendar.getInstance();
                                                            SimpleDateFormat currentDatex = new SimpleDateFormat("dd-MMMM-yyyy");
                                                            final String saveCurrentDate = currentDatex.format(forDatex.getTime());

                                                            FriendsDatabaseRef.child(online_user_id)
                                                                    .child(list_users_id)
                                                                    .child("date")
                                                                    .setValue(saveCurrentDate)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
//        it will create a nod (friends), for the receiver and the sender to become friends
                                                                            FriendsDatabaseRef
                                                                                    .child(list_users_id)
                                                                                    .child(online_user_id)
                                                                                    .child("date")
                                                                                    .setValue(saveCurrentDate)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
//                                        after they become friends, to remove the send request, etc
                                                                                            FriendsReqDatabaseRef
                                                                                                    .child(online_user_id)
                                                                                                    .child(list_users_id).removeValue()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                            if (task.isSuccessful()){
                                                                                                                FriendsReqDatabaseRef
                                                                                                                        .child(list_users_id)
                                                                                                                        .child(online_user_id)
                                                                                                                        .removeValue()
                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                if (task.isSuccessful()){

                                                                                                                                    Toast.makeText(getContext()
                                                                                                                                            ,"Friend Request Accepted Successfully"
                                                                                                                                            , Toast.LENGTH_SHORT).show();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });

                                                        }

                                                        if(position == 1){
// the one is for when someone decline a friend request

                                                            FriendsDatabaseRef.child(online_user_id)
                                                                    .child(list_users_id).removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()){
                                                                                FriendsDatabaseRef.child(list_users_id)
                                                                                        .child(online_user_id).removeValue()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                if (task.isSuccessful()){

                                                                                                    Toast.makeText(getContext()
                                                                                                            ,"Friend Request Cancelled Successfully"
                                                                                                            , Toast.LENGTH_SHORT).show();

                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });



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

                            else if (request_type.equals("sent")){

                                Button req_sent_btn = viewHolder.maviw.findViewById(R.id.btnRequestAcceptks);
                                req_sent_btn.setText("Request sent");

                                viewHolder.maviw.findViewById(R.id.btnRequestDeclineks).setVisibility(View.INVISIBLE);

                                UsersReference.child(list_users_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String userName = dataSnapshot
                                                .child("user_name").getValue().toString();
                                        final String thumbImage = dataSnapshot
                                                .child("user_thumb_image").getValue().toString();
                                        final String userStatus = dataSnapshot
                                                .child("user_status").getValue().toString();

                                        viewHolder.setUserName(userName);
                                        viewHolder.setThumb_user_image(thumbImage, getContext());
                                        viewHolder.setUser_Status(userStatus);

                                        viewHolder.maviw.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                CharSequence options[] = new CharSequence[]{
                                                        "Cancel Friend Request",

                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Friend Request Sent");

                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int position) {

                                                        if(position == 0){
// the one is for when someone decline a friend request

                                                            FriendsDatabaseRef.child(online_user_id)
                                                                    .child(list_users_id).removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()){
                                                                                FriendsDatabaseRef.child(list_users_id)
                                                                                        .child(online_user_id).removeValue()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                if (task.isSuccessful()){

                                                                                                    Toast.makeText(getContext()
                                                                                                            ,"Friend Request Cancelled Successfully"
                                                                                                            , Toast.LENGTH_SHORT).show();

                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });



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


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        };

        codeRequestList.setAdapter(firebaseRecyclerAdapter);



    }
}
