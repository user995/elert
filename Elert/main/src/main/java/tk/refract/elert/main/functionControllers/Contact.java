package tk.refract.elert.main.FunctionControllers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import tk.refract.elert.main.R;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class Contact {
    private String name;
    private String cell;
    private String id;
    private Bitmap contactImage;

    public Contact(Context context, String cell) {
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER + " = " + cell, null, null);
        phones.moveToFirst();
        name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME));
        id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
        this.cell = cell;
        phones.close();

    }

    public Contact(String Contact_ID, Context context) {
        id = Contact_ID;
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + Contact_ID, null, null);

        phones.moveToFirst();
        name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME));

        String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
        if (number.substring(0, 3).equals("+27"))
            number = "0" + number.substring(3);
        else if (number.substring(0, 2).equals("27"))
            number = "0" + number.substring(2);

        cell = number;

        //Contact Image handling
        Uri my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));
        InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), my_contact_Uri);

        if (photo_stream == null) {
            contactImage = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.placeholder);
        } else {
            BufferedInputStream buf = new BufferedInputStream(photo_stream);
            contactImage = BitmapFactory.decodeStream(buf);
        }


        phones.close();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public Bitmap getContactImage() {
        return contactImage;
    }

    public void setContactImage(Bitmap contactImage) {
        this.contactImage = contactImage;
    }


}
