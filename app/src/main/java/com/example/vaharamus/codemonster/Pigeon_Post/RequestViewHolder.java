package com.example.vaharamus.codemonster.Pigeon_Post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vaharamus.codemonster.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vaharamus on 08/02/2018.
 */

public
//static
class RequestViewHolder extends RecyclerView.ViewHolder {

    public View maviw;

    public RequestViewHolder(View itemView) {
        super(itemView);

        maviw = itemView;

    }

    public void setUserName(String userName) {

        TextView userNameDisplay = (TextView) maviw.findViewById(R.id.txtRequestProfileNameks);
        userNameDisplay.setText(userName);
         }

    public void setThumb_user_image(final String thumbImage, final Context ctx) {

//            final CircleImageView thumb_image = (CircleImageView) maviw.findViewById(R.id.civRequestProfileImageks);
//
////         to display the picture offline
//            Picasso.with(ctx).load(thumbImage).networkPolicy(NetworkPolicy.OFFLINE)
//                .placeholder(R.drawable.default_image)
//                .into(thumb_image, new Callback() {
//                    @Override
//                     public void onSuccess(){
//                      }
//
//                     @Override
//                      public void OnError() {
//
        //            Picasso.with(ctx).load(thumbImage).placeholder(R.drawable.default_image).into(thumb_image);
//                      }
//                      });


    }

    public void setUser_Status(String userStatus) {

        TextView status = (TextView) maviw.findViewById(R.id.txtRequestProfileStatusks);
        status.setText(userStatus);


    }
}
