package com.codeshastra.coderr.provideameal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.text.DateFormat;
import java.util.Date;

public class MyGcmPushReceiver extends GcmListenerService {
Message msg;
    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();
    private NotificationUtils notificationUtils;
    private String notifMessage = "New donate requests available";

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String title ="Provide a Meal";
        String emessage = bundle.getString("message");
        String name = bundle.getString("name");
        String address = bundle.getString("address");
        String contact = bundle.getString("number");
        String meals = bundle.getString("meals");
        String email = bundle.getString("email");
        String message= String.valueOf(Html.fromHtml(emessage));
        String time = DateFormat.getDateTimeInstance().format(new Date());
        // String image = bundle.getString("image");
        // String timestamp = bundle.getString("created_at");
        Log.e(TAG, "From: " + from);
        Log.e(TAG, "Title: " + name);
        Log.e(TAG, "message: " + address);
        Log.e(TAG, "time: " + contact);
        // Log.e(TAG, "image: " + image);
        // Log.e(TAG, "timestamp: " + timestamp);
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", notifMessage);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            Intent resultIntent = new Intent(getApplicationContext(), ListActivity.class);
            resultIntent.putExtra("message", message);
            showNotificationMessage(getApplicationContext(), title, notifMessage, resultIntent);
            // play notification sound
            // NotificationUtils notificationUtils = new NotificationUtils();
            // notificationUtils.playNotificationSound();
        } else {
            Intent resultIntent = new Intent(getApplicationContext(), ListActivity.class);
            resultIntent.putExtra("message", message);
            showNotificationMessage(getApplicationContext(), title, notifMessage, resultIntent);
        }

        MessagesDataSource m = new MessagesDataSource(this);
        m.open();
        msg= m.createMessage(name, address, contact, meals, email);
        m.close();
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }
}