//TODO: overall still super buggy, dont forget to put a shit ton of if statements
package seanchen.find_my_stuff;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener
{

    private TextView mTextMessage;
    private ListView view_list;
    private RelativeLayout add_loc_menu;
    private RelativeLayout add_cam_menu;
    private TextView user_input;
    private Button buttonAddLoc;
    private LatLng cur_loc;
    private Marker cur_marker;
    private boolean list_menu = false;
    private boolean add_menu = false;
    private List<antiLossItem> item_list = new ArrayList<antiLossItem>();
    private String mCurrentPhotoPath;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    /**---------------------------------Bottom_Menu----------------------------------------------**/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    view_list.setVisibility(View.GONE);
                    add_loc_menu.setVisibility(View.GONE);

                    mTextMessage.setText(R.string.title_camera);
                    dispatchTakePictureIntent();
                    /*Intent i = new Intent(Menu.this, pictureTime.class);
                    startActivity(i);*/
                    return true;
                case R.id.navigation_addLoc:
                    add_cam_menu.setVisibility(View.GONE);
                    view_list.setVisibility(View.GONE);

                    mTextMessage.setText(R.string.title_addLoc);
                    add_loc_menu.setVisibility(View.VISIBLE);
                    add_menu = true;
                    return true;
                case R.id.navigation_list:
                    add_cam_menu.setVisibility(View.GONE);
                    add_loc_menu.setVisibility(View.GONE);

                    view_list.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.title_list);
                    return true;
            }
            return false;
        }

    };

    /**---------------------------------Start----------------------------------------------------**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //imgMyLocation = (ImageView) findViewById(R.id.imgMyLocation);
        add_loc_menu = (RelativeLayout) findViewById(R.id.addLocMenu);
        add_cam_menu = (RelativeLayout) findViewById(R.id.photo_result);
        buttonAddLoc = (Button) findViewById(R.id.add_loc_button);
        mTextMessage = (TextView) findViewById(R.id.message);

        //lists out the object in R.id.item_list of type ListView
        //TODO: display text AND icon on the side if there is one
        //TODO: save the array for next time/ restore the array from last time
        view_list = (ListView) findViewById(R.id.item_list);
        ArrayAdapter<antiLossItem> adapter = new ArrayAdapter<antiLossItem>(this, android.R.layout.simple_list_item_1, item_list);
        view_list.setAdapter(adapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**---------------------------------CAMERA---------------------------------------------------**/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    //get image icon and post it somewhere(?)
    //saved the image in my phone's path: /storage/9C33-6BBD/DCIM/100ANDRO/DSC_7157.JPG
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = (ImageView) findViewById(R.id.imageView) ;
            mImageView.setImageBitmap(imageBitmap);
            //start form process
        }
        add_cam_menu.setVisibility(View.VISIBLE);
    }

    public void onSubmitClick2 (View view)
    {
        //TODO: Get input value
        //TODO: Get location when toggle button is enabled and dispay it
        //TODO: save as new object and add to current list
        //TODO: Clear input text
        Toast.makeText(this, "supposedly saved", Toast.LENGTH_SHORT).show();
        add_cam_menu.setVisibility(View.GONE);
    }

    /**---------------------------------List_the_objects-----------------------------------------**/
    //TODO: allow a delete button so user can delete objects

    public void onSubmitClick(View view)
    {
        user_input = (TextView) findViewById(R.id.item_name);
        String t = user_input.getText().toString();
        if(user_input.getText().toString().matches(""))
        {
            Toast.makeText(this, "ENTER NAME PLEASE", Toast.LENGTH_SHORT).show();
            return;
        }
        LatLng g = cur_marker.getPosition();
        antiLossItem i = new antiLossItem(t, g);
        item_list.add(i);
        //HOW TO SAVE i into system???
        //HOW TO ACCESS i in strings.xml???
        //ALT: HOW TO DISPLAY AN ARRAY OF i ON ACTIVITY MENU?
        user_input.setText("");
        cur_marker.setVisible(false);
        Toast.makeText(this, "you did it", Toast.LENGTH_SHORT).show();
    }

    /**---------------------------------GoogleMaps-----------------------------------------------**/
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        LatLng rando_place = new LatLng(24.8138, 120.9675);
        cur_marker = mMap.addMarker(new MarkerOptions().title("Current Chosen Location").position(rando_place).draggable(true));
        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }

    @Override
    public void onMapClick(LatLng point)
    {
        //Toast.makeText(this, "map clicked", Toast.LENGTH_SHORT).show();
        cur_marker.setVisible(true);
        cur_marker.setPosition(point);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /**---------------------------------GPS_Location---------------------------------------------**/
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        cur_marker.setPosition(mMap.getCameraPosition().target);
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

}
