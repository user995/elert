package tk.refract.elert.main.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import tk.refract.elert.main.R;
import tk.refract.elert.main.functionControllers.LocalDatabaseController;

public class HomeFragment extends Fragment {

    private LocalDatabaseController dbController;
    private Context context;
    private ListView lvNotifications;
    private TextView tvNoNotifications;

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
        //ArrayList<Notification> notifications =  dbController.getLatestNotifcations();
        //lvNotifications = (ListView)view.findViewById(R.id.lvNotifications);
        //notifyAdapter = new NotificationAdapter(context,notifications);
        tvNoNotifications = (TextView) view.findViewById(R.id.tvNoNotifications);

        //lvNotifications.setAdapter(conAdapter);

        //updateView();

        return view;
    }


//    private void updateView(){
//        if (notifyAdapter.isEmpty())
//        {
//            lvNotifications.setVisibility(View.GONE);
//            tvNoNotifications.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            lvNotifications.setVisibility(View.VISIBLE);
//            tvNoNotifications.setVisibility(View.GONE);
//        }
//    }


}
