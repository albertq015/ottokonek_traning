package com.otto.mart.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.otto.mart.OttoMartApp;
import com.otto.mart.R;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity;
import com.otto.mart.ui.activity.transaction.history.HistoryActivity;

import java.lang.ref.WeakReference;
import java.util.Map;

//import in.netcore.smartechfcm.Smartech;

public class NotificationReceiverService extends FirebaseMessagingService {

    private final String TAG = this.getClass().getSimpleName();

    private final String KEY_NOTIF_TARGET_TRANSACTION = "transaction history";
    private final String KEY_NOTIF_TARGET_HISTORY_OMZET = "history_omzet";
    private final String KEY_NOTIF_TARGET_HISTORY_DEPOSIT = "history_deposit";
    private final String KEY_NOTIF_TARGET_HISTORY_OC = "history_oc";
    private final String KEY_NOTIF_TARGET_EARNING_POINT = "earning point";
    private final String KEY_NOTIF_TARGET_PAYMENT = "payment_qr";
    private final String KEY_NOTIF_TARGET_TOP_UP_DEPOSIT = "top_up_deposit";

    public static final String KEY_INTENT_HISTORY = "NOTIF_HISTORY";
    public static final String KEY_INTENT_HISTORY_OC = "NOTIF_HISTORY_OC";
    private final String KEY_INTENT_OTHERS = "NOTIF_OTHERS";
    private boolean setCustomSound = false;

    @Override
    public void onNewToken(String token) {
        Log.d("Firebase", "Firebase Token (On New Token) " + token);
        SessionManager.setFirebaseToken(token);
        //Smartech.getInstance(new WeakReference<>(this)).setDevicePushToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //boolean isFromNetcore =  false;//Smartech.getInstance(new WeakReference<Context>(this)).handlePushNotification(remoteMessage.getData().toString());
        setCustomSound = false;

        //if (!isFromNetcore) {
            super.onMessageReceived(remoteMessage);

            Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.ottopay_notification_ringtone);

            PendingIntent pendingIntent = null;
            String target = remoteMessage.getData().get("target");

            if (target != null && OttoMartApp.getContext() != null) {
                if (target.equalsIgnoreCase(KEY_NOTIF_TARGET_TRANSACTION)) {
                    Intent intent = new Intent(KEY_INTENT_HISTORY);
                    setCustomSound = true;
                    intent.putExtra(HistoryActivity.KEY_TITLE, HistoryActivity.TAB_LABEL_OMZET);
                    intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, false);
                    pendingIntent = getPendingIntent(intent);
                } else if (target.equalsIgnoreCase(KEY_NOTIF_TARGET_TOP_UP_DEPOSIT)) {
                    Intent intent = new Intent(KEY_INTENT_HISTORY);
                    setCustomSound = true;
                    intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, false);
                    pendingIntent = getPendingIntent(intent);
                } else if (target.equalsIgnoreCase(KEY_NOTIF_TARGET_HISTORY_DEPOSIT)) {
                    Intent intent = new Intent(KEY_INTENT_HISTORY);
                    intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, false);
                    pendingIntent = getPendingIntent(intent);
                } else if (target.equalsIgnoreCase(KEY_NOTIF_TARGET_HISTORY_OMZET)) {
                    Intent intent = new Intent(KEY_INTENT_HISTORY);
                    intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, true);
                    pendingIntent = getPendingIntent(intent);
                } else if (target.equalsIgnoreCase(KEY_NOTIF_TARGET_PAYMENT)) {
                    Intent intent = new Intent(KEY_INTENT_HISTORY);
                    setCustomSound = true;
                    intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, true);
                    pendingIntent = getPendingIntent(intent);
                } else if (target.equalsIgnoreCase(KEY_NOTIF_TARGET_HISTORY_OC)) {
                    Intent intent = new Intent(KEY_INTENT_HISTORY_OC);
                    pendingIntent = getPendingIntent(intent);
                } else if (target.equalsIgnoreCase(KEY_NOTIF_TARGET_EARNING_POINT)) {
                    Intent intent = new Intent(KEY_INTENT_OTHERS);
                    intent.putExtra("NOTIF_EARNING_POINT", true);
                    pendingIntent = getPendingIntent(intent);
                } else {
                    Intent intent = new Intent(OttoMartApp.getContext(), DashboardActivity.class);
                    intent.putExtra(DashboardActivity.KEY_NOTIF_INBOX, true);
                    pendingIntent = getPendingIntent(intent);
                }
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Ottopay4")
                    .setSmallIcon(R.drawable.ic_ottopay_logo)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    // .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW);


            if (pendingIntent != null) {
                mBuilder.setContentIntent(pendingIntent);
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            //Force Disable custom sound
            setCustomSound = false;

            if(setCustomSound) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AudioAttributes attributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build();

                    NotificationChannel channel = new NotificationChannel("Ottopay4",
                            this.getPackageName(),
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setSound(NOTIFICATION_SOUND_URI, attributes);
                    notificationManager.createNotificationChannel(channel);
                } else {
                    mBuilder.setSound(NOTIFICATION_SOUND_URI);
                }
            }

            notificationManager.notify(generateRandomIdNotification(), mBuilder.build());
    }

    private PendingIntent getPendingIntent(Intent intent){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    private int generateRandomIdNotification() {
        return (int) System.currentTimeMillis();
    }

    private void showBodyNotification(Map<String, String> data) {
        for (String key : data.keySet())
            LogHelper.showError(TAG, "notif body, key: " + key + ", data: " + data.get(key));
    }

}