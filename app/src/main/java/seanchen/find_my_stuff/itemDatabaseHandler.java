package seanchen.find_my_stuff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John- on 12/27/2017.
 */

public class itemDatabaseHandler extends SQLiteOpenHelper {
    //DB version
    private static final int VERSION = 1;
    //DB name
    private static final String DATABASE_NAME = "itemTable.db";
    //table name
    private static final String TABLE_NAME = "items";
    //col names
    public static final String ID = "item_id";
    public static final String ITEM = "item_name";
    public static final String LAT = "item_lat";
    public static final String LNG = "item_lng";
    public static final String DATE = "item_date";
    public static final String PIC = "item_pic";
    //converter helper
    blobToByteUtil converter = new blobToByteUtil();

    public itemDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY," + ITEM + " TEXT,"
                + LAT + " INTEGER PRIMARY KEY," + LNG + " INTEGER PRIMARY KEY," +
                DATE + " TEXT," + PIC +" BLOB);";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // Adding new item
    public void addItem(antiLossItem i)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //TODO: FIX BUG HERE!!!!!!!!!!!
        ContentValues values = new ContentValues();
        values.put(ID, i.get_id()); //item id
        values.put(ITEM, i.get_name()); // item Name
        values.put(LAT, i.get_loc().latitude); // item latitude
        values.put(LNG, i.get_loc().longitude); //item longitude
        values.put(DATE, i.get_date().toString());//item date
        values.put(PIC, converter.img_to_byte(i.get_pic())); //item pic

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single item
    public antiLossItem getItem(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] FIELDS = {ID, ITEM, LAT, LNG, DATE, PIC};
        Cursor cursor = db.query(TABLE_NAME, FIELDS, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        antiLossItem i = new antiLossItem(Integer.parseInt(cursor.getString(0)), //item id
                cursor.getString(1),//item name
                converter.doubles_to_latlng(cursor.getDouble(2),cursor.getDouble(3)),//item loc
               converter.byte_to_img(cursor.getBlob(5)) );//item pic
        // return contact
        return i;
    }

    // Getting All items
    public List<antiLossItem> getAllItems()
    {
        List<antiLossItem> item_list = new ArrayList<antiLossItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//                antiLossItem i = new antiLossItem();
//                i.set_name(cursor.getString(0));
//                i.setName(cursor.getString(1));
//                i.set_id(Integer.parseInt(cursor.getString(3)));
                antiLossItem i = new antiLossItem(Integer.parseInt(cursor.getString(0)), //item id
                        cursor.getString(1),//item name
                        converter.doubles_to_latlng(cursor.getDouble(2),cursor.getDouble(3)),//item loc
                        converter.byte_to_img(cursor.getBlob(5)) );//item pic
                // Adding contact to list
                item_list.add(i);
            } while (cursor.moveToNext());
        }

        // return contact list
        return item_list;
    }

    // Getting number of items
    public int getItemsCount()
    {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        Long n = DatabaseUtils.queryNumEntries(db, TABLE_NAME);//cursor.getCount();
        Log.d(Menu.class.getSimpleName(), "cursor.getcount = " + n.intValue());
        if(n.intValue() <= 0)
            return 0;
        // return count
        return n.intValue();
    }

    // Updating single item
    public int updateItem(antiLossItem i)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM, i.get_name());
//        values.put(ID, i.get_id());

        // updating row
        return db.update(TABLE_NAME, values, ID + " = ?",
                new String[] { String.valueOf(i.get_id()) });
    }

    // Deleting single item
    public void deleteItem(antiLossItem i)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[] { String.valueOf(i.get_id()) });
        db.close();
    }
}
