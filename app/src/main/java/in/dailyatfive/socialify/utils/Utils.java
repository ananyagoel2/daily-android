package in.dailyatfive.socialify.utils;

import android.text.method.KeyListener;
import android.widget.EditText;

public class Utils {

    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setTag(editText.getKeyListener());
        editText.setKeyListener(null);
    }

    public static void enableEditText(EditText editText) {
        editText.setFocusable(true);
        if(editText.getTag() != null) {
            editText.setKeyListener((KeyListener) editText.getTag());
        }
    }

}
