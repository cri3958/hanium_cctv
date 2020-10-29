package com.hanium.cctv.others;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String file_notification = "data_notification.txt";

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("FCM Log", "Refreshed token: " + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String title, String body) {
        String[] object_num = body.split("번");
        DbHelper dbHelper = new DbHelper(this);
        Boolean checkresult = dbHelper.checkCCTVLIST(object_num[0]);
        boolean notification = false;

        File fFile1 = new File("/data/data/com.hanium.cctv/files/" + file_notification);// notifcation 데이터 불러오기
        if (fFile1.exists()) {
            try {
                FileInputStream inFs = openFileInput(file_notification);
                byte[] txt = new byte[500];
                inFs.read(txt);
                if (new String(txt).trim().equals("true"))
                    notification = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (checkresult & notification) { //DB에 저장된 CCTV번호
            Intent intent = new Intent(this, cctv_watch_emergency.class);
            String[] reason = body.split("에서 ");
            intent.putExtra("object_num", object_num[0]);
            intent.putExtra("reason", reason[1]);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            String chId = "test";

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 알림 왔을때 사운드.

            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, chId)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 10초뒤에 알림이 안없어졌으면 강제로 화면 전환기능 추가 >> 어떻게....

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String chName = "ch name";
                NotificationChannel channel = new NotificationChannel(chId, chName, NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, notiBuilder.build());
        } else {
            Log.d("False route", "this user don't add " + object_num[0] + "CCTV!");
        }

    }
}