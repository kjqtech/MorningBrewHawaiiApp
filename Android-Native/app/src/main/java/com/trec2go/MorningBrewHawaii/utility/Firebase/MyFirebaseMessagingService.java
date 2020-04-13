package com.trec2go.MorningBrewHawaii.utility.Firebase;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//Package name to be changed

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.activity.AlertDialogActivity;
import com.trec2go.MorningBrewHawaii.activity.SplashActivity;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import java.util.Iterator;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Context context;
    Handler mHandler;
    Activity activity;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        context = getApplicationContext();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Utility.log("Message data payload: " + remoteMessage.getData());
            if (isAppRunning()) {
                showPopup(remoteMessage);
            } else {
                sendNotification(remoteMessage);
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Preference.setString(getApplicationContext(), Preference.FCM_token, s);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("FROM_NOTIFICATION", "yes");
        intent.putExtra("NOTIFICATION_TITLE", remoteMessage.getData().get("title"));
        intent.putExtra("NOTIFICATION_BODY", remoteMessage.getData().get("body"));
        PendingIntent pandingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);


        String channelId = getResources().getString(R.string.app_name);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId);
        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(Preference.getString(context, Preference.GLOBAL_UNAME).equalsIgnoreCase("MorningBrewHawaii")){
                notificationBuilder.setSmallIcon(R.drawable.logo_transperent);
            }else {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            }
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }*/
        notificationBuilder.setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pandingIntent)
                .setOnlyAlertOnce(false)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true);

        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= 21) {
            notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000});
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(getResources().getString(R.string.app_name));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);

        }

        notificationManager.notify(0, notificationBuilder.build());
    }


    private void showPopup(final RemoteMessage remoteMessage) {
        Intent intent = new Intent(context, AlertDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.putExtra("NOTIFICATION_TITLE", remoteMessage.getData().get("title"));
        intent.putExtra("NOTIFICATION_BODY", remoteMessage.getData().get("body"));
        startActivity(intent);

      /*  new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Hello");
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.show();
            }
        });*/

        /*SweetAlertDialog.sweetPopup(this, SweetAlertDialog.NORMAL_TYPE, "Hi Jaina ! Welcome to Asferi Collection", new DialogCallBackListner() {
            @Override
            public void setPositiveListner() {
                //Cliked Ok
            }

            @Override
            public void setNegativeListner() {

            }
        });
*/
    }

    private boolean isAppRunning() {
        ActivityManager m = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = m.getRunningTasks(10);
        Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
        int n = 0;
        while (itr.hasNext()) {
            n++;
            itr.next();
        }
        if (n == 1) { // App is killed
            return false;
        }

        return true; //App is in background or foreground
    }

}
