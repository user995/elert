package tk.refract.elert.main.FunctionControllers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.TextView;
import tk.refract.elert.main.HomeActivity;
import tk.refract.elert.main.Services.GeocodeAddressIntentService;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class Notification {
    public String namedLocation;
    private String name;
    private String location;
    private double latitude, longitude;
    private String cell;
    private String contact_id;
    private Date date;
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
    private TextView secret;

    public Notification(String contact_id, String location,String date, Context context) {
        this.contact_id = contact_id;
        Contact contact = new Contact(contact_id,context);
        this.name = contact.getName();
        this.location = location;
        this.date = sdf.parse(date,new ParsePosition(0));
        String[] loc = location.split(",");
        latitude = Double.parseDouble(loc[0]);
        longitude = Double.parseDouble(loc[1]);
        this.cell = contact.getCell();
        this.context = context;
        identifyNamedLocation();
    }

    private void identifyNamedLocation(){
        AddressResultReceiver mResultReceiver = new AddressResultReceiver(null);
        Intent intent = new Intent(context, GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER,mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,getLocation());
        context.startService(intent);
    }

    public String getContact_id() {
        return contact_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString(){
        return sdf.format(date);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        String[] loc = location.split(",");
        latitude = Double.parseDouble(loc[0]);
        longitude = Double.parseDouble(loc[1]);
        identifyNamedLocation();
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public Uri MapURI(){
        return Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude);
    }

    public Uri CallURI(){
        return Uri.parse("tel:"+cell);
    }

    public void getNamedLocation(TextView tvLocation){
        secret = tvLocation;
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {

                final String location = resultData.getString(Constants.RESULT_DATA_KEY);
                ((HomeActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        namedLocation =  location;
                        if (secret != null)
                            secret.setText(namedLocation);
                    }
                });
            } else
                ((HomeActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        namedLocation = "Unknown";
                        if (secret != null)
                            secret.setText(namedLocation);
                    }
                });

        }
    }
}
