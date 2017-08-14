package gsoft.am.firebaselocatapptest1;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 7172;
    private LatLngReceiver latLngReceiver;

    private GoogleMap mMap;
//    double currentLatitude;
//    double currentLongitude;
    LatLng latLng;
    Marker currLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        latLngReceiver = new LatLngReceiver();

        // регистрируем BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(LocatAppIntentService.ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(latLngReceiver, intentFilter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            // permission has been granted, continue as usual
            mMap.setMyLocationEnabled(true);

        }



//        mMap.setMinZoomPreference(2);
        mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("I am here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0,0)));
        mMap.clear();
    }

    public class LatLngReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);


        }
    }

    private void updateUI(Intent intent) {

//        latTv.setText(Double.toString(intent.getDoubleExtra(LocatAppIntentService.EXTRA_KEY_LAT, 0)));
//        lngTv.setText(Double.toString(intent.getDoubleExtra(LocatAppIntentService.EXTRA_KEY_LNG, 0)));

        double currentLatitude = intent.getDoubleExtra(LocatAppIntentService.EXTRA_KEY_LAT, 0);
        double currentLongitude = intent.getDoubleExtra(LocatAppIntentService.EXTRA_KEY_LNG, 0);

        mMap.clear();
        latLng=new LatLng(currentLatitude,currentLongitude);

//        mMap.setMinZoomPreference(4);
        mMap.addMarker(new MarkerOptions().position(latLng).title("I am here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_ACCESS_FINE_LOCATION);
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(latLngReceiver);
    }
}
