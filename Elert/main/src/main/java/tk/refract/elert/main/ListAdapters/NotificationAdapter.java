package tk.refract.elert.main.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import tk.refract.elert.main.HomeActivity;
import tk.refract.elert.main.R;
import tk.refract.elert.main.Services.GeocodeAddressIntentService;
import tk.refract.elert.main.functionControllers.Constants;
import tk.refract.elert.main.functionControllers.Notification;

import java.util.List;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class NotificationAdapter extends ArrayAdapter<Notification> {
    private TextView tvNotLocation;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        super(context, R.layout.layout_notification, notifications);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_notification, parent, false);
        }
        TextView tvNotPerson = (TextView) view.findViewById(R.id.tvNotPerson);
        TextView tvNotDate = (TextView) view.findViewById(R.id.tvNotDate);
        tvNotLocation = (TextView) view.findViewById(R.id.tvNotLocation);
        tvNotPerson.setText(getItem(position).getName());
        tvNotDate.setText(getItem(position).getDateString());
        tvNotLocation.setText("Location: Unknown");

        AddressResultReceiver mResultReceiver = new AddressResultReceiver(null);
        Intent intent = new Intent(getContext(), GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, getItem(position).getLocation());
        getContext().startService(intent);

        return view;
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {

                final String location = resultData.getString(Constants.RESULT_DATA_KEY);
                ((HomeActivity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvNotLocation.setText("Location: " + location);
                    }
                });
            } else
                ((HomeActivity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvNotLocation.setText("Location: Unknown");
                    }
                });

        }
    }


}
