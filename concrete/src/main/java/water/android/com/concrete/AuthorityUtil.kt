package water.android.com.concrete

import android.app.Activity
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by EdgeDi
 * 2017/9/25 16:53
 */
class AuthorityUtil(val activity: Activity) {

    fun ApplyFor(permission: Array<String>, key: Int, listener: OnCheckPermission) {
        var s = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission.forEach { i ->
                if (ContextCompat.checkSelfPermission(activity, i) !== 0) {//无权限
                    ActivityCompat.requestPermissions(activity, permission, key)
                    s = true
                } else {//有权限
                    if (!s)
                        listener.Success()
                }
            }
        } else {
            listener.Success()
        }
    }

    interface OnCheckPermission {

        fun Success()

        fun Error()
    }

}