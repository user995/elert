package tk.refract.elert.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gc.materialdesign.views.ButtonFloat;
import org.json.JSONException;
import org.json.JSONObject;
import tk.refract.elert.main.Fragments.ContactFragment;
import tk.refract.elert.main.functionControllers.Constants;
import tk.refract.elert.main.functionControllers.LocalDatabaseController;
import tk.refract.elert.main.functionControllers.NetworkController;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements LocationListener {
    //Network
    public static final String TAG = NetworkController.class.getSimpleName();
    Integer counter = 0;
    //
    private DrawerLayout drawerLayout;
    private RelativeLayout welcome;
    private SharedPreferences sharedPref;
    private Toolbar toolbar;
    private NavigationView navView;
    private LocalDatabaseController ldbController;
    private android.app.FragmentManager fm;
    //runtime Constants
    private String cell;
    //Location variables
    private String Coordinates;
    private LocationManager locationManager;
    private String provider;
    private Location location;
    //Stores Locaiton
    private String Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check preferences and see open main_home activity screen or 'register screen'
        setContentView(R.layout.activity_home);
        ldbController = new LocalDatabaseController(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navView = (NavigationView) findViewById(R.id.navigation_view);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        fm = getFragmentManager();

        setSupportActionBar(toolbar);

        //Location Services
        StartLocationServices();
        Location = getLocation();

        //Fragments
        final ContactFragment contacts = new ContactFragment();
        contacts.setContext(this);
        contacts.setLocalDatabase(ldbController);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.reset:
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("name");
                        editor.remove("cell");
                        editor.commit();
                        return true;
                    case R.id.itEmCon:
                        changeFragment(contacts);
                        return true;

                    default:
                        return false;
                }
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        welcome = (RelativeLayout) findViewById(R.id.welcome);

        String testCell = sharedPref.getString("cell", "null");
        if (testCell.equals("null")) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            final ButtonFloat buttonFloat = (ButtonFloat) findViewById(R.id.buttonFloat);
            welcome.setVisibility(View.VISIBLE);
            buttonFloat.setVisibility(View.VISIBLE);
            buttonFloat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonFloat.setVisibility(View.GONE);

                    EditText etName = (EditText) findViewById(R.id.etName);
                    EditText etCell = (EditText) findViewById(R.id.etCell);
                    String name = etName.getText().toString().trim();
                    cell = etCell.getText().toString().trim();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name", name);
                    editor.putString("cell", cell);
                    editor.commit();

                    registerLogin(cell, getLocation());

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    toolbar.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv2Name)).setText(name);
                    ((TextView) findViewById(R.id.tv2Phone)).setText(cell);
                }
            });
        } else {
            cell = testCell;
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toolbar.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv2Name)).setText(sharedPref.getString("name", "error"));
            ((TextView) findViewById(R.id.tv2Phone)).setText(cell);
        }
    }

    void changeFragment(android.app.Fragment frag) {
        FragmentTransaction fragTrans = fm.beginTransaction();
        //ft.disallowAddToBackStack();
        try {
            fragTrans.replace(R.id.flFragment, frag);
        } catch (Exception e) {
            fragTrans.add(R.id.flFragment, frag);
        }
        fragTrans.addToBackStack(null);
        fragTrans.commit();
    }

    void welcomeVisible() {
        int cx = welcome.getRight();
        int cy = welcome.getBottom();
        int finalRadius = Math.max(welcome.getWidth(), welcome.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(welcome, cx, cy, 0, finalRadius);
        welcome.setVisibility(View.VISIBLE);
        anim.start();
    }

    void welcomeInvisible() {
        int cx = welcome.getRight();
        int cy = welcome.getBottom();
        int initialRadius = Math.max(welcome.getWidth(), welcome.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(welcome, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                welcome.setVisibility(View.INVISIBLE);
            }

        });
        anim.start();
    }

    private void registerLogin(final String cell_num, final String Location) {

        String tag = "register";

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Boolean error = jObj.getBoolean("error");
                    // pending = false;
                    if (!error) {
                        welcomeInvisible();
                    } else {
                        Toast.makeText(HomeActivity.this, "Big problems", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (counter < 5) {
                    counter++;
                    Toast.makeText(getApplicationContext(), "Retrying attempt " + counter, Toast.LENGTH_LONG).show();
                    registerLogin(cell_num, Location);
                } else {
                    Toast.makeText(getApplicationContext(), "Undefined network error " + error.getMessage(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<>();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("cell_num", cell_num);
                params.put("location", Location);
                return params;
            }
        };
        NetworkController.getInstance().addToRequestQueue(request, tag);
    }

    /**
     * Location Handling Methods below
     **/


    public void StartLocationServices() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);
    }

    public String getLocation() {
        if (location != null) {
            onLocationChanged(location);
            return Coordinates;
        } else {
            return "null";
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String lat = Double.toString(location.getLatitude());
        String lng = Double.toString(location.getLongitude());
        Coordinates = lat + "," + lng;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



}
