package com.example.vaharamus.codemonster;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vaharamus.codemonster.Pigeon_Post.MessageViewHolder;
import com.example.vaharamus.codemonster.R;
import com.example.vaharamus.codemonster.Model.Messages;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by Vaharamus on 07/02/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Messages> userMessagesList;

    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_layout_of_users, parent, false);

        mAuth = FirebaseAuth.getInstance();



        return new MessageViewHolder(v);
    }




    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
//    getting the online user to see who is online to send message
        String message_sender_id = mAuth.getCurrentUser().getUid();

        Messages messages = userMessagesList.get(position);
// getting the id of the person from who we are sending
        String fromUserId = messages.getFrom();

// if the id is online it means it is me ... ?
        if(fromUserId.equals(message_sender_id)){
            //        this layout will appear for the person who is online
            holder.messageText.setBackgroundResource(R.drawable.message_text_background_two);
            holder.messageText.setTextColor(Color.BLACK);
            holder.messageText.setGravity(Gravity.RIGHT);
        }
        else{
            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.BLACK);
            holder.messageText.setGravity(Gravity.LEFT);



        }



        holder.messageText.setText(messages.getMessage());

        }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

}
