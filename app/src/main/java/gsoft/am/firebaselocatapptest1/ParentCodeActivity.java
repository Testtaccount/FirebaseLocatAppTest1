package gsoft.am.firebaselocatapptest1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ParentCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView codeTv;

    private TextView latTv;
    private TextView lngTv;
    private LatLngReceiver latLngReceiver;

    private Button codeEnteredBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_code);
        codeTv = (TextView) findViewById(R.id.code_tv);
        latTv = (TextView) findViewById(R.id.lat_tv);
        lngTv = (TextView) findViewById(R.id.lng_tv);

        codeEnteredBtn = (Button) findViewById(R.id.code_ok_enter_btn);
        codeEnteredBtn.setOnClickListener(this);

        codeTv.setText(getIntent().getStringExtra("code"));


        latLngReceiver = new LatLngReceiver();

        // регистрируем BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(LocatAppIntentService.ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(latLngReceiver, intentFilter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(latLngReceiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_ok_enter_btn:

                String kidId = getIntent().getStringExtra("kidId");
                LocatAppIntentService.startActionGet(this, kidId);
//
//                Intent i = new Intent(this, LocationAppIntentService.class);
//                // Add extras to the bundle
////        i.putExtra("longitude", location.getLatitude());
////        i.putExtra("latitude", location.getLongitude());
//                i.putExtra("id", id);
//                // Start the service
//                startService(i);

                break;
        }
    }

    private void updateUI(Intent intent) {

        codeTv.setVisibility(View.GONE);
        codeEnteredBtn.setVisibility(View.GONE);
        latTv.setVisibility(View.VISIBLE);
        lngTv.setVisibility(View.VISIBLE);
        latTv.setText(Double.toString(intent.getDoubleExtra(LocatAppIntentService.EXTRA_KEY_LAT, 0)));
        lngTv.setText(Double.toString(intent.getDoubleExtra(LocatAppIntentService.EXTRA_KEY_LNG, 0)));




    }

    public class LatLngReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    }


}
