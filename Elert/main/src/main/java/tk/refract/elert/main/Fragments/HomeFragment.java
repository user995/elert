package tk.refract.elert.main.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import tk.refract.elert.main.FunctionControllers.LocalDatabaseController;
import tk.refract.elert.main.FunctionControllers.Notification;
import tk.refract.elert.main.ListAdapters.NotificationAdapter;
import tk.refract.elert.main.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private LocalDatabaseController dbController;
    private Context context;
    private ListView lvNotifications;
    private TextView tvNoNotifications;
    private NotificationAdapter notifyAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public void setLocalDatabase(LocalDatabaseController db) {
        dbController = db;
    }
    //private ContactAdapter conAdapter;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ArrayList<Notification> notifications = dbController.getLatestNotifications();
        lvNotifications = (ListView) view.findViewById(R.id.lvNotifications);
        notifyAdapter = new NotificationAdapter(context, notifications);
        tvNoNotifications = (TextView) view.findViewById(R.id.tvNoNotifications);

        lvNotifications.setAdapter(notifyAdapter);
        lvNotifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, notifyAdapter.getItem(position).MapURI());
                startActivity(intent);
            }
        });
        lvNotifications.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_CALL, notifyAdapter.getItem(position).CallURI());
                startActivity(intent);
                return true;
            }
        });
        updateView();
        return view;
    }

    private void updateView() {
        if (notifyAdapter.getCount() == 0) {
            lvNotifications.setVisibility(View.GONE);
            tvNoNotifications.setVisibility(View.VISIBLE);
        } else {
            lvNotifications.setVisibility(View.VISIBLE);
            tvNoNotifications.setVisibility(View.GONE);
        }
    }


}
