package tk.refract.elert.main.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
import tk.refract.elert.main.FunctionControllers.Constants;
import tk.refract.elert.main.FunctionControllers.NotificationView;
import tk.refract.elert.main.R;

import java.util.Date;
import java.util.Timer;


/**
 * Created by s212289853 on 17/04/2016.
 */
public class AlertNotificationService extends Service {

    private Timer timer;
    private Vibrator v;
    private PowerManager powerMan;
    private PowerManager.WakeLock wakeLock;
    private AudioManager am;
    private MediaPlayer mp;


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
                    if (mp != null) {
                        mp.stop();
                    }
                    break;
                case Constants.ACTION_NEARBY:
                    Toast.makeText(AlertNotificationService.this, "Nearby selected", Toast.LENGTH_SHORT).show();

                    Intent intentNearby = new Intent(this.getApplicationContext(), AlertNotificationService.class);
                    intentNearby.setAction(Constants.ACTION_RECEIVED_ALERT);
                    intentNearby.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getService(this, Constants.REQUEST_CODE, intentNearby, 0);
                    try {
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ACTION_SET:
                    break;
                case Constants.ACTION_RECEIVED_ALERT:
                    alertReceived();
                    alertNotification();
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

    private void alertReceived() {
        Date date = new Date();

        //Constants.ldb.insertNotification(new tk.refract.elert.main.FunctionControllers.Notification("515",((HomeActivity)getApplicationContext()).getLocation(),date.toString(),getApplicationContext()));
    }

    private void alertNotification() {
//        v = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//        powerMan = (PowerManager) getSystemService(POWER_SERVICE);
//        wakeLock = powerMan.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakelockTag");
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        mp = MediaPlayer.create(this, R.raw.scream);

        NotificationView smlView = new NotificationView(this, getPackageName(), R.layout.sml_notification_alert);
        Notification status = new Notification.Builder(this).build();
        status.contentView = smlView;

        //v.vibrate(60*1000);
        playSound();

        status.category = Notification.CATEGORY_SYSTEM;

        status.icon = R.mipmap.ic_launcher;

        status.vibrate = new long[]{0, 60000};
        status.ledARGB = Color.RED;
        status.priority = Notification.PRIORITY_MAX;
        status.ledOnMS = 500;
        status.ledOffMS = 300;
        status.flags |= Notification.FLAG_SHOW_LIGHTS;
        //NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //nm.notify(Constants.NOTIFICATION_ID,status);
        stopForeground(true);
        startForeground(Constants.NOTIFICATION_ID, status);
    }


    private void playSound() {
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }


}
