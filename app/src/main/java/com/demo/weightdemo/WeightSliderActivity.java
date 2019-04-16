package com.demo.weightdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import Adapter.WeightSliderAdapter;
import CustomControl.ScreenUtils;
import CustomControl.WithoutPaddingTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeightSliderActivity extends AppCompatActivity {

    @BindView(R.id.weightSlider)
    RecyclerView weightSliderRC;

    @BindView(R.id.weightTextView)
    WithoutPaddingTextView weightTextView;

    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_slider_activity);
        ButterKnife.bind(this);

        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        WeightSliderAdapter adapter = new WeightSliderAdapter(this);

        int padding = ScreenUtils.getScreenWidth(this) / 2 - ScreenUtils.dpToPx(this, 6);
        weightSliderRC.setPadding(padding, 0, padding, 0);
        weightSliderRC.setLayoutManager(layoutManager);
        weightSliderRC.setAdapter(adapter);
        weightSliderRC.scrollToPosition(151);
        LinearSnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(weightSliderRC);
        weightSliderRC.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int currentValue = layoutManager.findFirstVisibleItemPosition() + 1;
                weightTextView.setText(Integer.toString(currentValue));
            }
        });
    }

    @OnClick(R.id.backButtonClick)
    public void backBtnClick(View v){
        this.finish();
    }

}
