package com.demo.weightdemo;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import CustomControl.RulerValuePicker;
import CustomControl.RulerValuePickerListener;
import CustomControl.WithoutPaddingTextView;
import VerticalCustomControl.RulerValuePickerForVertical;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeightSliderScrollActivity extends AppCompatActivity {

    @BindView(R.id.heightTextViewInft)
    WithoutPaddingTextView heightTextViewInft;

    @BindView(R.id.inchTextView)
    WithoutPaddingTextView inchTextView;

    @BindView(R.id.height_picker)
    RulerValuePickerForVertical height_picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.height_slider_scroll_activity);
        ButterKnife.bind(this);
        height_picker.selectValue(54); // Maxvalue - pos
        height_picker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(final int selectedValue) {
                int ftValue = (selectedValue / 12);
                heightTextViewInft.setText(Integer.toString(ftValue));
                int reminderValueForInch = (selectedValue % 12);
                inchTextView.setText(Integer.toString(reminderValueForInch));

            }

            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                int ftValue = (selectedValue / 12);
                heightTextViewInft.setText(Integer.toString(ftValue));
                int reminderValueForInch = (selectedValue % 12);
                inchTextView.setText(Integer.toString(reminderValueForInch));
            }
        });
    }

    @OnClick(R.id.backButtonClick)
    public void backBtnClick(View v) {
        this.finish();
    }

}
