package com.example.mymoneytracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    static final String CHANNEL_ID = "1";
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        final Timer timer = new Timer(true);
        createNotificationChannel();
        final Intent intent = new Intent(getApplicationContext(), LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("MyMoney Tracker App")
                .setContentText("Keep tracking your money using MyMoney Tracker App.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int notifId = 0;
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                notificationManagerCompat.notify(notifId, notificationBuilder.build());
            }
        }, 10000);
        stopSelf();
        super.onCreate();
    }
    void createNotificationChannel(){
        String channel_name = String.valueOf("MyMoney Tracker Notification Channel");
        String channel_description = String.valueOf("MyMoneyTracker");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channel_description);
            NotificationManager gameNotificationManager = getSystemService(NotificationManager.class);
            gameNotificationManager.createNotificationChannel(channel);
        }
    }


}