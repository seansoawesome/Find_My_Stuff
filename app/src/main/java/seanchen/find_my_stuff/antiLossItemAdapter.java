package seanchen.find_my_stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John- on 12/21/2017.
 */

public class antiLossItemAdapter extends ArrayAdapter<antiLossItem>
{
    public antiLossItemAdapter(Context context, List<antiLossItem> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        antiLossItem user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items, parent, false);
        }
        // Lookup view for data population
        TextView itemname = (TextView) convertView.findViewById(R.id.name);
        TextView itemloc = (TextView) convertView.findViewById(R.id.location);
        if(user.has_pic())
        {
            ImageView itempic = (ImageView) convertView.findViewById(R.id.picture);
            itempic.setImageBitmap(user.get_pic());
//            itempic.buildDrawingCache(true);
//            Bitmap b = itempic.getDrawingCache(true);
//            BitmapDrawable d = (BitmapDrawable) itempic.getDrawable();
//            itempic.setImageDrawable(d);
        }
        // Populate the data into the template view using the data object
        itemname.setText(user.get_name());
        itemloc.setText(user.get_loc().toString());
        // Return the completed view to render on screen
        return convertView;
    }


}