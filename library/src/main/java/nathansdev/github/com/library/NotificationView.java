package nathansdev.github.com.library;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import java.util.Random;

public class NotificationView {

    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "channel_name";
    private static final String CHANNEL_DESCRIPTION = "channel_description";
    private static final String NOTIFICATION_ID = "notificationId";
    private static final String NOTIFICATION_SNOOZE_ACTION = "ACTION_NOTIFICATION_SNOOZE";
    private static final String NOTIFICATION_REPLY_ACTION = "ACTION_NOTIFICATION_REPLY";
    private static final String NOTIFICATION_VIEW_ACTION = "ACTION_NOTIFICATION_VIEW";
    private static final String KEY_TEXT_REPLY = "key_text_reply";

    private NotificationManager notificationManager;
    private final Context context;
    private PendingIntent notifyPendingIntent;
    private Uri notificationSoundUri;

    /**
     * @param application Application instance
     */
    public NotificationView(Context application) {
        this.context = application;
        notificationManager = (NotificationManager)
                application.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        createNotificationChannel();
    }

    public void showNotification(String contentTitle, String contentText, int icon) {
        int notificationId = getNotificationId();

        notifyPendingIntent = PendingIntent.getBroadcast(context,
                notificationId, getNotificationReadIntent(), PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder
                = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setContentIntent(notifyPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(notificationId, mBuilder.build());
    }

    public void showNotificationWithBigText(String contentTitle, String contentText, int icon) {
        int notificationId = getNotificationId();

        notifyPendingIntent = PendingIntent.getBroadcast(context,
                notificationId, getNotificationReadIntent(), PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder
                = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .setContentIntent(notifyPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(notificationId, mBuilder.build());
    }

    public void showNotificationWithSnooze(String contentTitle, String contentText, int notificationIcon,
                                           int notificationActionIcon, Intent snoozeIntent) {
        int notificationId = getNotificationId();

        snoozeIntent.setAction(NOTIFICATION_SNOOZE_ACTION);
        snoozeIntent.putExtra(NOTIFICATION_ID, notificationId);

        notifyPendingIntent = PendingIntent.getBroadcast(context,
                notificationId, getNotificationReadIntent(), PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(notificationIcon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notifyPendingIntent)
                .addAction(notificationActionIcon, context.getString(R.string.snooze),
                        snoozePendingIntent);
        notificationManager.notify(getNotificationId(), mBuilder.build());
    }

    public void showNotificationWithReply(String contentTitle, String contentText, int notificationIcon,
                                          int notificationActionIcon, Intent replyIntent) {
        int notificationId = getNotificationId();
        replyIntent.setAction(NOTIFICATION_REPLY_ACTION);
        replyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        replyIntent.putExtra(NOTIFICATION_ID, notificationId);

        notifyPendingIntent = PendingIntent.getBroadcast(context,
                notificationId, getNotificationReadIntent(), PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(context.getString(R.string.reply))
                .build();

        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(context,
                notificationId, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Action actionReply = new NotificationCompat.Action.Builder
                (notificationActionIcon, context.getString(R.string.reply), replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(notificationIcon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notifyPendingIntent)
                .addAction(actionReply);
        notificationManager.notify(getNotificationId(), mBuilder.build());
    }

    /**
     * create channel for
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Generates notification random id for each notification.
     *
     * @return random id.
     */
    private int getNotificationId() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    private Intent getNotificationReadIntent() {
        Intent notificationIntent = new Intent();
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationIntent.setAction(NOTIFICATION_VIEW_ACTION);
        return notificationIntent;
    }

    /**
     * Generates notification icon.
     *
     * @return random id.
     */
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_notifications_black_24dp : R.drawable.ic_stat_name;
    }
}
