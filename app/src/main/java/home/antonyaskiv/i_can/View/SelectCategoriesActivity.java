package home.antonyaskiv.i_can.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import home.antonyaskiv.i_can.Model.Categories;
import home.antonyaskiv.i_can.Presenters.ImplMapFragmentPresenter;

import home.antonyaskiv.i_can.R;

public class SelectCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_categories);
    }


    public void OpenMainActivity(View view){
        List<Categories> list = new ArrayList<>();
        list.add(new Categories("bla1"));
        list.add(new Categories("bla2"));
        list.add(new Categories("bla3"));
        list.add(new Categories("bla4"));
        ImplMapFragmentPresenter.insertOrUpdateCategories(list);
        Intent intent = new Intent(SelectCategoriesActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
