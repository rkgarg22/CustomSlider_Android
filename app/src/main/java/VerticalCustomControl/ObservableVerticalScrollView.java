
package VerticalCustomControl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;


@SuppressLint("ViewConstructor")
final class ObservableVerticalScrollView extends ScrollView {
    private static final long NEW_CHECK_DURATION = 100L;

    private long mLastScrollUpdateMills = -1;

    @Nullable
    private ScrollChangedListener mScrollChangedListener;

    private Runnable mScrollerTask = new Runnable() {

        public void run() {
            if (System.currentTimeMillis() - mLastScrollUpdateMills > NEW_CHECK_DURATION) {
                mLastScrollUpdateMills = -1;
                mScrollChangedListener.onScrollStopped();
            } else {
                //Post next delay
                postDelayed(this, NEW_CHECK_DURATION);
            }
        }
    };

    /**
     * Constructor.
     *
     * @param context  {@link Context} of caller.
     * @param listener {@link ScrollChangedListener} to get callbacks when scroll starts or stops.
     * @see ScrollChangedListener
     */
    public ObservableVerticalScrollView(@NonNull final Context context,
                                        @NonNull final ScrollChangedListener listener) {
        super(context);
        mScrollChangedListener = listener;
    }

    @Override
    protected void onScrollChanged(final int horizontalOrigin,
                                   final int verticalOrigin,
                                   final int oldHorizontalOrigin,
                                   final int oldVerticalOrigin) {
        super.onScrollChanged(horizontalOrigin, verticalOrigin, oldHorizontalOrigin, oldVerticalOrigin);
        if (mScrollChangedListener == null) return;
        mScrollChangedListener.onScrollChanged();

        if (mLastScrollUpdateMills == -1) postDelayed(mScrollerTask, NEW_CHECK_DURATION);
        mLastScrollUpdateMills = System.currentTimeMillis();
    }

    /**
     * Listener to get callbacks on scrollview scroll events.
     */
    interface ScrollChangedListener {

        /**
         * Called upon change in scroll position.
         */
        void onScrollChanged();

        /**
         * Called when the scrollview stops scrolling.
         */
        void onScrollStopped();
    }
}



