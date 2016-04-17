package tk.refract.elert.main.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import tk.refract.elert.main.FunctionControllers.Constants;
import tk.refract.elert.main.FunctionControllers.NotificationView;
import tk.refract.elert.main.R;

import java.util.Timer;


/**
 * Created by s212289853 on 17/04/2016.
 */
public class AlertNotificationService extends Service {

    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            Log.d("ACTION", action);
            switch (action) {
                case Constants.ACTION_NORMAL:
                    showNotification();
                    break;
                case Constants.ACTION_NEARBY:
                    Toast.makeText(AlertNotificationService.this, "Nearby selected", Toast.LENGTH_SHORT).show();
                    alertNotification();
                    break;
                case Constants.ACTION_SET:
                    break;
                case Constants.ACTION_RECEIVED_ALERT:

                    break;
                default:
                    break;
            }
        }
//        receiver = new Receiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constants.ACTION_NORMAL);
//        filter.addAction(Constants.ACTION_NEARBY);
//        registerReceiver(receiver,filter);

        return START_STICKY;
    }

    private void showNotification() {
        NotificationView smlView = new NotificationView(this, getPackageName(), R.layout.sml_notification_normal);
        NotificationView lrgView = new NotificationView(this, getPackageName(), R.layout.lrg_notification_normal);

        Notification status = new Notification.Builder(this).build();
        status.contentView = smlView;
        status.bigContentView = lrgView;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.mipmap.ic_launcher;
        startForeground(Constants.NOTIFICATION_ID, status);
    }

    private void alertNotification() {
        NotificationView smlView = new NotificationView(this, getPackageName(), R.layout.sml_notification_alert);
        Notification status = new Notification.Builder(this).build();
        status.contentView = smlView;
//        status.defaults = Notification.DEFAULT_ALL;
//        long[] longs = {10000,1,10000};
//        status.vibrate = longs;
//
//        status.ledARGB = Color.RED;
//        status.ledOffMS = 0;
//       // status.category = Notification.CATEGORY_ALARM;
//        status.flags = Notification.FLAG_SHOW_LIGHTS;
        status.icon = R.mipmap.ic_launcher;
        //NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //nm.notify(Constants.NOTIFICATION_ID,status);
        stopForeground(true);
        startForeground(Constants.NOTIFICATION_ID, status);
    }


}
