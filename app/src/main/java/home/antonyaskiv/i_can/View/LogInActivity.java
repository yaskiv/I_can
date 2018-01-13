package home.antonyaskiv.i_can.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import home.antonyaskiv.i_can.R;

public class LogInActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }
    public void Click(View view)
    {
        startActivity(new Intent(this,MainActivity.class));
    }


}
