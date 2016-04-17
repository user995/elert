package tk.refract.elert.main.FunctionControllers;

import android.app.PendingIntent;
import android.content.Intent;
import android.widget.RemoteViews;
import tk.refract.elert.main.HomeActivity;
import tk.refract.elert.main.R;
import tk.refract.elert.main.Services.AlertNotificationService;

/**
 * Created by user9_000 on 2016-04-17.
 */
public class NotificationView extends RemoteViews {
    private final AlertNotificationService mContext;

    public NotificationView(AlertNotificationService context, String packageName, int layoutId) {
        super(packageName, layoutId);
        mContext = context;

        if (R.layout.sml_notification_normal == layoutId)
            setupSmallNotification();
        else if (R.layout.lrg_notification_normal == layoutId)
            setupLargeNotification();
        else if (R.layout.sml_notification_alert == layoutId)
            setupSmallAlertNotification();
    }

    private void setupSmallNotification() {
        Intent intentNearby = new Intent(mContext.getApplicationContext(), AlertNotificationService.class);
        intentNearby.setAction(Constants.ACTION_NEARBY);
        intentNearby.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentNearby = PendingIntent.getService(mContext, Constants.REQUEST_CODE, intentNearby, 0);
        setOnClickPendingIntent(R.id.ivSmlNotiClosest, pendingIntentNearby);

        Intent intentSet = new Intent(mContext.getApplicationContext(), AlertNotificationService.class);
        intentSet.setAction(Constants.ACTION_SET);
        intentNearby.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentSet = PendingIntent.getService(mContext, Constants.REQUEST_CODE, intentSet, 0);
        setOnClickPendingIntent(R.id.ivSmlNotiSet, pendingIntentSet);

        Intent intentOpen = new Intent(mContext.getApplicationContext(), HomeActivity.class);
        intentOpen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentOpen = PendingIntent.getActivity(mContext, Constants.REQUEST_CODE, intentOpen, 0);
        setOnClickPendingIntent(R.id.SmlNotiLayout, pendingIntentOpen);
    }

    private void setupLargeNotification() {
        Intent intentNearby = new Intent(mContext.getApplicationContext(), AlertNotificationService.class);
        intentNearby.setAction(Constants.ACTION_NEARBY);
        intentNearby.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentNearby = PendingIntent.getService(mContext, Constants.REQUEST_CODE, intentNearby, 0);
        setOnClickPendingIntent(R.id.rlLrgNotiNear, pendingIntentNearby);

        Intent intentSet = new Intent(mContext.getApplicationContext(), AlertNotificationService.class);
        intentSet.setAction(Constants.ACTION_SET);
        intentNearby.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentSet = PendingIntent.getService(mContext, Constants.REQUEST_CODE, intentSet, 0);
        setOnClickPendingIntent(R.id.rlLrgNotiSet, pendingIntentSet);

        Intent intentOpen = new Intent(mContext.getApplicationContext(), HomeActivity.class);
        intentOpen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentOpen = PendingIntent.getActivity(mContext, Constants.REQUEST_CODE, intentOpen, 0);
        setOnClickPendingIntent(R.id.LrgNotiLayout, pendingIntentOpen);
    }

    private void setupSmallAlertNotification() {
        Intent intentOpen = new Intent(mContext.getApplicationContext(), HomeActivity.class);
        intentOpen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentOpen = PendingIntent.getActivity(mContext, Constants.REQUEST_CODE, intentOpen, 0);
        setOnClickPendingIntent(R.id.SmlNotiAlertLayout, pendingIntentOpen);

        // TODO: 2016-04-17 open map to location and open phone on btnPhone press

    }

}
