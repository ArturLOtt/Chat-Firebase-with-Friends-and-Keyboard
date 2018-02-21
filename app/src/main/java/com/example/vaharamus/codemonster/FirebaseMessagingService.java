package com.example.vaharamus.codemonster;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Vaharamus on 30/01/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


//    to receive the notification if the app is running or not
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String notification_title = remoteMessage.getNotification().getTitle();
        String notification_body = remoteMessage.getNotification().getBody();
            }

//        String click_action = remoteMessage.getNotification().getClickAction();
////            retrieve the sender id code
//        String from_sender_id = remoteMessage.getData().get("from_sender_id").toString();
//
//
//
//    NotificationCompat.Builder mBuilder =
//            new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(notification_title)
//                    .setContentText(notification_body);
//
//    Intent resultIntent = new Intent(click_action);
//    resultIntent.putExtra("receiver_user_id", from_sender_id);
//
//    PendingIntent resultPendingIntent =
//            PendingIntent.getActivity(
//                    this,
//                    0,
//                    resultIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT
//            );
//
//    mBuilder.setContentIntent(resultPendingIntent);
//
//
//// Sets an ID for the notification
//    int mNotificationId = (int) System.currentTimeMillis();
//// Gets an instance of the NotificationManager service
//    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//// Builds the notification and issues it.
//    mNotifyMgr.notify(mNotificationId, mBuilder.build());

}

