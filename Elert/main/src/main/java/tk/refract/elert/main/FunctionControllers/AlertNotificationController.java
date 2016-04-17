package tk.refract.elert.main.FunctionControllers;


// HOLY GRAIL
//
//Intent intent = new Intent(getApplicationContext(),AlertNotificationService.class);
//        intent.setAction(Constants.ACTION_NEARBY);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getService(this,Constants.REQUEST_CODE,intent,0);
//        view.setOnClickPendingIntent(R.id.button,pendingIntent);


//    public void createNotification() {
//
//        Intent homeIntent = new Intent(this, HomeActivity.class);
//        Intent nearbyIntent = new Intent(this, AlertNearby.class);
//        nearbyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent homePendingIntent = PendingIntent.getActivity(this, 1, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent nearbyPendingIntent = PendingIntent.getActivity(this, 1, nearbyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        android.app.Notification.Builder builder = new android.app.Notification.Builder(getApplicationContext());
//        builder.setContentTitle("Elert");
//        builder.setContentText("Standby mode");
//        builder.setContentIntent(homePendingIntent);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setOngoing(true);
//        builder.setPriority(android.app.Notification.PRIORITY_DEFAULT);
//        builder.addAction(R.drawable.ic_warning_black_24dp, "Nearby Alert", nearbyPendingIntent);
//        builder.setVisibility(1);
//        android.app.Notification notification = builder.build();
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, notification);
//    }
