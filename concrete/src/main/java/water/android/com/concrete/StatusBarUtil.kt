@file:Suppress("NOTHING_TO_INLINE", "unused")

package water.android.com.concrete

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ListView
import org.jetbrains.anko.act
import org.jetbrains.anko.internals.AnkoInternals
import water.android.com.concrete.dialog.BaseDialog

/**
 * Created by EdgeDi
 * 2017/11/9 15:19
 */
inline fun Activity.setBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = color
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val view_group = window.decorView as ViewGroup
        view_group.addView(ExpandUtil.createStatusBarView(act, color))
        ExpandUtil.setRootView(act)
    }
}

inline fun Activity.setImageColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.statusBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

inline fun Context.dip2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any>) {
    AnkoInternals.internalStartActivity(activity, T::class.java, params)
}

inline fun <reified T : Activity> Fragment.startFragmentForResult(result: Int, vararg params: Pair<String, Any>) {
    startActivityForResult(AnkoInternals.createIntent(activity, T::class.java, params), result)
}

inline fun <reified T : Activity> BaseDialog.startActivity(vararg params: Pair<String, Any>) {
    AnkoInternals.internalStartActivity(context, T::class.java, params)
}

inline fun <reified T : Activity> BaseDialog.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any>) {
    AnkoInternals.internalStartActivityForResult(context, T::class.java, requestCode, params)
}

inline fun Status(editText: EditText) {
    val start = editText.selectionStart
    editText.inputType =
            if (editText.inputType == 129) {
                145
            } else {
                129
            }
    editText.setSelection(start)
}

interface OnScrollBottom {
    fun scroll()
}

inline fun ListView.setScrollBottomListener(scrollBottom: OnScrollBottom) {
    setOnScrollListener(object : AbsListView.OnScrollListener {
        override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

        }

        override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (view?.lastVisiblePosition == view?.count!! - 1) {
                    scrollBottom.scroll()
                }
            }
        }
    })
}