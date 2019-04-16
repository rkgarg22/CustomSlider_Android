package CustomControl;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class ScreenUtils {

    public static int getScreenWidth(Context context) {

        WindowManager manger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics matics = new DisplayMetrics();
        manger.getDefaultDisplay().getMetrics(matics);
        return matics.widthPixels;
    }

    public static int dpToPx(Context context, int value) {
        float floatValue = (float) value;
        return ((int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, floatValue, context.getResources().getDisplayMetrics())));
    }

    public static int getScreenHeight(Context context) {

        WindowManager manger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics matics = new DisplayMetrics();
        manger.getDefaultDisplay().getMetrics(matics);
        return matics.heightPixels;
    }
}
