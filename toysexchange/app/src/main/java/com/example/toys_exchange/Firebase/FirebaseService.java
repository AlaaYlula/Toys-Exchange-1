package com.example.toys_exchange.Firebase;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.toys_exchange.MainActivity;
import com.example.toys_exchange.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        StringBuilder message = new StringBuilder();

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d("Amazon SNS Test", "Amazon SNS message received - Message data payload: " + remoteMessage.getData());
//            String messageString = remoteMessage.getData().get("message");
//            if(messageString != null)
//            {
//                message.append(messageString);
//            }
//            else
//            {
//                //Try to get the default message if the notification does not have the "message" attribute
//                message.append( remoteMessage.getData().get("default"));
//            }
//
//            displayNotification(message.toString());
//        }

    }


    /**
     * Create and show a simple notification containing the received message.
     *
     * @param messageBody FCM message body received.
     */
    private void displayNotification(String messageBody) {
        // The notification will go to the test screen
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Sets the text to display and sound to play when a notification is received
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Amazon SNS Test")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Generates a unique number so that multiple notifications can be displayed at one time
        int unique_number = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        // Displays the message
        notificationManager.notify(unique_number, notificationBuilder.build());
    }

}
