package seanchen.find_my_stuff;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by John- on 12/28/2017.
 */

public class blobToByteUtil {
        // convert from bitmap to byte array
        public static byte[] img_to_byte(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        }

        // convert from byte array to bitmap
        public static Bitmap byte_to_img(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        public static LatLng doubles_to_latlng(double lat, double lng)
        {
            return new LatLng(lat, lng);
        }

        public static Date string_to_date(String date)
        {
            DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            Date temp = null;
            try {
                temp = format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return temp;
        }

    public static Bitmap bitmap_color_bg(int color) {

        Bitmap bitmap = Bitmap.createBitmap(75, 75, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();

        paint.setColor(color);

        canvas.drawRect(0F, 0F, (float) 75, (float) 75, paint);

        return bitmap;

    }
}
