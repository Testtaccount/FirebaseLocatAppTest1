package gsoft.am.firebaselocatapptest1;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;

public class LocatAppService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String ACTION_SEND_LOCATION = "gsoft.am.firebaselocatapptest1.action.SEND_LOCATION";
    public static final String EXTRA_SEND_KID_ID = "gsoft.am.firebaselocatapptest1.extra.SEND_KID_ID";
    private static final String EXTRA_SEND_LAT = "gsoft.am.firebaselocatapptest1.extra.SEND_LAT";
    private static final String EXTRA_SEND_LNG = "gsoft.am.firebaselocatapptest1.extra.SEND_LNG";
    private static final String TAG = "testt";

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;

    private DatabaseReference latLngDb;


    private String kidId;
    private double lat;
    private double lng;

    public LocatAppService() {
    }

    public static void startActionSend(Context context, String kidId) {
        Intent intent = new Intent(context, LocatAppService.class);
        intent.setAction(ACTION_SEND_LOCATION);
        intent.putExtra(EXTRA_SEND_KID_ID, kidId);
        context.startService(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, " LocatAppService Created ", Toast.LENGTH_LONG).show();
        Log.i(TAG, "LocatAppService Created");

        latLngDb = FirebaseDbHelper.getDatabase().getReference("latlngs");
        buildGoogleApiClient();
        createLocationRequest();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private synchronized void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1 * 1000);      // 1 seconds, in milliseconds
//                .setFastestInterval(1 * 500); // 1/2 second, in milliseconds
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, " LocatAppService Started", Toast.LENGTH_LONG).show();
        Log.i(TAG, "LocatAppService Started");

        if (intent != null) {
            kidId = intent.getStringExtra(EXTRA_SEND_KID_ID);
            Preference.getInstance(this).setKidId(kidId);
        } else {
            kidId = Preference.getInstance(this).getKidId();
        }

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        Toast.makeText(this, " LocatAppService Destroyed ", Toast.LENGTH_LONG).show();
        Log.i(TAG, "LocatAppService Destroyed");

        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
//
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, location.toString());

////        currentLatitude = location.getLatitude();
////        currentLongitude = location.getLongitude();
////
////        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
//
        handleNewLocation(location);

//        String id = latLngDb.push().getKey();
//        LatLng latLng = new LatLng(id, lat, lng);
//        latLngDb.child(kidId).child(id).setValue(latLng);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed! " +
                        "Please check your settings and try again.",
                Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }

    private void handleNewLocation(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

        Log.i(TAG, String.valueOf(lat));
        Log.i(TAG, String.valueOf(lng));


//        new Thread(new Runnable() {
//            @Override
//            public void run() {

        String id = latLngDb.push().getKey();
        LatLng latLng = new LatLng(id, lat, lng);
        latLngDb.child(kidId).child(id).setValue(latLng);

//        Stop service once it finishes its task
//                stopSelf();
//            }
//        }).start();
    }



}
