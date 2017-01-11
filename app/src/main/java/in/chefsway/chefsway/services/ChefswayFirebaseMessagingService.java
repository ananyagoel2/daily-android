package in.chefsway.chefsway.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import in.chefsway.chefsway.R;
import in.chefsway.chefsway.ui.ProfileActivity;

public class ChefswayFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("OnMessageReceived","Called");
        //Log.e("remoteMesage","thIS"+(new Gson()).toJson(remoteMessage));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setContentTitle(remoteMessage.getData().get("name"));
        builder.setSmallIcon(R.drawable.ic_done);

        Intent resultIntent = new Intent(this, ProfileActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ProfileActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

        notificationManager.notify(0,builder.build());

    }
}
