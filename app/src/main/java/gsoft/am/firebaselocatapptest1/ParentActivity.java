package gsoft.am.firebaselocatapptest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.security.SecureRandom;
import java.util.Random;

public class ParentActivity extends AppCompatActivity {


    private EditText nameEt;
    private Button okBtn;
    private DatabaseReference userDb;
    private String parentId;
    private String kidId;
    String code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);


        nameEt = (EditText) findViewById(R.id.name_et);
        okBtn = (Button) findViewById(R.id.ok_btn);

        userDb = FirebaseDbHelper.getDatabase().getReference("users");

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ParentActivity.this, nameEt.getText(), Toast.LENGTH_SHORT).show();
                createParent();
                createKid();

                openParentCodeActivity();
            }
        });


    }

    private void openParentCodeActivity() {

        Intent intent=new Intent(ParentActivity.this,ParentCodeActivity.class);
        intent.putExtra("code",code);
        intent.putExtra("kidId",kidId);
        startActivity(intent);
        finish();
    }

    private void createParent() {
        //getting the values to save
        String name = "test name";
        String mode = "parent";

        //getting a unique id using push().getKey() method
        //it will create a unique id and we will use it as the Primary Key for our Artist
        parentId = userDb.push().getKey();

        code=getSaltString();
        User user = new User(parentId, name, mode, code);

        //Saving the Artist
        userDb.child(parentId).setValue(user);


        //displaying a success toast
        Toast.makeText(this, "parent added", Toast.LENGTH_SHORT).show();


    }

    private void createKid() {
        //getting the values to save
        String name = nameEt.getText().toString();
        String mode = "kid";

        //getting a unique id using push().getKey() method
        //it will create a unique id and we will use it as the Primary Key for our Artist
        kidId = userDb.push().getKey();

        User user = new User(kidId, name, mode, code);

        //Saving the Artist
        userDb.child(kidId).setValue(user);


        //displaying a success toast
        Toast.makeText(this, "kid added", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ParentActivity.this, MainActivity.class));
        finish();
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
