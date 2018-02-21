package com.example.vaharamus.codemonster.Crown;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaharamus.codemonster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private Button sendRequest;
    private Button sendDecline;
    private TextView profileName;
    private TextView profileStatus;
    private ImageView profileImage;

    //reference to firebase database
    private DatabaseReference UsersReference;

    private String CURRENT_STATE;
//    to create a database reference
    private DatabaseReference FriendRequestRefnce;
    private FirebaseAuth mauth;
    String sender_user_id;
    String receiver_user_id;
    private DatabaseReference FriendsReference;
    private DatabaseReference NotificationsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        para o database de como esta a solicitações de amizades, rejeiçs, etc
        FriendRequestRefnce = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
        FriendRequestRefnce.keepSynced(true);


        mauth = FirebaseAuth.getInstance();
        sender_user_id = mauth.getCurrentUser().getUid();

        FriendsReference = FirebaseDatabase.getInstance().getReference().child("Friends");
        FriendsReference.keepSynced(true);

        NotificationsRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        NotificationsRef.keepSynced(true);

        UsersReference = FirebaseDatabase.getInstance().getReference().child("Users");

//        this is the id of whom we clicked
        receiver_user_id = getIntent().getExtras().get("visit_user_id").toString();

////       esse toast foi um teste para ver se ele pegava a key, funcionou.
////        Ele mostra a key 'toda complicada' do firebase quando se clica em um dos perfils
//        Toast.makeText(ProfileActivity.this, receiver_user_id, Toast.LENGTH_SHORT).show();

        sendRequest = (Button) findViewById(R.id.btn_send_requestks);
        sendDecline = (Button) findViewById(R.id.btn_send_declineks);
        profileName = (TextView) findViewById(R.id.txtDisplay_nameks);
        profileStatus = (TextView) findViewById(R.id.txtDisplay_statusks);
        profileImage = (ImageView) findViewById(R.id.profile_visit_imageks);

        CURRENT_STATE = "not_friends";


        UsersReference.child(receiver_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//              retrieve the data from firebase database
                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String image = dataSnapshot.child("user_image").getValue().toString();

                profileName.setText(name);
                profileStatus.setText(status);
//                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.default_image).into(profileImage)

                FriendRequestRefnce.child(sender_user_id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

//   here it retrieve the data from firebase and will check if the message was send
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

//                      this will check for friends request
                                    if(dataSnapshot.hasChild(receiver_user_id)){
                                        String request_type = dataSnapshot
                                                .child(receiver_user_id).child("request_type")
                                                .getValue().toString();
                                        if(request_type.equals("sent")){
//                      se for sent, a pessoa vai ter a opção de recusar ou aceitar
                                            CURRENT_STATE = "request_sent";
                                            sendRequest.setText("Cancel_Friend_Request");

                                            sendDecline.setVisibility(View.INVISIBLE);
                                            sendDecline.setEnabled(false);
                                        }
                                        else if(request_type.equals("received")){
                                            CURRENT_STATE = "request_received";
                                            sendRequest.setText("Accepted_Friend_Request");

                                            sendDecline.setVisibility(View.VISIBLE);
                                            sendDecline.setEnabled(true);

                                            sendDecline.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View bv) {
                                                    DeclineFriendRequest();
                                                }
                                            });
//                   if a person unfriend each other, it should remove this
                                        }
                                    }

                                else{
                                    FriendsReference.child("sender_user_id")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(receiver_user_id)){
                                                        CURRENT_STATE = "friends";
                                                        sendRequest.setText(("Unfriend"));

                                                        sendDecline.setVisibility(View.INVISIBLE);
                                                        sendDecline.setEnabled(false);
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                sendDecline.setVisibility(View.INVISIBLE);
                sendDecline.setEnabled(false);


                //                a condição para a pessoa não dar send request para si mesma
                if(!sender_user_id.equals(receiver_user_id)){
                    sendRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View mv) {
                            sendRequest.setEnabled(false);

                            if(CURRENT_STATE.equals("not_friends")){
                                sendFriendRequest();
                            }
                            if(CURRENT_STATE.equals("request_sent")){
                                CancelFriendRequest();
                            }
                            if(CURRENT_STATE.equals("request_received")){
                                AcceptFriendRequest();
                            }
                            if(CURRENT_STATE.equals("friends")){
                                UnFriendaFriend();
                            }
                        }
                    });
                }
                else{
                    sendDecline.setVisibility(View.INVISIBLE);
                    sendRequest.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

//   this method will do the user to have 2 options, to accept or to decline
    private void DeclineFriendRequest() {
        FriendRequestRefnce.child(sender_user_id)
                .child(receiver_user_id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            FriendRequestRefnce.child(receiver_user_id)
                                    .child(sender_user_id).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                sendRequest.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
//                                                aqui muda o texto do botão
                                                sendRequest.setText("Send_Friend_Request");

                                                sendDecline.setVisibility(View.INVISIBLE);
                                                sendDecline.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    private void UnFriendaFriend() {
        FriendsReference.child(sender_user_id).child(receiver_user_id)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendsReference.child(receiver_user_id)
                                    .child(sender_user_id)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                sendRequest.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
                                                sendRequest.setText("Send_Friend_Request");
//                                                make the button for decline invisible
                                                sendDecline.setVisibility(View.INVISIBLE);
                                                sendDecline.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void AcceptFriendRequest() {
        Calendar forDatex = Calendar.getInstance();
        SimpleDateFormat currentDatex = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDatex.format(forDatex.getTime());

        FriendsReference.child(sender_user_id)
                .child(receiver_user_id)
                .child("date")
                .setValue(saveCurrentDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//        it will create a nod (friends), for the receiver and the sender to become friends
                        FriendsReference
                                .child(receiver_user_id)
                                .child(sender_user_id)
                                .child("date")
                                .setValue(saveCurrentDate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
//                                        after they become friends, to remove the send request, etc
                                        FriendRequestRefnce
                                                .child(sender_user_id)
                                                .child(receiver_user_id).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()){
                                                            FriendRequestRefnce
                                                                    .child(receiver_user_id)
                                                                    .child(sender_user_id)
                                                                    .removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()){
                                                                                sendRequest.setEnabled(true);
                                                                                CURRENT_STATE = "friends";
//                                                aqui muda o texto do botão
                                                                                sendRequest.setText("Unfriend");

                                                                                sendDecline.setVisibility(View.INVISIBLE);
                                                                                sendDecline.setEnabled(false);
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

    private void CancelFriendRequest() {
        FriendRequestRefnce.child(sender_user_id)
                .child(receiver_user_id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            FriendRequestRefnce.child(receiver_user_id)
                                    .child(sender_user_id).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                sendRequest.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
//                                                aqui muda o texto do botão
                                                sendRequest.setText("Send_Friend_Request");

                                                sendDecline.setVisibility(View.INVISIBLE);
                                                sendDecline.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendFriendRequest() {

        FriendRequestRefnce.child(sender_user_id).child(receiver_user_id)
                .child("request_type")
                .setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful()){
                          FriendRequestRefnce.child("receiver_user_id")
                                  .child("sender_user_id")
                                  .child("resquest_type")
                                  .setValue("received")
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {

                                         if(task.isSuccessful()){

                                             HashMap<String, String> notificationData = new HashMap<String, String>();
                                             notificationData.put("from", sender_user_id);
                                             notificationData.put("type", "request");

                                             NotificationsRef.child(receiver_user_id).push().setValue(notificationData)
                                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
//      if someone send a friend request, the button will be enabled

                                                             if (task.isSuccessful()){
                                                                 sendRequest.setEnabled(true);
                                                                 CURRENT_STATE = "request_sent";
                                                                 sendRequest.setText("Cancel Friend Request");

                                                                 sendDecline.setVisibility(View.INVISIBLE);
                                                                 sendDecline.setEnabled(false);


                                                             }
                                                         }
                                                     });



                                             }
                                                                               }
                                  });
                      }
                    }
                });
    }
}




