package tk.refract.elertlocationidentifier.locid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class Location {
    private String name;
    private String location;
    private double latitude, longitude;
    private String cell;
    private String contact_id;
    private Date date;
    private Context context;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());

    public Location(String contact_id, String location, String date, Context context) {
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

    public Location(String location, String date, Context context, String cell) {


        Contact contact = new Contact(context,cell);
        this.contact_id = contact.getId();
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

    public void setDate(String date) {
        this.date = sdf.parse(date,new ParsePosition(0));
        tvUpdate.setText(getDateString());
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



    public String namedLocation;

    private TextView tvLocation, tvUpdate;
    public void getNamedLocation(TextView tvLocation, TextView tvUpdate){
        this.tvLocation = tvLocation;
        this.tvUpdate = tvUpdate;
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {

                final String location = resultData.getString(Constants.RESULT_DATA_KEY);
                ((MainActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        namedLocation =  location;
                        if (tvLocation != null)
                            tvLocation.setText(namedLocation);
                    }
                });
            } else
                ((MainActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        namedLocation = "Unknown";
                        if (tvLocation != null)
                            tvLocation.setText(namedLocation);
                    }
                });

        }
    }

    public void startUpdates(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateLocation();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,0,5*1000);
    }

    private void updateLocation(){
        String tag = "getlocation";
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Log.d("RESPONSE",response);
                    JSONObject jObj = new JSONObject(response);
                    Boolean error = jObj.getBoolean("error");
                    if (!error){
                        setLocation(jObj.getString("location"));
                        setDate(jObj.getString("lastupdate"));
                    } else {
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error","There is an error here");
                //error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<String, String>();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag","getlocation");
                params.put("cell_num",cell);
                return params;
            }
        };
        NetworkController.getInstance().addToRequestQueue(request,tag);
    }
}
