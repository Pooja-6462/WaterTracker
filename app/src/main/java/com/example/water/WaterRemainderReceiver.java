package com.example.water;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;

public class WaterRemainderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "water_reminder_channel";

        // Create notification channel for Android 8+
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Water Reminder",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Reminds you to drink water");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }

        // Open app when notification clicked
        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, openIntent,
                PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        Notification notification = new NotificationCompat.Builder(
                context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("💧 Time to Drink Water!")
                .setContentText("Stay hydrated! Drink a glass of water now.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You need to stay hydrated! " +
                                "Drink at least a glass of water now. " +
                                "Your body will thank you! 💧"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 200, 500})
                .setColor(Color.BLUE)
                .build();

        manager.notify(1, notification);
    }
}