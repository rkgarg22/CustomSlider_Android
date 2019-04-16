package com.demo.weightdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import Adapter.WeightSliderAdapter;
import CustomControl.RulerValuePicker;
import CustomControl.RulerValuePickerListener;
import CustomControl.ScreenUtils;
import CustomControl.WithoutPaddingTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeightSliderScrollActivity extends AppCompatActivity {

    @BindView(R.id.weightTextView)
    WithoutPaddingTextView weightTextView;

    @BindView(R.id.weight_ruler_picker)
    RulerValuePicker weight_ruler_picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_slider_scroll_activity);
        ButterKnife.bind(this);
        weight_ruler_picker.selectValue(152);
        weight_ruler_picker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(final int selectedValue) {
                weightTextView.setText(Integer.toString(selectedValue));

            }

            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                weightTextView.setText(Integer.toString(selectedValue));

            }
        });
    }

    @OnClick(R.id.backButtonClick)
    public void backBtnClick(View v){
        this.finish();
    }

}
