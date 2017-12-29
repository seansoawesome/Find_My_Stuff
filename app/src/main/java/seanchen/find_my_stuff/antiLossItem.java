package seanchen.find_my_stuff;

//import android.media.Image;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by John- on 7/27/2017.
 */


public class antiLossItem
{
    private int item_id;
    private String item_name;
    private LatLng item_loc;
    private Bitmap item_pic;
    private Date item_date;
    private boolean containPic = false;
    private boolean containLoc = false;

    public antiLossItem(){}

//    public antiLossItem(int id, String name)
//    {
//        this.item_name = name;
//        this.item_id = id;
//    }
//
//    public antiLossItem(int id, String name, LatLng loc)
//    {
//        this.item_name = name;
//        this.item_loc = loc;
//        this.containPic = false;
//        this.containLoc = true;
//        this.item_id = id;
//    }

    public antiLossItem(int id, String name, LatLng loc, Bitmap pic, Date date)
    {
        this.item_name = name;
        this.item_loc = loc;
        this.item_pic = pic;
        if(pic!= null)
            this.containPic = true;
        if(loc!=null)
            this.containLoc = true;
        this.item_id = id;
        this.item_date = date;
    }

//    public antiLossItem(int id, String name, Bitmap pic)
//    {
//        this.item_name = name;
//        this.item_pic = pic;
//        this.containPic = false;
//        this.containLoc = false;
//        this.item_id = id;
//    }

    @Override
    public String toString()
    {
        return "Location: " + get_loc().latitude + ", " + get_loc().longitude;
    }

    public String coord_to_String(LatLng c)
    {
        return "Lat:" + c.latitude +", Lng:" + c.longitude;
    }

    public String get_name()
    {
        return item_name;
    }
    public void set_name(String n) {this.item_name = n; }

    public LatLng get_loc() { return item_loc; }
    public void set_loc(double lat, double lng) {LatLng t = new LatLng(lat,lng); item_loc = t;}

    public Bitmap get_pic() { return item_pic; }

    public Date get_date() { return item_date; }

    public int get_id(){ return item_id; }
    public void  set_id(int id) {this.item_id = id;}

    public boolean has_pic(){return containPic;}

    public boolean has_loc() {return containLoc;}
}