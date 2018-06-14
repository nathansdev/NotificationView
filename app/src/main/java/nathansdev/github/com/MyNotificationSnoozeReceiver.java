package nathansdev.github.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyNotificationSnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("snoozeReceiver", intent.getAction());
        //doSnoozeNotification();
    }
}
