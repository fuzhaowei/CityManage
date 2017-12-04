package water.android.com.concrete

import android.util.Log

/**
 * Created by EdgeDi
 * 2017/10/17 8:59
 */
class LogUtil(val cla: Class<*>) {

    fun LogE(result: String) {
        Log.e("****************-----", "-----****************")
        Log.e("****************-----", "-----****************")
        Log.e("****************-----", "-----****************")
        Log.e(cla.toString(), result)
        Log.e("****************-----", "-----****************")
        Log.e("****************-----", "-----****************")
        Log.e("****************-----", "-----****************")
    }

}