package gsoft.am.firebaselocatapptest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button parentBtn=(Button)findViewById(R.id.parent_btn);
        Button kidBtn=(Button)findViewById(R.id.kid_btn);
        parentBtn.setOnClickListener(this);
        kidBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.parent_btn:
                startActivity(new Intent(MainActivity.this, ParentActivity.class));
                finish();
                break;
            case R.id.kid_btn:
                startActivity(new Intent(MainActivity.this, KidActivity.class));
                finish();
                break;
        }
    }
}
