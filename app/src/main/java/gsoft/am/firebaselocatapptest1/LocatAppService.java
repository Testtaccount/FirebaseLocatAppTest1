package gsoft.am.firebaselocatapptest1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class LocatAppService extends Service {

    private static final String ACTION_SEND_LOCATION = "gsoft.am.firebaselocatapptest1.action.SEND_LOCATION";
    private static final String EXTRA_SEND_KID_ID = "gsoft.am.firebaselocatapptest1.extra.SEND_KID_ID";
    private static final String EXTRA_SEND_LAT = "gsoft.am.firebaselocatapptest1.extra.SEND_LAT";
    private static final String EXTRA_SEND_LNG = "gsoft.am.firebaselocatapptest1.extra.SEND_LNG";
    private static final String TAG = "testt";
    private DatabaseReference latLngDb;
    String kidId;
    double lat;
    double lng;

    public static void startActionSend(Context context, double d1, double d2, String kidId) {
        Intent intent = new Intent(context, LocatAppService.class);
        intent.setAction(ACTION_SEND_LOCATION);
        intent.putExtra(EXTRA_SEND_LAT, d1);
        intent.putExtra(EXTRA_SEND_LNG, d2);
        intent.putExtra(EXTRA_SEND_KID_ID, kidId);
        context.startService(intent);
    }

    public LocatAppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, " LocatAppService Created ", Toast.LENGTH_LONG).show();
        latLngDb = FirebaseDbHelper.getDatabase().getReference("latlngs");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, " LocatAppService Started", Toast.LENGTH_LONG).show();

        lat = intent.getDoubleExtra(EXTRA_SEND_LAT, 0);
        lng = intent.getDoubleExtra(EXTRA_SEND_LNG, 0);
        kidId = intent.getStringExtra(EXTRA_SEND_KID_ID);

        Log.i(TAG, String.valueOf(lat));
        Log.i(TAG, String.valueOf(lng));


        new Thread(new Runnable() {
            @Override
            public void run() {


                String id = latLngDb.push().getKey();
                LatLng latLng = new LatLng(id, lat, lng);
                latLngDb.child(kidId).child(id).setValue(latLng);

                Log.i(TAG, latLng.toString());

                //Stop service once it finishes its task
                stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(this, " LocatAppService Destroyed", Toast.LENGTH_LONG).show();
//
//    }
}
