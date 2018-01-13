package home.antonyaskiv.i_can.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import home.antonyaskiv.i_can.R;

public class SelectCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_categories);
    }


    public void OpenMainActivity(View view){
        Intent intent = new Intent(SelectCategoriesActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
