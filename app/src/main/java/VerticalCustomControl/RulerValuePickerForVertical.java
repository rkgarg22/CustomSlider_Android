
package VerticalCustomControl;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.demo.weightdemo.R;

import CustomControl.RulerValuePickerListener;


/**
 * <p>
 * <li>Diagram:</li>
 * Observable ScrollView
 * |------------------|---------------------\--/----------------------|------------------|<br/>
 * |                  |                      \/                       |                  |<br/>
 * |                  |                                               |                  |<br/>
 * |  Left Spacer     |                 RulerView                     |  Right Spacer    |<br/>
 * |                  |                                               |                  |<br/>
 * |                  |                                               |                  |<br/>
 * |------------------|-----------------------------------------------|------------------|<br/>
 *
 * @see <a href="https://github.com/dwfox/DWRulerView>Original Repo</a>
 */
public final class RulerValuePickerForVertical extends FrameLayout implements ObservableVerticalScrollView.ScrollChangedListener {

    /**
     * Left side empty view to add padding to the ruler.
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private View mTopSpacer;

    /**
     * Right side empty view to add padding to the ruler.
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private View mBottomSpacer;

    /**
     * Ruler view with values.
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private VerticalRulerView mRulerView;

    /**
     * {@link ObservableVerticalScrollView}, that will host all three components.
     *
     * @see #mTopSpacer
     * @see #mBottomSpacer
     * @see #mRulerView
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private ObservableVerticalScrollView mVerticalScrollView;

    @Nullable
    private RulerValuePickerListener mListener;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private Paint mNotchPaint;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private Path mNotchPath;

    private int mNotchColor = Color.WHITE;

    /**
     * Public constructor.
     */
    public RulerValuePickerForVertical(@NonNull final Context context) {
        super(context);
        init(null);
    }

    /**
     * Public constructor.
     */
    public RulerValuePickerForVertical(@NonNull final Context context,
                                       @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * Public constructor.
     */
    public RulerValuePickerForVertical(@NonNull final Context context,
                                       @Nullable final AttributeSet attrs,
                                       final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Public constructor.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RulerValuePickerForVertical(@NonNull final Context context,
                                       @Nullable final AttributeSet attrs,
                                       final int defStyleAttr,
                                       final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /**
     * Initialize the view and parse the {@link AttributeSet}.
     *
     * @param attributeSet {@link AttributeSet} to parse or null if no attribute parameters set.
     */
    private void init(@Nullable AttributeSet attributeSet) {

        //Add all the children
        addChildViews();

        if (attributeSet != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet,
                    R.styleable.RulerValuePicker,
                    0,
                    0);

            try { //Parse params
                if (a.hasValue(R.styleable.RulerValuePicker_notch_color)) {
                    mNotchColor = a.getColor(R.styleable.RulerValuePicker_notch_color, Color.WHITE);
                }

                if (a.hasValue(R.styleable.RulerValuePicker_ruler_text_color)) {
                    setTextColor(a.getColor(R.styleable.RulerValuePicker_ruler_text_color, Color.WHITE));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_ruler_text_size)) {
                    setTextSize((int) a.getDimension(R.styleable.RulerValuePicker_ruler_text_size, 14));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_indicator_color)) {
                    setIndicatorColor(a.getColor(R.styleable.RulerValuePicker_indicator_color, Color.WHITE));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_indicator_width)) {
                    setIndicatorWidth(a.getDimensionPixelSize(R.styleable.RulerValuePicker_indicator_width,
                            4));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_indicator_interval)) {
                    setIndicatorIntervalDistance(a.getDimensionPixelSize(R.styleable.RulerValuePicker_indicator_interval,
                            4));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_long_height_height_ratio)
                        || a.hasValue(R.styleable.RulerValuePicker_short_height_height_ratio)) {

                    setIndicatorHeight(a.getFraction(R.styleable.RulerValuePicker_long_height_height_ratio,
                            1, 1, 0.6f),
                            a.getFraction(R.styleable.RulerValuePicker_short_height_height_ratio,
                                    1, 1, 0.4f));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_min_value) ||
                        a.hasValue(R.styleable.RulerValuePicker_max_value)) {
                    setMinMaxValue(a.getInteger(R.styleable.RulerValuePicker_min_value, 0),
                            a.getInteger(R.styleable.RulerValuePicker_max_value, 100));
                }
            } finally {
                a.recycle();
            }
        }

        //Prepare the notch color.
        mNotchPaint = new Paint();
        prepareNotchPaint();
        mNotchPath = new Path();
    }

    /**
     * Create the paint for notch. This will
     */
    private void prepareNotchPaint() {
        mNotchPaint.setColor(mNotchColor);
        mNotchPaint.setStrokeWidth(5f);
        mNotchPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * Programmatically add the children to the view.
     * <p>
     * <li>The main view contains the {@link android.widget.HorizontalScrollView}. That allows
     * {@link VerticalRulerView} to scroll horizontally.</li>
     * <li>{@link #mVerticalScrollView} contains {@link LinearLayout} that will act as the container
     * to hold the children inside the horizontal view.</li>
     * <li>{@link LinearLayout} container will contain three children.
     * <ul><b>Left spacer:</b> Width of this view will be the half width of the view. This will add staring at the start of the ruler.</ul>
     * <ul><b>Right spacer:</b> Width of this view will be the half width of the view. This will add ending at the end of the ruler.</ul>
     * <ul><b>{@link VerticalRulerView}:</b> Ruler view will contain the ruler with indicator.</ul>
     * </li>
     */
    private void addChildViews() {
        mVerticalScrollView = new ObservableVerticalScrollView(getContext(), this);
        mVerticalScrollView.setHorizontalScrollBarEnabled(false); //Don't display the scrollbar
        mVerticalScrollView.setVerticalScrollBarEnabled(false);
        final LinearLayout rulerContainer = new LinearLayout(getContext());
        rulerContainer.setOrientation(LinearLayout.VERTICAL);

        //Add left spacing to the container
        mTopSpacer = new View(getContext());
        rulerContainer.addView(mTopSpacer);

        //Add ruler to the container
        mRulerView = new VerticalRulerView(getContext());
        rulerContainer.addView(mRulerView);

        //Add right spacing to the container
        mBottomSpacer = new View(getContext());
        rulerContainer.addView(mBottomSpacer);


        //Add this container to the scroll view.
        mVerticalScrollView.removeAllViews();
        mVerticalScrollView.addView(rulerContainer);

        //Add scroll view to this view.
        removeAllViews();
        addView(mVerticalScrollView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw the top notch
        canvas.drawPath(mNotchPath, mNotchPaint);
    }

    @Override
    protected void onLayout(boolean isChanged, int left, int top, int right, int bottom) {
        super.onLayout(isChanged, left, top, right, bottom);

        if (isChanged) {
            final int height = getHeight();

            //Set width of the left spacer to the half of this view.
            final ViewGroup.LayoutParams leftParams = mTopSpacer.getLayoutParams();
            leftParams.height = height / 2;
            mTopSpacer.setLayoutParams(leftParams);

            //Set width of the right spacer to the half of this view.
            final ViewGroup.LayoutParams rightParams = mBottomSpacer.getLayoutParams();
            rightParams.height = height / 2;
            mBottomSpacer.setLayoutParams(rightParams);

            calculateNotchPath();

            invalidate();
        }
    }

    /**
     * Calculate notch path. Notch will be in the triangle shape at the top-center of this view.
     *
     * @see #mNotchPath
     */
    private void calculateNotchPath() {

        mNotchPath.reset();

        mNotchPath.moveTo(mRulerView.getWidth(), (getHeight() + 35) / 2 - 20);
        mNotchPath.lineTo(mRulerView.getWidth() - 30, (getHeight() + 35) / 2);
        mNotchPath.lineTo(mRulerView.getWidth(), (getHeight() + 35) / 2 + 20);
    }

    /**
     * Scroll the ruler to the given value.
     *
     * @param value Value to select. Value must be between {@link #getMinValue()} and {@link #getMaxValue()}.
     *              If the value is less than {@link #getMinValue()}, {@link #getMinValue()} will be
     *              selected.If the value is greater than {@link #getMaxValue()}, {@link #getMaxValue()}
     *              will be selected.
     */
    public void selectValue(final int value) {
        mVerticalScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int valuesToScroll;
                if (value < mRulerView.getMinValue()) {
                    valuesToScroll = 0;
                } else if (value > mRulerView.getMaxValue()) {
                    valuesToScroll = mRulerView.getMaxValue() - mRulerView.getMinValue();
                } else {
                    valuesToScroll = value - mRulerView.getMinValue();
                }

                mVerticalScrollView.smoothScrollTo(
                        0, valuesToScroll * mRulerView.getIndicatorIntervalWidth());
            }
        }, 400);
    }

    /**
     * @return Get the current selected value.
     */
    public int getCurrentValue() {
        int absoluteValue = mVerticalScrollView.getScrollY() / mRulerView.getIndicatorIntervalWidth();
        int value = mRulerView.getMaxValue() - absoluteValue - 1;

        if (value > mRulerView.getMaxValue()) {
            return mRulerView.getMaxValue();
        } else if (value < mRulerView.getMinValue()) {
            return mRulerView.getMinValue();
        } else {
            return value;
        }
    }

    @Override
    public void onScrollChanged() {
        if (mListener != null) mListener.onIntermediateValueChange(getCurrentValue());
    }

    @Override
    public void onScrollStopped() {
        makeOffsetCorrection(mRulerView.getIndicatorIntervalWidth());
        if (mListener != null) {
            mListener.onValueChange(getCurrentValue());
        }
    }

    private void makeOffsetCorrection(final int indicatorInterval) {
        int offsetValue = mVerticalScrollView.getScrollY() % indicatorInterval;
        if (offsetValue < indicatorInterval / 2) {
            mVerticalScrollView.scrollBy(0, -offsetValue);
        } else if (getCurrentValue() == 0) {
            mVerticalScrollView.scrollBy(0, indicatorInterval - offsetValue - 35);
        } else {
            mVerticalScrollView.scrollBy(0, indicatorInterval - offsetValue);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.value = getCurrentValue();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        selectValue(ss.value);
    }

    //**********************************************************************************//
    //******************************** GETTERS/SETTERS *********************************//
    //**********************************************************************************//

    /**
     * @param notchColorRes Color resource of the notch to display. Default color os {@link Color#WHITE}.
     * @see #setNotchColor(int)
     * @see #getNotchColor()
     */
    public void setNotchColorRes(@ColorRes final int notchColorRes) {
        setNotchColor(ContextCompat.getColor(getContext(), notchColorRes));
    }

    /**
     * @return Integer color of the notch. Default color os {@link Color#WHITE}.
     * @see #setNotchColor(int)
     * @see #setNotchColorRes(int)
     */
    @ColorInt
    public int getNotchColor() {
        return mNotchColor;
    }

    /**
     * @param notchColor Integer color of the notch to display. Default color os {@link Color#WHITE}.
     * @see #prepareNotchPaint()
     * @see #getNotchColor()
     */
    public void setNotchColor(@ColorInt final int notchColor) {
        mNotchColor = notchColor;
        prepareNotchPaint();
        invalidate();
    }

    /**
     * @return Color integer value of the ruler text color.
     * @see #setTextColor(int)
     * @see #setTextColorRes(int)
     */
    @CheckResult
    @ColorInt
    public int getTextColor() {
        return mRulerView.getTextColor();
    }

    /**
     * Set the color of the text to display on the ruler.
     *
     * @param color Color integer value.
     * @see #getTextColor()
     * @see VerticalRulerView#mTextColor
     */
    public void setTextColor(@ColorInt final int color) {
        mRulerView.setTextColor(color);
    }

    /**
     * Set the color of the text to display on the ruler.
     *
     * @param color Color resource id.
     * @see VerticalRulerView#mTextColor
     */
    public void setTextColorRes(@ColorRes final int color) {
        setTextColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * @return Size of the text of ruler in dp.
     * @see #setTextSize(int)
     * @see #setTextSizeRes(int)
     * @see VerticalRulerView#mTextColor
     */
    @CheckResult
    public float getTextSize() {
        return mRulerView.getTextSize();
    }

    /**
     * Set the size of the text to display on the ruler.
     *
     * @param dimensionDp Text size dimension in dp.
     * @see #getTextSize()
     * @see VerticalRulerView#mTextSize
     */
    public void setTextSize(final int dimensionDp) {
        mRulerView.setTextSize(dimensionDp);
    }

    /**
     * Set the size of the text to display on the ruler.
     *
     * @param dimension Text size dimension resource.
     * @see #getTextSize()
     * @see VerticalRulerView#mTextSize
     */
    public void setTextSizeRes(@DimenRes final int dimension) {
        setTextSize((int) getContext().getResources().getDimension(dimension));
    }

    /**
     * @return Color integer value of the indicator color.
     * @see #setIndicatorColor(int)
     * @see #setIndicatorColorRes(int)
     * @see VerticalRulerView#mIndicatorColor
     */
    @CheckResult
    @ColorInt
    public int getIndicatorColor() {
        return mRulerView.getIndicatorColor();
    }

    /**
     * Set the indicator color.
     *
     * @param color Color integer value.
     * @see #getIndicatorColor()
     * @see VerticalRulerView#mIndicatorColor
     */
    public void setIndicatorColor(@ColorInt final int color) {
        mRulerView.setIndicatorColor(color);
    }

    /**
     * Set the indicator color.
     *
     * @param color Color resource id.
     * @see #getIndicatorColor()
     * @see VerticalRulerView#mIndicatorColor
     */
    public void setIndicatorColorRes(@ColorRes final int color) {
        setIndicatorColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * @return Width of the indicator in pixels.
     * @see #setIndicatorWidth(int)
     * @see #setIndicatorWidthRes(int)
     * @see VerticalRulerView#mIndicatorHeightPx
     */
    @CheckResult
    public float getIndicatorWidth() {
        return mRulerView.getIndicatorWidth();
    }

    /**
     * Set the width of the indicator line in the ruler.
     *
     * @param widthPx Width in pixels.
     * @see #getIndicatorWidth()
     * @see VerticalRulerView#mIndicatorHeightPx
     */
    public void setIndicatorWidth(final int widthPx) {
        mRulerView.setIndicatorWidth(widthPx);
    }

    /**
     * Set the width of the indicator line in the ruler.
     *
     * @param width Dimension resource for indicator width.
     * @see #getIndicatorWidth()
     * @see VerticalRulerView#mIndicatorHeightPx
     */
    public void setIndicatorWidthRes(@DimenRes final int width) {
        setIndicatorWidth(getContext().getResources().getDimensionPixelSize(width));
    }

    /**
     * @return Get the minimum value displayed on the ruler.
     * @see #setMinMaxValue(int, int)
     * @see VerticalRulerView#mMinValue
     */
    @CheckResult
    public int getMinValue() {
        return mRulerView.getMinValue();
    }

    /**
     * @return Get the maximum value displayed on the ruler.
     * @see #setMinMaxValue(int, int)
     * @see VerticalRulerView#mMaxValue
     */
    @CheckResult
    public int getMaxValue() {
        return mRulerView.getMaxValue();
    }

    /**
     * Set the maximum value to display on the ruler. This will decide the range of values and number
     * of indicators that ruler will draw.
     *
     * @param minValue Value to display at the left end of the ruler. This can be positive, negative
     *                 or zero. Default minimum value is 0.
     * @param maxValue Value to display at the right end of the ruler. This can be positive, negative
     *                 or zero.This value must be greater than min value. Default minimum value is 100.
     * @see #getMinValue()
     * @see #getMaxValue()
     */
    public void setMinMaxValue(final int minValue, final int maxValue) {
        mRulerView.setValueRange(minValue, maxValue);
        invalidate();
        selectValue(minValue);
    }

    /**
     * @return Get distance between two indicator in pixels.
     * @see #setIndicatorIntervalDistance(int)
     * @see VerticalRulerView#mIndicatorInterval
     */
    @CheckResult
    public int getIndicatorIntervalWidth() {
        return mRulerView.getIndicatorIntervalWidth();
    }

    /**
     * Set the spacing between two vertical lines/indicators. Default value is 14 pixels.
     *
     * @param indicatorIntervalPx Distance in pixels. This cannot be negative number or zero.
     * @see VerticalRulerView#mIndicatorInterval
     */
    public void setIndicatorIntervalDistance(final int indicatorIntervalPx) {
        mRulerView.setIndicatorIntervalDistance(indicatorIntervalPx);
    }

    /**
     * @return Ratio of long indicator height to the ruler height.
     * @see #setIndicatorHeight(float, float)
     * @see VerticalRulerView#mLongIndicatorWidthRatio
     */
    @CheckResult
    public float getLongIndicatorWidthRatio() {
        return mRulerView.getLongIndicatorWidthRatio();
    }

    /**
     * @return Ratio of short indicator height to the ruler height.
     * @see #setIndicatorHeight(float, float)
     * @see VerticalRulerView#mShortIndicatorWidth
     */
    @CheckResult
    public float getShortIndicatorWidthRatio() {
        return mRulerView.getShortIndicatorWidthRatio();
    }

    /**
     * Set the height of the long and short indicators.
     *
     * @param longHeightRatio  Ratio of long indicator height to the ruler height. This value must
     *                         be between 0 to 1. The value should greater than {@link #getShortIndicatorWidthRatio()}.
     *                         Default value is 0.6 (i.e. 60%). If the value is 0, indicator won't
     *                         be displayed. If the value is 1, indicator height will be same as the
     *                         ruler height.
     * @param shortHeightRatio Ratio of short indicator height to the ruler height. This value must
     *                         be between 0 to 1. The value should less than {@link #getLongIndicatorWidthRatio()}.
     *                         Default value is 0.4 (i.e. 40%). If the value is 0, indicator won't
     *                         be displayed. If the value is 1, indicator height will be same as
     *                         the ruler height.
     * @see #getLongIndicatorWidthRatio()
     * @see #getShortIndicatorWidthRatio()
     */
    public void setIndicatorHeight(final float longHeightRatio,
                                   final float shortHeightRatio) {
        mRulerView.setIndicatorWidth(longHeightRatio, shortHeightRatio);
    }

    /**
     * Set the {@link RulerValuePickerListener} to get callbacks when the value changes.
     *
     * @param listener {@link RulerValuePickerListener}
     */
    public void setValuePickerListener(@Nullable final RulerValuePickerListener listener) {
        mListener = listener;
    }

    /**
     * User interface state that is stored by RulerView for implementing
     * {@link View#onSaveInstanceState}.
     */
    public static class SavedState extends BaseSavedState {

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

        private int value = 0;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            value = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(value);
        }
    }
}
