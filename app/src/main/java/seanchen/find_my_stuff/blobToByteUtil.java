package seanchen.find_my_stuff;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;

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
}
