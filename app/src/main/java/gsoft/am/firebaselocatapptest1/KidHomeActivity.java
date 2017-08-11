package gsoft.am.firebaselocatapptest1;

import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class KidHomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private final String TAG = "testt";
    public static final String NEW_LATLNG = "gsoft.am.firebaselocatapptest1.NEW_LATLNG";

    private TextView kidNameTv;

    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    protected GoogleApiClient mGoogleApiClient;
//    private DatabaseReference latLngDb;
//    Intent intent;
//    private LatLngReceiver latLngReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_home);
        kidNameTv = (TextView) findViewById(R.id.kid_name_tv);
        kidNameTv.setText(getIntent().getStringExtra("name"));
//        latLngDb = FirebaseDbHelper.getDatabase().getReference("latlngs");
//        intent = new Intent(KidHomeActivity.NEW_LATLNG);
//        latLngReceiver = new LatLngReceiver();
//        // регистрируем BroadcastReceiver
//        IntentFilter intentFilter = new IntentFilter(
//                LocationAppIntentService.ACTION);
//        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
//        registerReceiver(latLngReceiver, intentFilter);
        buildGoogleApiClient();

    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(latLngReceiver);
//    }


//    public class LatLngReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            updateUI(intent);
//        }
//    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, location.toString());
        String kidId = getIntent().getStringExtra("kid_id");
        double lat = location.getLatitude();
        double lng = location.getLongitude();


        LocatAppService.startActionSend(this, lat, lng, kidId);


        //txtOutput.setText(location.toString());
//
//        mLatitudeText.setText(String.valueOf(location.getLatitude()));
//        mLongitudeText.setText(String.valueOf(location.getLongitude()));
        Log.i(TAG, String.valueOf(location.getLatitude()));
        Log.i(TAG, String.valueOf(location.getLongitude()));

//        String id = latLngDb.push().getKey();
//        LatLng latLng = new LatLng(id, location.getLatitude(), location.getLongitude());
//
//        Log.i(TAG, latLng.toString());
//        latLngDb.child(kidId).child(id).setValue(latLng);
//
//
//        sendLatLngs(kidId);
//
//        Toast.makeText(this, "latlng saved", Toast.LENGTH_LONG).show();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1 * 1000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


}
