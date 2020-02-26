package sv.ugm.komsi.notificationappb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button mNotifyButton;
    Button mUpdateButton;
    Button mCancelButton;
    private NotificationManager mNotifyManager;
    private static final String CHANNEL_ID="ch1";
    private static final int NOTIFICATION_ID=0;
    private static  final String NOTIF_URL="https://www.google.com/search?q=google+translate&oq=goo&aqs=chrome.0.69i59j69i57j69i60l3j69i65l2j69i60.626j0j7&sourceid=chrome&ie=UTF-8";
    private static final String ACTION_UPDATE_NOTIFICATION="sv.ugm.komsi.notificationappb.ACTION_UPDATE_NOTIFICATION";
    private static final String ACTION_CANCEL_NOTIFICATION="sv.ugm.komsi.notificationappb.ACTION_CANCEL_NOTIFICATION";
    private NotificationReceiver mReciever=new NotificationReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotifyButton=findViewById(R.id.button_notify);
        mUpdateButton=findViewById(R.id.button_update);
        mCancelButton=findViewById(R.id.button_cancel);
        mNotifyManager=(NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ACTION_UPDATE_NOTIFICATION);
        intentFilter.addAction(ACTION_CANCEL_NOTIFICATION);
        registerReceiver(mReciever,intentFilter);
        mNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            sendNotification();
            }
        });
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            updateNotification();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();

            }
        });
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID,"Notification",
                            NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Descriptions....");
            channel.enableVibration(true);
            channel.enableLights(true);
            mNotifyManager.createNotificationChannel(channel);
        }
    }
    private void sendNotification(){
        Intent notifIntent=new Intent(this,MainActivity.class);
        PendingIntent notifPendingIntent=
                PendingIntent.getActivity(this,NOTIFICATION_ID,
                        notifIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Intent moreIntent=new Intent(Intent.ACTION_VIEW,
                Uri.parse(NOTIF_URL));
        PendingIntent morePendingIntent=PendingIntent.getActivity(
                this,NOTIFICATION_ID,moreIntent,PendingIntent.FLAG_ONE_SHOT);//sekali aktivitas

        Intent updateIntent=new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent=PendingIntent.getBroadcast(
                this,NOTIFICATION_ID,updateIntent,PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notifyBuilder =
               new NotificationCompat.Builder(this,CHANNEL_ID)//untuk API 26 perlu CHANNEL_ID
                .setContentTitle("Notification Title")
                       .setContentText("Notification Text")
                            .setSmallIcon(R.drawable.ic_notif)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(morePendingIntent)
                                    .addAction(R.drawable.ic_notif,"LEARN MORE",morePendingIntent)
                                    .addAction(R.drawable.ic_notif,"UPDATE",updatePendingIntent);

        Notification myNotification=notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID,myNotification);


    }
    private void updateNotification(){
        Bitmap image= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        Intent cancelIntent = new Intent(ACTION_CANCEL_NOTIFICATION);
        PendingIntent cancelPendingIntent=PendingIntent.getBroadcast(
                this,NOTIFICATION_ID,cancelIntent,PendingIntent.FLAG_ONE_SHOT
        );

        Intent notifIntent=new Intent(this,MainActivity.class);
        PendingIntent notifPendingIntent=
                PendingIntent.getActivity(this,NOTIFICATION_ID,
                        notifIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder =
                new NotificationCompat.Builder(this,CHANNEL_ID)//untuk API 26 perlu CHANNEL_ID
                        .setContentTitle("Notification Title")
                        .setContentText("Notification Text")
                        .setSmallIcon(R.drawable.ic_notif)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(image)
                                .setBigContentTitle("Notification Updated!"))
                                .setContentIntent(notifPendingIntent)
                                .addAction(R.drawable.ic_notif,"Learn More",notifPendingIntent)
                                .addAction(R.drawable.ic_notif,"Cancel",cancelPendingIntent);

        Notification myNotification=notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID,myNotification);
    }
    private void cancelNotification(){
         mNotifyManager.cancel(NOTIFICATION_ID);
    }
    private class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch (action){
                case ACTION_UPDATE_NOTIFICATION:
                    updateNotification();
                    break;
                case ACTION_CANCEL_NOTIFICATION:
                    cancelNotification();
                    break;
            }
        }
    }
}
//untuk API 26 keatas menggunakan channel
