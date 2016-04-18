package tk.refract.elertlocationidentifier.locid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class LocationAdapter extends ArrayAdapter<Location> {
    public LocationAdapter(Context context, List<Location> notifications) {
        super(context, R.layout.layout_location, notifications);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_location, parent, false);
        }
        TextView tvNotPerson = (TextView) view.findViewById(R.id.tvNotPerson);
        TextView tvNotDate = (TextView) view.findViewById(R.id.tvNotDate);
        TextView tvNotLocation = (TextView) view.findViewById(R.id.tvNotLocation);
        tvNotPerson.setText(getItem(position).getName());
        tvNotDate.setText(getItem(position).getDateString());
        getItem(position).getNamedLocation(tvNotLocation, tvNotDate);


        return view;
    }




}
