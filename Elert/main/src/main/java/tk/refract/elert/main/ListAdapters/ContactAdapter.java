package tk.refract.elert.main.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import tk.refract.elert.main.R;
import tk.refract.elert.main.functionControllers.Contact;

import java.util.List;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, R.layout.layout_contact, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_contact, parent, false);
        }
        CircleImageView imgCon = (CircleImageView) view.findViewById(R.id.imgConImage);

        TextView tvConName = (TextView) view.findViewById(R.id.tvConName);
        tvConName.setText(getItem(position).getName());

        return view;
    }
}
