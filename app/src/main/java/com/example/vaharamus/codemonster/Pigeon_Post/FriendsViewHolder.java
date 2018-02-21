package com.example.vaharamus.codemonster.Pigeon_Post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vaharamus.codemonster.R;

/**
 * Created by Vaharamus on 31/01/2018.
 */

public class FriendsViewHolder extends RecyclerView.ViewHolder{

     public View vizarx;

    public FriendsViewHolder(View itemView) {
        super(itemView);

        vizarx = itemView;

    }

    public void setDatex(String datex){
        TextView sinceFriendsDate = (TextView) vizarx.findViewById(R.id.id_all_statusks);
        sinceFriendsDate.setText("Friends since: \n" + datex);
    }

    public  void setUserName(String userName){
        TextView userNameDisplay = (TextView) vizarx.findViewById(R.id.id_all_usersks);
        userNameDisplay.setText(userName);

    }


    public  void setThumbImage(final String thumbImage, final Context ctx) {
        //            final CircleImageView thumb_image = (CircleImageView) vx.findViewById(R.id.all_profile_imageks);
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

    public void setUserOnline(String online_status) {
//        retrieving the true and false value in this variable Online_status
        ImageView onlineStatusView = (ImageView) vizarx.findViewById(R.id.online_statusks);

        if(online_status.equals("true")){
            onlineStatusView.setVisibility(vizarx.VISIBLE);
        }
        else{
            onlineStatusView.setVisibility(vizarx.INVISIBLE);
        }
    }
}
