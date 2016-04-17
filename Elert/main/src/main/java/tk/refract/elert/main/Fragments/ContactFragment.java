package tk.refract.elert.main.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonFloat;
import tk.refract.elert.main.ListAdapters.ContactAdapter;
import tk.refract.elert.main.R;
import tk.refract.elert.main.functionControllers.Contact;
import tk.refract.elert.main.functionControllers.LocalDatabaseController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactFragment extends Fragment {

    private static int RQS_PICK = 1;
    private LocalDatabaseController dbController;
    private Context context;
    private ListView lvContacts;
    private TextView tvNoContacts;
    private ContactAdapter conAdapter;
    private Comparator<Contact> contactSort = new Comparator<Contact>() {
        @Override
        public int compare(Contact lhs, Contact rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    };

    public ContactFragment() {
        // Required empty public constructor
    }

    public void setLocalDatabase(LocalDatabaseController db) {
        dbController = db;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ArrayList<Contact> contacts = fillContacts();
        lvContacts = (ListView) view.findViewById(R.id.lvContacts);
        conAdapter = new ContactAdapter(context, contacts);
        tvNoContacts = (TextView) view.findViewById(R.id.tvNoContacts);

        lvContacts.setAdapter(conAdapter);

        updateView();

        ButtonFloat fltBtnAdd = (ButtonFloat) view.findViewById(R.id.fltBtnAdd);
        fltBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, RQS_PICK);
            }
        });

        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = conAdapter.getItem(position);
                conAdapter.remove(contact);
                dbController.deleteContact(contact.getId());
                updateView();
                return true;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQS_PICK)
            if (resultCode == -1) {
                Uri contactData = data.getData();
                Cursor cursor = context.getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                conAdapter.add(new Contact(contactId, context));
                conAdapter.sort(contactSort);
                dbController.insertContact(contactId);
                cursor.close();
                updateView();
            }

    }

    private void updateView() {
        if (conAdapter.getCount() == 0) {
            lvContacts.setVisibility(View.GONE);
            tvNoContacts.setVisibility(View.VISIBLE);
        } else {
            lvContacts.setVisibility(View.VISIBLE);
            tvNoContacts.setVisibility(View.GONE);
        }
    }

    private ArrayList<Contact> fillContacts() {
        ArrayList<Contact> list = new ArrayList<>();
        for (String contactID : dbController.getContacts()) {
            list.add(new Contact(contactID, context));
        }
        Collections.sort(list, contactSort);
        return list;
    }
}
