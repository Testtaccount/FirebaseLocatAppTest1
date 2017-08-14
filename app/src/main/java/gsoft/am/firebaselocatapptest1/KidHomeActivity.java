package gsoft.am.firebaselocatapptest1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;

public class KidHomeActivity extends AppCompatActivity {
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 7171;
    private final String TAG = "testt";
    public static final String NEW_LATLNG = "gsoft.am.firebaselocatapptest1.NEW_LATLNG";

    private TextView kidNameTv;
    String kidId;
//    protected Location mLastLocation;

//    private DatabaseReference latLngDb;
//    Intent intent;
//    private LatLngReceiver latLngReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_home);
        kidNameTv = (TextView) findViewById(R.id.kid_name_tv);


        kidId = getIntent().getStringExtra("kid_id");
        kidNameTv.setText(getIntent().getStringExtra("name"));
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
//                    PERMISSION_ACCESS_FINE_LOCATION);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            // permission has been granted, continue as usual
            LocatAppService.startActionSend(this, kidId);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                LocatAppService.startActionSend(this, kidId);
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
//
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_ACCESS_FINE_LOCATION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    LocatAppService.startActionSend(this, kidId);
//                } else {
//                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//        }

}



