package com.demo.weightdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import Adapter.HeightSliderAdapter;
import Adapter.WeightSliderAdapter;
import CustomControl.ScreenUtils;
import CustomControl.WithoutPaddingTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeightSliderActivity extends AppCompatActivity {

    @BindView(R.id.heightSliderRC)
    RecyclerView heightSliderRC;

    @BindView(R.id.heightTextViewInft)
    WithoutPaddingTextView heightTextViewInft;

    @BindView(R.id.inchTextView)
    WithoutPaddingTextView inchTextView;

    LinearLayoutManager layoutManager;
    HeightSliderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.height_slider_activity);
        ButterKnife.bind(this);

        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        adapter = new HeightSliderAdapter(this);

        int padding = ScreenUtils.getScreenHeight(this) / 2 - ScreenUtils.dpToPx(this, 103);
        heightSliderRC.setPadding(0, padding, 0, padding);
        heightSliderRC.setLayoutManager(layoutManager);
        heightSliderRC.setAdapter(adapter);
        LinearSnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(heightSliderRC);
        heightSliderRC.scrollToPosition(67);

        heightSliderRC.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                int currentPosition = layoutManager.findFirstVisibleItemPosition() + 1;
                int ftValue = (currentPosition / 12);
                heightTextViewInft.setText(Integer.toString(ftValue));
                int reminderValueForInch = (currentPosition % 12);
                inchTextView.setText(Integer.toString(reminderValueForInch));

                if(layoutManager.findFirstVisibleItemPosition() == 0 && layoutManager.findLastVisibleItemPosition()==1){
                    inchTextView.setText("0");
                }
            }
        });

        heightSliderRC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = heightSliderRC.findChildViewUnder(event.getX(), event.getY());
                if (view != null) {
                    return false;
                } else {
                    return true;
                }
            }
        });
    }

    @OnClick(R.id.backButtonClick)
    public void backBtnClick(View v) {
        this.finish();
    }
}
