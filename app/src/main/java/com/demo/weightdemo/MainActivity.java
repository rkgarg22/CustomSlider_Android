package com.demo.weightdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import Adapter.WeightSliderAdapter;
import CustomControl.ScreenUtils;
import CustomControl.WithoutPaddingTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.weightSldierBtn)
    public void weightSliderBtnClick(View v) {
        Intent weightSliderBtn = new Intent(this, WeightSliderScrollActivity.class);
        startActivity(weightSliderBtn);
    }

    @OnClick(R.id.heightSldierBtn)
    public void heightSliderBtnClick(View v) {
        Intent heightSliderBtn = new Intent(this, HeightSliderScrollActivity.class);
        startActivity(heightSliderBtn);
    }
}
