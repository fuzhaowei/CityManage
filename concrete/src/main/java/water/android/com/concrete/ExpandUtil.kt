package water.android.com.concrete

import android.app.Activity
import android.view.View
import android.view.ViewGroup

/**
 * Created by EdgeDi
 * 2017/11/9 15:30
 */
object ExpandUtil {

    fun createStatusBarView(activity: Activity, color: Int): View {
        val view = View(activity)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity))
        view.layoutParams = params
        view.setBackgroundColor(color)
        return view
    }

    private fun getStatusBarHeight(activity: Activity) =
            activity.resources.getDimensionPixelOffset(activity.resources.getIdentifier("status_bar_height", "dimen", "android"))

    fun setRootView(activity: Activity) {
        val rootView = (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rootView.fitsSystemWindows = true
    }
}