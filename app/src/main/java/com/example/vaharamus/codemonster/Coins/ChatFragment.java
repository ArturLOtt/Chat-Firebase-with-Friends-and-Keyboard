package com.example.vaharamus.codemonster.Coins;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vaharamus.codemonster.Crown.ChatActivity;
import com.example.vaharamus.codemonster.Model.Chats;
import com.example.vaharamus.codemonster.Pigeon_Post.ChatsViewHolder;
import com.example.vaharamus.codemonster.R;
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
public class ChatFragment extends Fragment {

    private RecyclerView myCodeChatsList;

    private View myMainView;

    private DatabaseReference FriendsReference;
    private DatabaseReference UsersReference;
    private FirebaseAuth mReich;

    String online_user_id;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //   this will get the friends in the friends nod


        myMainView = inflater.inflate(R.layout.fragment_chat, container, false);

        myCodeChatsList = (RecyclerView) myMainView.findViewById(R.id.rvChat_listks);

        mReich = FirebaseAuth.getInstance();
        online_user_id = mReich.getCurrentUser().getUid();

        FriendsReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Friends")
                .child(online_user_id);
        UsersReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users");

        myCodeChatsList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        myCodeChatsList.setLayoutManager(linearLayoutManager);


        // Inflate the layout for this fragment
        return myMainView;
    }

    @Override
    public void onStart() {
//                firebase to retrieve the users
        super.onStart();
        FirebaseRecyclerAdapter<Chats, ChatsViewHolder>firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Chats, ChatsViewHolder>
                        (Chats.class,
                                R.layout.all_users_display_layout,
                                ChatsViewHolder.class,
                                FriendsReference){

                    @Override
                    protected void populateViewHolder(final ChatsViewHolder viewHolder,
                                                      Chats model, int position) {


//                        depois do =, o código vai pegar a key e storar no list_user_id
                        final String list_user_id = getRef(position).getKey();

                        UsersReference.child(list_user_id)
                                .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {

                                final String userName = dataSnapshot
                                        .child("user_name")
                                        .getValue()
                                        .toString();
                                String thumbImage = dataSnapshot
                                        .child("user_thumb_image")
                                        .getValue().toString();

                                String userStatus = dataSnapshot
                                        .child("user_status")
                                        .getValue()
                                        .toString();


                                if(dataSnapshot.hasChild("online")){
                                    String online_status = (String) dataSnapshot
                                            .child("online")
                                            .getValue()
                                            .toString();
                                    viewHolder.setUserOnline(online_status);
                                }

                                viewHolder.setUserName(userName);
                                viewHolder.setThumbImage(thumbImage, getContext());

                                viewHolder.setUserStatus(userStatus);

                                viewHolder.vizarx.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(dataSnapshot.child("online").exists()){
                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                            chatIntent.putExtra("visit_user_id", list_user_id);
                                            chatIntent.putExtra("user_name", userName);
                                            startActivity(chatIntent);
                                        }
                                        else{

                                            UsersReference.child(list_user_id).child("online")
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
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                };

        myCodeChatsList.setAdapter(firebaseRecyclerAdapter);




    }
}
