package gsoft.am.firebaselocatapptest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(KidActivity.this, MainActivity.class));
        finish();
    }
}
