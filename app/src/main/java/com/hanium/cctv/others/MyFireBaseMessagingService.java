package com.hanium.cctv.others;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hanium.cctv.R;
import com.hanium.cctv.cctv.cctv_watch_emergency;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("FCM Log", "Refreshed token: " + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, cctv_watch_emergency.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String[] info = body.split("@");
        DbHelper dbHelper = new DbHelper(this);
        Boolean checkresult = dbHelper.checkCCTVLIST(info[0]);

        if (checkresult) {
            String chId = "test";

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 알림 왔을때 사운드.

            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, chId)
                    .setSmallIcon(R.drawable.cctv)
                    .setContentTitle("응급상황 감지")
                    .setContentText("확인바랍니다.")
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 10초뒤에 알림이 안없어졌으면 강제로 화면 전환기능 추가 >> 어떻게....

            Notification notification = notiBuilder.build();
            notification.sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.test_sound);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String chName = "ch name";
                NotificationChannel channel = new NotificationChannel(chId, chName, NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, notiBuilder.build());
        } else {
            Log.d("False route", "get miss notification");
        }

    }
}