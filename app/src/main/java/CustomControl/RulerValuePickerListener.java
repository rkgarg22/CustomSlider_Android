

package CustomControl;

/**
 * Created by Kevalpatel2106 on 29-Mar-18.
 * Listener to get the callback for {@link RulerValuePicker} events.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
public interface RulerValuePickerListener {

    void onValueChange(int selectedValue);

    void onIntermediateValueChange(int selectedValue);
}
