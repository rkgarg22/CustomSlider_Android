
package VerticalCustomControl;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.demo.weightdemo.R;



final class VerticalRulerView extends View {

    /**
     * Height of the view. This view height is measured in {@link #onMeasure(int, int)}.
     *
     * @see #onMeasure(int, int)
     */
    private int mViewWidth;

    public int totalViewHeight;

    /**
     * {@link Paint} for the line in the ruler view.
     *
     * @see #refreshPaint()
     */
    private Paint mIndicatorPaint;

    /**
     * {@link Paint} to display the text on the ruler view.
     *
     * @see #refreshPaint()
     */
    private Paint mTextPaint;

    /**
     * Distance interval between two subsequent indicators on the ruler.
     *
     * @see #setIndicatorIntervalDistance(int)
     * @see #getIndicatorIntervalWidth()
     */
    private int mIndicatorInterval = 14 /* Default value */;

    /**
     * Minimum value. This value will be displayed at the left-most end of the ruler. This value
     * must be less than {@link #mMaxValue}.
     *
     * @see #setValueRange(int, int)
     * @see #getMinValue()
     */
    private int mMinValue = 0 /* Default value */;

    /**
     * Maximum value. This value will be displayed at the right-most end of the ruler. This value
     * must be greater than {@link #mMinValue}.
     *
     * @see #setValueRange(int, int)
     * @see #getMaxValue()
     */
    private int mMaxValue = 100 /* Default maximum value */;

    /**
     * Ratio of long indicator height to the ruler height. This value must be between 0 to 1. The
     * value should greater than {@link #mShortIndicatorWidth}. Default value is 0.6 (i.e. 60%).
     * If the value is 0, indicator won't be displayed. If the value is 1, indicator height will be
     * same as the ruler height.
     *
     * @see #setIndicatorWidth(float, float)
     * @see #getLongIndicatorWidthRatio()
     */
    private float mLongIndicatorWidthtRatio = 0.6f /* Default value */;

    /**
     * Ratio of short indicator height to the ruler height. This value must be between 0 to 1. The
     * value should less than {@link #mLongIndicatorWidth}. Default value is 0.4 (i.e. 40%).
     * If the value is 0, indicator won't be displayed. If the value is 1, indicator height will be
     * same as the ruler height.
     *
     * @see #setIndicatorWidth(float, float)
     * @see #getShortIndicatorWidthRatio()
     */
    private float mShortIndicatorWidthRatio = 0.2f /* Default value */;

    /**
     * Actual height of the long indicator in pixels. This height is derived from
     * {@link #mLongIndicatorWidthtRatio}.
     *
     * @see #updateIndicatorWidth(float, float)
     */
    private int mLongIndicatorWidth = 0;

    /**
     * Actual height of the short indicator in pixels. This height is derived from
     * {@link #mShortIndicatorWidthRatio}.
     *
     * @see #updateIndicatorWidth(float, float)
     */
    private int mShortIndicatorWidth = 0;

    /**
     * Integer color of the text, that is displayed on the ruler.
     *
     * @see #setTextColor(int)
     * @see #getTextColor()
     */
    @ColorInt
    private int mTextColor = Color.WHITE;

    /**
     * Integer color of the indicators.
     *
     * @see #setIndicatorColor(int)
     * @see #getIndicatorColor()
     */
    @ColorInt
    private int mIndicatorColor = Color.WHITE;

    /**
     * Height of the text, that is displayed on ruler in pixels.
     *
     * @see #setTextSize(int)
     * @see #getTextSize()
     */
    @Dimension
    private int mTextSize = 36;

    /**
     * Width of the indicator in pixels.
     *
     * @see #setIndicatorWidth(int)
     * @see #getIndicatorWidth()
     */
    @Dimension
    private float mIndicatorHeightPx = 4f;

    public VerticalRulerView(@NonNull final Context context) {
        super(context);
        parseAttr(null);
    }

    public VerticalRulerView(@NonNull final Context context,
                             @Nullable final AttributeSet attrs) {
        super(context, attrs);
        parseAttr(attrs);
    }

    public VerticalRulerView(@NonNull final Context context,
                             @Nullable final AttributeSet attrs,
                             final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttr(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalRulerView(@NonNull final Context context,
                             @Nullable final AttributeSet attrs,
                             int defStyleAttr,
                             int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttr(attrs);
    }

    private void parseAttr(@Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet,
                    R.styleable.RulerView,
                    0,
                    0);

            try { //Parse params
                if (a.hasValue(R.styleable.RulerView_ruler_text_color)) {
                    mTextColor = a.getColor(R.styleable.RulerView_ruler_text_color, Color.WHITE);
                }

                if (a.hasValue(R.styleable.RulerView_ruler_text_size)) {
                    mTextSize = a.getDimensionPixelSize(R.styleable.RulerView_ruler_text_size, 14);
                }

                if (a.hasValue(R.styleable.RulerView_indicator_color)) {
                    mIndicatorColor = a.getColor(R.styleable.RulerView_indicator_color, Color.WHITE);
                }

                if (a.hasValue(R.styleable.RulerView_indicator_height)) {
                    mIndicatorHeightPx = a.getDimensionPixelSize(R.styleable.RulerView_indicator_height,
                            4);
                }

                if (a.hasValue(R.styleable.RulerView_indicator_interval)) {
                    mIndicatorInterval = a.getDimensionPixelSize(R.styleable.RulerView_indicator_interval,
                            4);
                }

                if (a.hasValue(R.styleable.RulerView_long_width_ratio)) {
                    mLongIndicatorWidthtRatio = a.getFraction(R.styleable.RulerView_long_width_ratio,
                            1, 1, 0.6f);
                }
                if (a.hasValue(R.styleable.RulerView_short_width_ratio)) {
                    mShortIndicatorWidthRatio = a.getFraction(R.styleable.RulerView_short_width_ratio,
                            1, 1, 0.4f);
                }
                setIndicatorWidth(mLongIndicatorWidthtRatio, mShortIndicatorWidthRatio);

                if (a.hasValue(R.styleable.RulerView_min_value)) {
                    mMinValue = a.getInteger(R.styleable.RulerView_min_value, 0);
                }
                if (a.hasValue(R.styleable.RulerView_max_value)) {
                    mMaxValue = a.getInteger(R.styleable.RulerView_max_value, 100);
                }
                setValueRange(mMinValue, mMaxValue);
            } finally {
                a.recycle();
            }
        }
        refreshPaint();
    }

    /**
     * Create the indicator paint and value text color.
     */
    private void refreshPaint() {
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setStrokeWidth(mIndicatorHeightPx);
        mIndicatorPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);


        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Iterate through all value
        for (int value = 0; value < mMaxValue; value++) {

            if ((value) % 12 == 0) {
                setIndicatorWidth(0.4f, 0.4f);
                setIndicatorColor(getResources().getColor(android.R.color.darker_gray));
                drawLongIndicator(canvas, value);
                drawValueText(canvas, value);
            } else if ((value ) % 6 == 0) {
                setIndicatorWidth(0.6f, 0.3f);
                setIndicatorColor(getResources().getColor(android.R.color.darker_gray));
                drawSmallIndicator(canvas, value);
            } else {
                setIndicatorWidth(0.6f, 0.2f);
                setIndicatorColor(getResources().getColor(R.color.cyan));
                drawSmallIndicator(canvas, value);
            }
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Measure dimensions
        mViewWidth  = MeasureSpec.getSize(widthMeasureSpec);
        totalViewHeight = ((mMaxValue - 1) * mIndicatorInterval) + 35;
        updateIndicatorWidth(mLongIndicatorWidthtRatio, mShortIndicatorWidthRatio);
        this.setMeasuredDimension(mViewWidth, totalViewHeight);
    }

    /**
     * Calculate and update the height of the long and the short indicators based on new ratios.
     *
     * @param longIndicatorWidthtRatio  Ratio of long indicator height to the ruler height.
     * @param shortIndicatorWidthRatio Ratio of short indicator height to the ruler height.
     */
    private void updateIndicatorWidth(final float longIndicatorWidthtRatio,
                                       final float shortIndicatorWidthRatio) {
        mLongIndicatorWidth = (int) (mViewWidth * longIndicatorWidthtRatio);
        mShortIndicatorWidth = (int) (mViewWidth * shortIndicatorWidthRatio);

    }

    /**
     * Draw the vertical short line at every value.
     *
     * @param canvas {@link Canvas} on which the line will be drawn.
     * @param value  Value to calculate the position of the indicator.
     */
    private void drawSmallIndicator(@NonNull final Canvas canvas,
                                    final int value) {
        canvas.drawLine(mViewWidth,
                (mIndicatorInterval * value) + 15 ,
                mViewWidth - mShortIndicatorWidth,
                (mIndicatorInterval * value) + 15 ,
                mIndicatorPaint);
    }


    /**
     * Draw the vertical long line.
     *
     * @param canvas {@link Canvas} on which the line will be drawn.
     * @param value  Value to calculate the position of the indicator.
     */
    private void drawLongIndicator(@NonNull final Canvas canvas,
                                   final int value) {
        canvas.drawLine(mViewWidth,
                (mIndicatorInterval * value) + 15 ,
                mViewWidth - mLongIndicatorWidth,
                (mIndicatorInterval * value) + 15,
                mIndicatorPaint);
    }

    /**
     * Draw the value number below the longer indicator. This will use {@link #mTextPaint} to draw
     * the text.
     *
     * @param canvas {@link Canvas} on which the text will be drawn.
     * @param value  Value to draw.
     */
    private void drawValueText(@NonNull final Canvas canvas,
                               final int value) {
        canvas.drawText(String.valueOf((mMaxValue-value)/ 12),
                mViewWidth - (mLongIndicatorWidth + mTextPaint.getTextSize()),
                (mIndicatorInterval * value) + (mTextPaint.getTextSize() * 0.4f) + 15 ,
                mTextPaint);

    }

    /////////////////////// Properties getter/setter ///////////////////////

    /**
     * @return Color integer value of the ruler text color.
     * @see #setTextColor(int)
     */
    @CheckResult
    @ColorInt
    int getTextColor() {
        return mIndicatorColor;
    }

    /**
     * Set the color of the text to display on the ruler.
     *
     * @param color Color integer value.
     */
    void setTextColor(@ColorInt final int color) {
        mTextColor = color;
        refreshPaint();
    }

    /**
     * @return Size of the text of ruler in pixels.
     * @see #setTextSize(int)
     */
    @CheckResult
    float getTextSize() {
        return mTextSize;
    }

    /**
     * Set the size of the text to display on the ruler.
     *
     * @param textSizeSp Text size dimension in dp.
     */
    void setTextSize(final int textSizeSp) {
        mTextSize = RulerViewUtils.sp2px(getContext(), textSizeSp);
        refreshPaint();
    }


    /**
     * @return Color integer value of the indicator color.
     * @see #setIndicatorColor(int)
     */
    @CheckResult
    @ColorInt
    int getIndicatorColor() {
        return mIndicatorColor;
    }

    /**
     * Set the indicator color.
     *
     * @param color Color integer value.
     */
    void setIndicatorColor(@ColorInt final int color) {
        mIndicatorColor = color;
        refreshPaint();
    }

    /**
     * @return Width of the indicator in pixels.
     * @see #setIndicatorWidth(int)
     */
    @CheckResult
    float getIndicatorWidth() {
        return mIndicatorHeightPx;
    }

    /**
     * Set the width of the indicator line in the ruler.
     *
     * @param widthPx Width in pixels.
     */
    void setIndicatorWidth(final int widthPx) {
        //mIndicatorWidthPx = widthPx;
        refreshPaint();
    }


    /**
     * @return Get the minimum value displayed on the ruler.
     * @see #setValueRange(int, int)
     */
    @CheckResult
    int getMinValue() {
        return mMinValue;
    }

    /**
     * @return Get the maximum value displayed on the ruler.
     * @see #setValueRange(int, int)
     */
    @CheckResult
    int getMaxValue() {
        return mMaxValue;
    }

    /**
     * Set the maximum value to display on the ruler. This will decide the range of values and number
     * of indicators that ruler will draw.
     *
     * @param minValue Value to display at the left end of the ruler. This can be positive, negative
     *                 or zero. Default minimum value is 0.
     * @param maxValue Value to display at the right end of the ruler. This can be positive, negative
     *                 or zero.This value must be greater than min value. Default minimum value is 100.
     */
    void setValueRange(final int minValue, final int maxValue) {
        mMinValue = minValue;
        mMaxValue = maxValue;
        invalidate();
    }

    /**
     * @return Get distance between two indicator in pixels.
     * @see #setIndicatorIntervalDistance(int)
     */
    @CheckResult
    int getIndicatorIntervalWidth() {
        return mIndicatorInterval;
    }

    /**
     * Set the spacing between two vertical lines/indicators. Default value is 14 pixels.
     *
     * @param indicatorIntervalPx Distance in pixels. This cannot be negative number or zero.
     * @throws IllegalArgumentException if interval is negative or zero.
     */
    void setIndicatorIntervalDistance(final int indicatorIntervalPx) {
        if (indicatorIntervalPx <= 0)
            throw new IllegalArgumentException("Interval cannot be negative or zero.");

        mIndicatorInterval = indicatorIntervalPx;
        invalidate();
    }

    /**
     * @return Ratio of long indicator height to the ruler height.
     * @see #setIndicatorWidth(float, float)
     */
    @CheckResult
    float getLongIndicatorWidthRatio() {
        return mLongIndicatorWidthtRatio;
    }

    /**
     * @return Ratio of short indicator height to the ruler height.
     * @see #setIndicatorWidth(float, float)
     */
    @CheckResult
    float getShortIndicatorWidthRatio() {
        return mShortIndicatorWidthRatio;
    }

    /**
     * Set the height of the long and short indicators.
     *
     * @param longWidthRatio  Ratio of long indicator height to the ruler height. This value must
     *                         be between 0 to 1. The value should greater than {@link #mShortIndicatorWidth}.
     *                         Default value is 0.6 (i.e. 60%). If the value is 0, indicator won't
     *                         be displayed. If the value is 1, indicator height will be same as the
     *                         ruler height.
     * @param shortWidthRatio Ratio of short indicator height to the ruler height. This value must
     *                         be between 0 to 1. The value should less than {@link #mLongIndicatorWidth}.
     *                         Default value is 0.4 (i.e. 40%). If the value is 0, indicator won't
     *                         be displayed. If the value is 1, indicator height will be same as
     *                         the ruler height.
     * @throws IllegalArgumentException if any of the parameter is invalid.
     */
    void setIndicatorWidth(final float longWidthRatio,
                            final float shortWidthRatio) {

        if (shortWidthRatio < 0 || shortWidthRatio > 1) {
            throw new IllegalArgumentException("Sort indicator height must be between 0 to 1.");
        }

        if (longWidthRatio < 0 || longWidthRatio > 1) {
            throw new IllegalArgumentException("Long indicator height must be between 0 to 1.");
        }

        if (shortWidthRatio > longWidthRatio) {
            throw new IllegalArgumentException("Long indicator height cannot be less than sort indicator height.");
        }

        mLongIndicatorWidthtRatio = longWidthRatio;
        mShortIndicatorWidthRatio = shortWidthRatio;

        updateIndicatorWidth(mLongIndicatorWidthtRatio, mShortIndicatorWidthRatio);

        invalidate();
    }
}
