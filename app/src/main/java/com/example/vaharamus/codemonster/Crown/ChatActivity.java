package com.example.vaharamus.codemonster.Crown;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vaharamus.codemonster.LastSeenTime;
import com.example.vaharamus.codemonster.MessageAdapter;
import com.example.vaharamus.codemonster.Model.Messages;
import com.example.vaharamus.codemonster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.vaharamus.codemonster.Crown.SettingsActivity.Gallery_pickx;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverId;
    private String messageReceiverName;

    private Toolbar ChatToolBar;

    private TextView userNameTitle;
    private TextView userLastSeen;
    private CircleImageView userChatProfileImage;

    private ImageButton SendMessageButton;
    private ImageButton SelectImageButton;
    private EditText InputMessageText;

    private DatabaseReference rootRef;

    private FirebaseAuth mAuth;
    private String messageSenderId;

    private RecyclerView userMessageList;

    private final List<Messages> messagesList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    private MessageAdapter messageAdapter;

    private static int Gallery_Pick = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rootRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();

        messageReceiverId = getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName = getIntent().getExtras().get("user_name").toString();

        ChatToolBar = (Toolbar) findViewById(R.id.chat_bar_layout);
        setSupportActionBar(ChatToolBar);

//        add the action button to the activityChat
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar,  null);

//        to conect the action bar to the custom view
        actionBar.setCustomView(action_bar_view);

        userNameTitle = (TextView) findViewById(R.id.txtCustomProfileNameks);
        userLastSeen = (TextView) findViewById(R.id.txtCustomLastSeenks);
        userChatProfileImage = (CircleImageView)findViewById(R.id.imgCustomProfileImageks);

        SendMessageButton = (ImageButton) findViewById(R.id.btnSendMessageks);
        SelectImageButton = (ImageButton) findViewById(R.id.btnSelectImageks);
        InputMessageText = (EditText) findViewById(R.id.txtInputMessageks);

        messageAdapter = new MessageAdapter(messagesList);

        userMessageList = (RecyclerView) findViewById(R.id.rvMessageListUsersks);

        linearLayoutManager = new LinearLayoutManager(this);

        userMessageList.setHasFixedSize(true);
        userMessageList.setLayoutManager(linearLayoutManager);
        userMessageList.setAdapter(messageAdapter);

//method to retrieve the data for the messages
        FetchMessages();



//        set the username
        userNameTitle.setText(messageReceiverName);


        rootRef.child("Users").child(messageReceiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String online = dataSnapshot.child("online").getValue().toString();
                final String userThumb = dataSnapshot.child("user_thumb_image").getValue().toString();

////         to display the picture offline
//            Picasso.with(ChatActivity.this).load(userThumb)
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .placeholder(R.drawable.default_image)
//                .into(userChatProfileImage, new Callback() {
//                    @Override
//                     public void onSuccess(){
//                      }
//
//                     @Override
//                      public void OnError() {
//
                //            Picasso.with(ChatActivity).load(userThumb)
                // .placeholder(R.drawable.default_image).into(userChatProfileImage);
//                      }
//                      });

//                it means the suer is online
        if(online.equals("true")){
            userLastSeen.setText("online");
        }
        else{

            LastSeenTime getTime = new LastSeenTime();

            long last_seen = Long.parseLong(online);

            String lastSeenDisplayTime = getTime
                    .getTimeAgo(last_seen, getApplicationContext()).toString();

            userLastSeen.setText(lastSeenDisplayTime);
        }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });


        SelectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//   for the user to open the gallery from his mobile phone
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);

            }
        });

////        these toast to check if it's working or not
//        Toast.makeText(ChatActivity.this, "messageReceiverId",
//                Toast.LENGTH_SHORT).show();
//
//        Toast.makeText(ChatActivity.this, "messageReceiverName",
//                Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//       the 'if' condition to check the request code which it will select from the galley

        if (requestCode == Gallery_pickx && resultCode==RESULT_OK &&data !=null) {
// esse método são as funcionalidds do botão Crop
            Uri ImageUri = data.getData();

        }

    }



    private void FetchMessages() {
//        by using the rootFer it can point to the messages nod(?)
        rootRef
                .child("Messages")
                .child(messageSenderId)
                .child(messageReceiverId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Messages messages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



    }

    private void SendMessage() {
        //                retrieve the text from our editText
        String messageText = InputMessageText.getText().toString();

//                validation if user leave the message box empty
        if(TextUtils.isEmpty(messageText)) {

            Toast.makeText(ChatActivity.this, "Please write your message, "
                    , Toast.LENGTH_LONG).show();
        }
        else{
            String message_sender_ref = "Messages/"
                + messageSenderId + "/" + messageReceiverId;

            String message_receiver_ref = "Messages/"
                    + messageReceiverId + "/" + messageSenderId;

//            create an unic random key for user specific message to avoid mixing messages
            DatabaseReference user_message_key = rootRef
                    .child("Messages")
                    .child(messageSenderId)
                    .child(messageReceiverId)
                    .push();

//            store this key in a string
            String message_push_id = user_message_key.getKey();

//            to store in the database
            Map messageTextBody = new HashMap();
//            by using this messageTextBody it can save the data in the database
            messageTextBody.put("message", messageText);
            messageTextBody.put("seen", false);
            messageTextBody.put("type", "text");
            messageTextBody.put("time", ServerValue.TIMESTAMP);
            messageTextBody.put("from", messageSenderId);

            Map messageBodyDetails = new HashMap();

//                    using this messageBodyDetails it will store the reference and the data
                messageBodyDetails.put(message_sender_ref
                        + "/" + message_push_id, messageTextBody);

            messageBodyDetails.put(message_receiver_ref
                    + "/" + message_push_id, messageTextBody);


//            to store in the dataBase
            rootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

//                    if he gets any error it will display in the log
                    if(databaseError != null){
                        Log.d("Chat_Log", databaseError.getMessage().toString());
                    }

                    InputMessageText.setText("");

                }
            });

        }
    }

}
