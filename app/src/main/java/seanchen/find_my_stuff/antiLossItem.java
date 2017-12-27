package seanchen.find_my_stuff;

//import android.media.Image;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by John- on 7/27/2017.
 */

@Entity(tableName = "items")
public class antiLossItem
{
    @PrimaryKey
    @ColumnInfo(name = "name")
    private String item_name;
    @ColumnInfo(name = "Location")
    private LatLng item_loc;
    @ColumnInfo(name = "Picture")
    private Bitmap item_pic;
    @ColumnInfo(name = "Date")
    private Date item_date = new Date();
    @ColumnInfo(name = "Contains Picture")
    private boolean containPic;
    @ColumnInfo(name = "Contains Location")
    private boolean containLoc;

    public antiLossItem(String name)
    {
        item_name = name;
    }

    @Ignore
    public antiLossItem(String name, LatLng loc)
    {
        item_name = name;
        item_loc = loc;
        containPic = false;
        containLoc = true;
    }

    public antiLossItem(String name, LatLng loc, Bitmap pic)
    {
        item_name = name;
        item_loc = loc;
        item_pic = pic;
        containPic = true;
        containLoc = true;
    }

    @Ignore
    public antiLossItem(String name, Bitmap pic)
    {
        item_name = name;
        item_pic = pic;
        containPic = false;
        containLoc = false;
    }

    @Override
    public String toString()
    {
        return "Item: " + get_name() +" | Location: "+get_loc().latitude + ", "+get_loc().longitude;
    }

    public String get_name()
    {
        return item_name;
    }

    public LatLng get_loc()
    {
        return item_loc;
    }

    public Bitmap get_pic()
    {
        return item_pic;
    }

    public Date get_date()
    {
        return item_date;
    }

    public boolean has_pic(){return containPic;}

    public boolean has_loc() {return containLoc;}
}
