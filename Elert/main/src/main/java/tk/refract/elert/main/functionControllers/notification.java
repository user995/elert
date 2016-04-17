package tk.refract.elert.main.functionControllers;

import android.content.Context;
import android.net.Uri;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class Notification {
    private String name;
    private String location;
    private double latitude, longitude;
    private String cell;
    private String contact_id;
    private Date date;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());

    public Notification(String contact_id, String location,String date, Context context) {
        this.contact_id = contact_id;
        Contact contact = new Contact(contact_id,context);
        this.name = contact.getName();
        this.location = location;
        this.date = sdf.parse(date,new ParsePosition(0));
        String[] loc = location.split(",");
        latitude = Double.parseDouble(loc[0]);
        longitude = Double.parseDouble(loc[1]);
        this.cell = contact.getCell();
    }

    public String getContact_id() {
        return contact_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString(){
        return sdf.format(date);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        String[] loc = location.split(",");
        latitude = Double.parseDouble(loc[0]);
        longitude = Double.parseDouble(loc[1]);
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public Uri MapURI(){
        return Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude);
    }

    public Uri CallURI(){
        return Uri.parse("tel:"+cell);
    }
}
