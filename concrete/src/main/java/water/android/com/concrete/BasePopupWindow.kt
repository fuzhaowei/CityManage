package water.android.com.concrete

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow

/**
 * Created by EdgeDi
 * 2017/10/16 13:54
 */
abstract class BasePopupWindow(val activity: Activity) {

    private var window: PopupWindow? = null
    protected var view: View? = null
    private val inflater = LayoutInflater.from(activity)
    private var hideListener: onWindowHideListener? = null

    fun getHideListener() = hideListener

    fun setHideListener(hideListener: onWindowHideListener) {
        this.hideListener = hideListener
    }

    init {
        onCreate()
    }

    private fun onCreate() {
        if (view === null) {
            view = inflater.inflate(layout(), null)
            initUI()
            setListener()
            window = PopupWindow(view,
                    if (getWidth() == 0) {
                        WindowManager.LayoutParams.MATCH_PARENT
                    } else {
                        getWidth()
                    },
                    when {
                        getHeith() == 0 -> WindowManager.LayoutParams.WRAP_CONTENT
                        getHeith() == -1 -> WindowManager.LayoutParams.MATCH_PARENT
                        else -> getHeith()
                    }
                    , true)
            window?.isFocusable = true
            window?.isOutsideTouchable = true
            window?.setBackgroundDrawable(ColorDrawable())
            window?.setOnDismissListener({
                if (getAlter())
                    alter(1f)
                if (getHideListener() != null) {
                    getHideListener()?.hide()
                }
            })
        }
    }

    open protected fun getWidth() = 0

    open protected fun getHeith() = 0

    open fun show(view: View, x: Int = 0, y: Int = 0) {
        if (getAlter())
            alter(0.5f)
        window?.showAsDropDown(view, x, y)
    }

    open fun showBottom(view: View) {
        if (getAlter())
            alter(0.5f)
        window?.showAtLocation(view, Gravity.BOTTOM, 0, 0)
    }

    open fun hide() {
        if (getAlter())
            alter(0f)
        window?.dismiss()
    }

    private fun alter(level: Float) {
        val lp = activity.window.attributes
        lp.alpha = level
        activity.window.attributes = lp
    }

    open fun getAlter() = true

    abstract fun layout(): Int

    abstract fun initUI()

    abstract fun setListener()

    interface onWindowHideListener {
        fun hide()
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = activity.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}

