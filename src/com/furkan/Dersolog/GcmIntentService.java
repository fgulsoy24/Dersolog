package com.furkan.Dersolog;
import com.google.android.gms.gcm.GoogleCloudMessaging;

 
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
  
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
  
    public GcmIntentService() {
        super("GcmIntentService");
    }
  
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        //Gelen mesaj tipini al�yoruz
        String messageType = gcm.getMessageType(intent);
        String mesaj = intent.getExtras().getString("notification_message");
        Log.d("mesaj--->>>",mesaj);
         
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
             
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {//Herhangi bir sorun yoksa Notification m�z� olu�turacak methodu �a��r�yoruz
                sendNotification(mesaj);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
 
    private void sendNotification(String msg) { //Burda Status barda g�sterilecek Notification�n ayarlar� yap�l�yor(titre�im,bildirim,text boyutu vs..)
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
  
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SplashScreen.class), 0);
  
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setContentTitle("Dersolog")
        .setSmallIcon(R.drawable.booklogoo)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
  
         
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());//Notification g�steriliyor.
    }
}