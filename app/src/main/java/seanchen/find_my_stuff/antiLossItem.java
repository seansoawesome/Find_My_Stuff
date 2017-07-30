package seanchen.find_my_stuff;

import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by John- on 7/27/2017.
 */


public class antiLossItem
{
    private String item_name;
    private LatLng item_loc;
    private Image item_pic;
    private Date item_date = new Date();

    public antiLossItem(String name)
    {
        item_name = name;
    }

    public antiLossItem(String name, LatLng loc)
    {
        item_name = name;
        item_loc = loc;
    }

    public antiLossItem(String name, LatLng loc, Image pic)
    {
        item_name = name;
        item_loc = loc;
        item_pic = pic;
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

    public Image get_pic()
    {
        return item_pic;
    }

    public Date get_date()
    {
        return item_date;
    }
}
