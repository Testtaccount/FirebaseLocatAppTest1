package gsoft.am.firebaselocatapptest1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class KidActivity extends AppCompatActivity {

    private EditText codeEt;
    private Button kaOkBtn;
    private DatabaseReference userDb;
    boolean isCodeRight=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid);

        userDb=FirebaseDbHelper.getDatabase().getReference("users");


        codeEt = (EditText) findViewById(R.id.code_et);
        kaOkBtn = (Button) findViewById(R.id.ka_ok_btn);


        kaOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(KidActivity.this, codeEt.getText(), Toast.LENGTH_SHORT).show();

                if(isLocationEnabled(KidActivity.this)){
                    userDb.orderByChild("code").equalTo(codeEt.getText().toString()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                            isCodeRight=true;

                            String mod = (String)dataSnapshot.child("mod").getValue();
                            if(mod.equalsIgnoreCase("kid")){


                                Intent intent=new Intent(KidActivity.this,KidHomeActivity.class);
                                intent.putExtra("kid_id",(String)dataSnapshot.child("id").getValue());
                                intent.putExtra("name",(String)dataSnapshot.child("name").getValue());


                                Log.d("testt",(String)dataSnapshot.child("name").getValue());


                                startActivity(intent);
                                finish();
                            }
                            Log.d("testt",dataSnapshot.getKey());
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(KidActivity.this,"wrong code, try again..",Toast.LENGTH_LONG).show();

                        }
                    });
                }else {
                    Toast.makeText(KidActivity.this,"Turn On Your Location..",Toast.LENGTH_LONG).show();

                    showSettingsAlert();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(KidActivity.this, MainActivity.class));
        finish();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);


        alertDialog.setTitle("GPS is not Enabled!");

        alertDialog.setMessage("Do you want to turn on GPS?");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }

}
