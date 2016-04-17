package tk.refract.elert.main.Services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import tk.refract.elert.main.FunctionControllers.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by s212289853 on 17/04/2016.
 */
public class GeocodeAddressIntentService extends IntentService {
    protected ResultReceiver resultReceiver;

    public GeocodeAddressIntentService() {
        super("GeocodeAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);


        String location = intent.getStringExtra(
                Constants.LOCATION_DATA_EXTRA);
        String[] loc = location.split(",");
        String errorMessage = "";
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), 1);
        } catch (IOException ioException) {
            errorMessage = "Service Not Available";
            Log.e("ERROR", errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "Invalid Latitude or Longitude Used";
            Log.e("ERROR", errorMessage + ". " + "Latitude = " + loc[0] + ", Longitude = " + loc[1], illegalArgumentException);
        }

        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty())
                errorMessage = "Not Found";
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else
            deliverResultToReceiver(Constants.SUCCESS_RESULT, addresses.get(0).getAddressLine(0));

    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        resultReceiver.send(resultCode, bundle);
    }
}
