//TO DO : PUT CLASS TYPE ANTILOSSITEM INTO ARRAY FORM? DISPLAY IT
//GET CAMERA TO WORK
//(?)FIGURE OUT HOW TO DELETE/SAVE/RESTORE DATA
package seanchen.find_my_stuff;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Menu extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener
{

    private TextView mTextMessage;
    private TextView user_input;
    private ListView view_list;
    private RelativeLayout add_loc_menu;
    private Button buttonAddLoc;
    private LatLng cur_loc;
    private Marker cur_marker;
    private boolean list_menu = false;
    private boolean add_menu = false;
    private antiLossItem item_list;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    if(list_menu)
                        view_list.setVisibility(View.GONE);
                    if(add_menu)
                        add_loc_menu.setVisibility(View.GONE);

                    mTextMessage.setText(R.string.title_camera);
                    Intent i = new Intent(Menu.this, pictureTime.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_addLoc:
                    if(list_menu)
                        view_list.setVisibility(View.GONE);

                    mTextMessage.setText(R.string.title_addLoc);
                    add_loc_menu.setVisibility(View.VISIBLE);
                    add_menu = true;
                    return true;
                case R.id.navigation_list:
                    if(add_menu)
                        add_loc_menu.setVisibility(View.GONE);

                    list_menu = true;
                    view_list.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.title_list);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //imgMyLocation = (ImageView) findViewById(R.id.imgMyLocation);
        view_list = (ListView) findViewById(R.id.item_list);
        add_loc_menu = (RelativeLayout) findViewById(R.id.addLocMenu);
        buttonAddLoc = (Button) findViewById(R.id.add_loc_button);
        mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

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
        //HOW TO SAVE i into system???
        //HOW TO ACCESS i in strings.xml???
        //ALT: HOW TO DISPLAY AN ARRAY OF i ON ACTIVITY MENU?
        Toast.makeText(this, "you did it", Toast.LENGTH_SHORT).show();
    }


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
        cur_marker.setPosition(point);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
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

}
