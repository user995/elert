package tk.refract.elert.main.functionControllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class LocalDatabaseController extends SQLiteOpenHelper {
    private Context context;
    public LocalDatabaseController(Context context) {
        super(context, Constants.DB_NAME, null, 2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS notification(contact_id VARCHAR, location VARCHAR(150), date VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS contact(contact_id VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notification");
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }

    public void scrubDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS notification");
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }

    public boolean insertNotification(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contact_id", notification.getContact_id());
        contentValues.put("location", notification.getLocation());
        contentValues.put("date", notification.getDateString());
        db.insert("notification", null, contentValues);
        return true;
    }

    public ArrayList<Notification> getLatestNotifications() {
        ArrayList<Notification> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM notification", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Notification notification = new Notification(res.getString(res.getColumnIndex("contact_id")), res.getString(res.getColumnIndex("location")), res.getString(res.getColumnIndex("date")), context);
            list.add(notification);
            res.moveToNext();
        }

        int to = list.size() - 3 > 0 ? list.size() - 3 : 0;
        ArrayList<Notification> listV2 = new ArrayList<>();
        for (int i = list.size() - 1; i > to; i--) {
            listV2.add(list.get(i));
        }

        return listV2;
    }

    public boolean insertContact(String Contact_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contact_id", Contact_ID);
        db.insert("contact", null, contentValues);
        return true;
    }

    public Integer deleteContact(String Contact_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contact",
                "contact_id = ? ",
                new String[]{Contact_ID});
    }

    public ArrayList<String> getContacts() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM contact", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            list.add(res.getString(res.getColumnIndex("contact_id")));
            res.moveToNext();
        }
        return list;
    }
}
