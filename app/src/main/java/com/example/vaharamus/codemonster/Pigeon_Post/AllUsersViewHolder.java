package com.example.vaharamus.codemonster.Pigeon_Post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vaharamus.codemonster.R;

/**
 * Created by Vaharamus on 17/01/2018.
 */

public class AllUsersViewHolder extends RecyclerView.ViewHolder {

    public View vx;

    public AllUsersViewHolder(View itemView) {
        super(itemView);

        vx = itemView;
    }
// this view will be used for the firebase adapter

        public void setUser_name(String user_name){
            TextView name = (TextView) vx.findViewById(R.id.id_all_usersks);
            name.setText(user_name);
        }


        public void setUser_status(String user_status){
            TextView status = (TextView) vx.findViewById(R.id.id_all_statusks);
            status.setText(user_status);
        }
//
        public void setUser_thumb_image(final Context ctx, final String user_thumb_image){
//            final CircleImageView thumb_image = (CircleImageView) vx.findViewById(R.id.all_profile_imageks);
//
////         to display the picture offline
//            Picasso.with(ctx).load(user_thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
//                .placeholder(R.drawable.default_image)
//                .into(thumb_image, new Callback() {
//                    @Override
//                     public void onSuccess(){
//                      }
//
//                     @Override
//                      public void OnError() {
//
    //            Picasso.with(ctx).load(user_thumb_image).placeholder(R.drawable.default_image).into(thumb_image);
//                      }
//                      });
//
//                   }
//
//
        }


}


