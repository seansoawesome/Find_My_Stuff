package seanchen.find_my_stuff;

import android.net.Uri;
import android.view.ContextMenu.ContextMenuInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import java.util.Date;
import java.util.List;

public class Menu extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    //SQLite Database var
    private itemDatabaseHandler db;
    private int item_count;
    //end

    //Item List View
    private int current_index;
    antiLossItemAdapter adapter;

    private TextView mTextMessage;//the title of each menu
    private TextView user_input;//the input box in the add_location menu
    private TextView user_input2;//the input box in the snap_it menu
    private TextView empty;
    private ImageView img;
    private Bitmap tmp_img;
    private ListView view_list;//the list box in the items menu
    private RelativeLayout add_loc_menu;//the layout in add_location menu
    private RelativeLayout add_cam_menu;//the layout  in snap_it menu
    private Switch location_switch;//the switch in the snap_it menu | user decides if they want loc or not
    private Marker cur_marker;//google maps marker
    private List<antiLossItem> item_list = new ArrayList<antiLossItem>();//an arraylist of type antiLostItem
    private Location loc;
    private Date date = new Date();
    private boolean useLoc = false;
    private boolean picTaken = false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    /**
     * ---------------------------------Bottom_Menu----------------------------------------------
     **/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        //all menu's visibility are set to GONE first
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    empty.setVisibility(View.GONE);
                    view_list.setVisibility(View.GONE);//close the items menu
                    add_loc_menu.setVisibility(View.GONE);//close the add_location menu

                    mTextMessage.setText(R.string.title_camera);//set title
                    dispatchTakePictureIntent();//start camera

                    return true;
                case R.id.navigation_addLoc:
                    empty.setVisibility(View.GONE);
                    view_list.setVisibility(View.GONE);
                    add_cam_menu.setVisibility(View.GONE);//close the snap_it menu

                    mTextMessage.setText(R.string.title_addLoc);
                    add_loc_menu.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_list:
                    if (item_list.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                        System.out.println("null");
                    } else {
                        empty.setVisibility(View.GONE);
                        System.out.println("not null");
                    }
                    add_cam_menu.setVisibility(View.GONE);
                    add_loc_menu.setVisibility(View.GONE);

                    view_list.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.title_list);
                    return true;
            }
            return false;
        }

    };

    /**
     * ---------------------------------Start----------------------------------------------------
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //init databse
        db = new itemDatabaseHandler(this);
        //Log.d(Menu.class.getSimpleName(), "db inited");//SAFE
        item_count = db.getItemsCount();
        //Log.d(Menu.class.getSimpleName(), "itemcount:" + item_count);
        if (item_count > 0)
            item_list = db.getAllItems();
        //Log.d(Menu.class.getSimpleName(), "itemlist copied over");

        //initiate google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialize the various layouts and buttons to their respective counterparts in activity_menu
        empty = (TextView) findViewById(R.id.empty_list);
        user_input = (TextView) findViewById(R.id.item_name);
        user_input2 = (TextView) findViewById(R.id.item_name_cam);
        location_switch = (Switch) findViewById(R.id.loc_switch);
        add_loc_menu = (RelativeLayout) findViewById(R.id.addLocMenu);
        add_cam_menu = (RelativeLayout) findViewById(R.id.photo_result);
        mTextMessage = (TextView) findViewById(R.id.message);
        picTaken = false;

        //List out the contents of the arrayList in items_menu
        //toString() function in class is default, i think
        view_list = (ListView) findViewById(R.id.item_list);
//        ArrayAdapter<antiLossItem> adapter = new ArrayAdapter<antiLossItem>(this, android.R.layout.simple_list_item_1, item_list);
        adapter = new antiLossItemAdapter(this, item_list);
        view_list.setAdapter(adapter);
        registerForContextMenu(view_list);
        //listviewlistener so user has options for each longclikc for an item
//        view_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////                options.setVisibility(View.VISIBLE);
//                current_index = (int)id;
//                return true;
//            }
//        });

        //the switch button in snap_it menu
        location_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    useLoc = true;
                    date = new Date();
                } else {
                    useLoc = false;
                }
            }
        });

        //initialize the navigation menu on th bottom of the screen
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * ---------------------------------CAMERA---------------------------------------------------
     **/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    //get image icon and post it to imageView in snap_it menu
    //saved the image in my phone's path: /storage/9C33-6BBD/DCIM/100ANDRO/DSC_7157.JPG
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            tmp_img = (Bitmap) extras.get("data");
            img = (ImageView) findViewById(R.id.imageView);
            img.setImageBitmap(tmp_img);
            picTaken = true;
            date = new Date();
            //start form process
            add_cam_menu.setVisibility(View.VISIBLE);
        }
    }

    public void onSubmitClick2(View view) {
        if (user_input2.getText().toString().matches(""))//if textView is empty
        {
            Toast.makeText(this, "ENTER NAME PLEASE", Toast.LENGTH_SHORT).show();
            return;
        }
        if (picTaken == false)//if textView is empty
        {
            Toast.makeText(this, "TAKE A PICTURE PLEASE", Toast.LENGTH_SHORT).show();
            return;
        }
        item_count++;
        String input = user_input2.getText().toString();
        Toast.makeText(this, "supposedly saved", Toast.LENGTH_SHORT).show();
        add_cam_menu.setVisibility(View.GONE);
        if (useLoc == true) {
            LatLng g = new LatLng(loc.getLatitude(), loc.getLongitude());
            antiLossItem i = new antiLossItem(item_count, input, g, tmp_img, date);
            item_list.add(i);
            db.addItem(i);
        } else {
            antiLossItem i = new antiLossItem(item_count, input, null, tmp_img, date);
            item_list.add(i);
            db.addItem(i);
        }
        picTaken = false;
        useLoc = false;
        user_input2.setText("");//clear the textbox
        location_switch.setChecked(false);
    }

    /**
     * ---------------------------------List_the_objects-----------------------------------------
     **/
    //TODO: allow a delete button so user can delete objects
    public void onSubmitClick(View view) {
        String t = user_input.getText().toString();
        if (user_input.getText().toString().matches("")) {
            Toast.makeText(this, "ENTER NAME PLEASE", Toast.LENGTH_SHORT).show();
            return;
        }
        item_count++;
        LatLng g = cur_marker.getPosition();

        if (useLoc == true) {
            antiLossItem i = new antiLossItem(item_count, t, g, null, date);
            item_list.add(i);
            db.addItem(i);
        } else {
            antiLossItem i = new antiLossItem(item_count, t, null, null, date);
            item_list.add(i);
            db.addItem(i);
        }

        useLoc = false;
        user_input.setText("");
        cur_marker.setVisible(false);
        Toast.makeText(this, "you did it", Toast.LENGTH_SHORT).show();
    }

//    private void delete_item()
//    {
//        db.deleteItem(current_index);
//        adapter.remove(adapter.getItem(current_index));
//        item_count = db.getItemsCount();
//        options.setVisibility(View.GONE);
//    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        //find out which menu item was pressed
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove_item:
                remove_item((int)info.id);
                return true;
            case R.id.directions_to_item:
                doOptionTwo((int)info.id);
                return true;
            default:
                return false;
        }
    }

    private void remove_item(int id)
    {
        db.deleteItem(id);
        adapter.remove(adapter.getItem(id));
    }

    private void doOptionTwo(int id) {
        LatLng loc= adapter.getItem(id).get_loc();
        Uri gmmIntentUri = Uri.parse("geo:" + loc.latitude + "," + loc.longitude + "?q=landmarks");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
        //Toast.makeText(this, "Option Two Chosen...", Toast.LENGTH_LONG).show();
    }



    /**---------------------------------GoogleMaps-----------------------------------------------**/
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        LatLng rando_place = new LatLng(24.8138, 120.9675);//random place i chose for default purposes
        cur_marker = mMap.addMarker(new MarkerOptions().title("Current Chosen Location").position(rando_place).draggable(true));
        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }

    @Override
    public void onMapClick(LatLng point)
    {
        useLoc = true;
        date = new Date();
        cur_marker.setVisible(true);
        cur_marker.setPosition(point);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied)
        {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    //drawMarker
    private void drawMarker(Location location){
        LatLng ltlng = new LatLng(loc.getLatitude(), loc.getLongitude());
        cur_marker.setPosition(ltlng);
        date = new Date();
        useLoc = true;
    }

    // Displays a dialog with error message explaining that the location permission is missing.
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
            LocationManager locMan = (LocationManager)getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locMan.getBestProvider(criteria, true);
            loc = locMan.getLastKnownLocation(provider);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location)
                {
                    loc = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            locMan.requestLocationUpdates(provider, 20000, 0, locationListener);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        drawMarker(loc);
        useLoc = true;
        //        cur_marker.setPosition(mMap.getCameraPosition().target);
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