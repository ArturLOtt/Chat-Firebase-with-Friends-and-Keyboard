package com.example.vaharamus.codemonster.Pigeon_Post;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vaharamus.codemonster.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vaharamus on 07/02/2018.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView messageText;
    public CircleImageView userProfileImage;

    public MessageViewHolder(View view){
        super(view);

        messageText = (TextView) view.findViewById(R.id.txtMessageTextks);

//        userProfileImage = (CircleImageView) view.findViewById(R.id.civMessagesProfileImageks);



    }



}
