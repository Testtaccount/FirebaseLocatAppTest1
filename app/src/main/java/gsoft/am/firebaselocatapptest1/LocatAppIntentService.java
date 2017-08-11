package gsoft.am.firebaselocatapptest1;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LocatAppIntentService extends IntentService {

    private static final String TAG = "testt";



    public static final String ACTION = "gsoft.am.firebaselocatapptest1.action.LocatAppIntentService";

    private static final String ACTION_GET_LOCATION = "gsoft.am.firebaselocatapptest1.action.GET_LOCATION";
    public static final String EXTRA_GET_KID_ID = "gsoft.am.firebaselocatapptest1.extra.GET_KID_ID";

    public static final String EXTRA_KEY_LAT = "gsoft.am.firebaselocatapptest1.action.LocatAppIntentService.EXTRA_KEY_LAT";
    public static final String EXTRA_KEY_LNG = "gsoft.am.firebaselocatapptest1.action.LocatAppIntentService.EXTRA_KEY_LNG";

//    private DatabaseReference latLngDb;
    private DatabaseReference latLngDbChild;


    public LocatAppIntentService() {
        super("LocatAppIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        latLngDb = FirebaseDbHelper.getDatabase().getReference("latlngs");

    }

//    public static void startActionSend(Context context, double d1, double d2, String kidId) {
//        Intent intent = new Intent(context, LocatAppIntentService.class);
//        intent.setAction(ACTION_SEND_LOCATION);
//        intent.putExtra(EXTRA_SEND_LAT, d1);
//        intent.putExtra(EXTRA_SEND_LNG, d2);
//        intent.putExtra(EXTRA_SEND_KID_ID, kidId);
//        context.startService(intent);
//    }


    public static void startActionGet(Context context, String kidId) {
        Intent intent = new Intent(context, LocatAppIntentService.class);
        intent.setAction(ACTION_GET_LOCATION);
        intent.putExtra(EXTRA_GET_KID_ID, kidId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_SEND_LOCATION.equals(action)) {
//                double lat = intent.getDoubleExtra(EXTRA_SEND_LAT, 0);
//                double lng = intent.getDoubleExtra(EXTRA_SEND_LNG, 0);
//                String kidId = intent.getStringExtra(EXTRA_SEND_KID_ID);
//
//                handleActionSend(lat, lng, kidId);
//            } else if (ACTION_GET_LOCATION.equals(action)) {

                String kidId = intent.getStringExtra(EXTRA_GET_KID_ID);

                handleActionGet(kidId);
//            }
        }
    }


//    private void handleActionSend(double lat, double lng, String kidId) {
//
//        String id = latLngDb.push().getKey();
//
//        LatLng latLng = new LatLng(id, lat, lng);
//
//        Log.i(TAG, latLng.toString());
//
//        latLngDb.child(kidId).child(id).setValue(latLng);
//
//
//        Toast.makeText(this, "latlng saved", Toast.LENGTH_LONG).show();
//    }


    private void handleActionGet(String kidId) {

        latLngDbChild = FirebaseDbHelper.getDatabase().getReference("latlngs").child(kidId);

        latLngDbChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {

                    LatLng location = locationSnapshot.getValue(LatLng.class);

                    // возвращаем результат
                    Intent responseIntent = new Intent();
                    responseIntent.setAction(ACTION);
                    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    responseIntent.putExtra(EXTRA_KEY_LAT, location != null ? location.getLatitude() : 0);
                    responseIntent.putExtra(EXTRA_KEY_LNG, location != null ? location.getLongitude() : 0);
                    responseIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendBroadcast(responseIntent);
                    Log.d("testt", "LocatAppIntentService location: " + location.getLatitude()+" "+location.getLongitude()); //log
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
