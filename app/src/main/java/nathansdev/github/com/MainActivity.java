package nathansdev.github.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import nathansdev.github.com.library.NotificationView;

public class MainActivity extends AppCompatActivity {
    private NotificationView notificationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button showNormal = findViewById(R.id.show_normal);
        Button showBig = findViewById(R.id.show_big);
        Button showSnooze = findViewById(R.id.show_snooze);
        Button showReply = findViewById(R.id.show_reply);
        notificationView = new NotificationView(getApplicationContext());
        showNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationView.showNotification("title",
                        getString(R.string.small_text), getNotificationIcon());
            }
        });
        showBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationView.showNotificationWithBigText("title",
                        getString(R.string.big_text), getNotificationIcon());
            }
        });
        showSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent snoozeIntent = new Intent(MainActivity.this, MyNotificationSnoozeReceiver.class);
                notificationView.showNotificationWithSnooze("title", "hi",
                        getNotificationIcon(), getNotificationIcon(), snoozeIntent);
            }
        });
        showReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent(MainActivity.this, MyNotificationReplyReceiver.class);
                notificationView.showNotificationWithReply("title", "hi",
                        getNotificationIcon(), getNotificationIcon(), replyIntent);
            }
        });
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_notifications_black_24dp : R.drawable.ic_notification;
    }
}
