package tk.refract.elert.main.functionControllers;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class Contact {
    private String name;
    private String cell;

    public Contact(String name, String cell) {
        this.name = name;
        this.cell = cell;
    }

    public Contact(String Contact_ID, Context context) {
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + Contact_ID, null, null);

        phones.moveToFirst();

        String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
        if (number.substring(0, 3).equals("+27"))
            number = "0" + number.substring(4);
        else if (number.substring(0, 2).equals("27"))
            number = "0" + number.substring(3);

        name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME));
        cell = number;

        phones.close();
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
}
