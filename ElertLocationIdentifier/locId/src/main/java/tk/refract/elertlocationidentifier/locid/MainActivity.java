package tk.refract.elertlocationidentifier.locid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gc.materialdesign.views.ButtonFloat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private LocationAdapter locateAdapter;
    public static final String TAG = NetworkController.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Location> locations = new ArrayList<Location>();
        locateAdapter = new LocationAdapter(this,locations);
        ListView lvLocations = (ListView) findViewById(R.id.lvLocations);
        lvLocations.setAdapter(locateAdapter);

        lvLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW, locateAdapter.getItem(position).MapURI());
                startActivity(intent);
                }
        });

        lvLocations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Location location = locateAdapter.getItem(i);
                locateAdapter.remove(location);
                return true;
            }
        });

        ButtonFloat btn = (ButtonFloat)findViewById(R.id.fltBtnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == -1) {
                Uri contactData = data.getData();
                Cursor cursor = this.getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                updateLocation((new Contact(contactId,this)).getCell());
                cursor.close();
            }

    }

    private void updateLocation(final String cell){
        String tag = "getlocation";
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Log.d("RESPONSE",response);
                    JSONObject jObj = new JSONObject(response);
                    Boolean error = jObj.getBoolean("error");
                    if (!error){
                        Location location = new Location(jObj.getString("location"),jObj.getString("lastupdate"),MainActivity.this,cell);
                        location.startUpdates();
                        locateAdapter.add(location);
                    } else {
                        Toast.makeText(MainActivity.this, "Contact probably not using Elert", Toast.LENGTH_SHORT).show();
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