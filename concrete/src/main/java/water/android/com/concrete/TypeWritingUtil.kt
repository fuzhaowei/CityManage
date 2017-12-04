package water.android.com.concrete

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Created by EdgeDi
 * 2017/10/30 9:41
 */
object TypeWritingUtil {


    fun ShowType(activity: Activity, editText: EditText) {
        val input = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
    }

    fun HideType(activity: Activity, editText: EditText) {
        val input = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromInputMethod(editText.windowToken, 0)
    }

    fun StatusType(activity: Activity) {
        val input = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}