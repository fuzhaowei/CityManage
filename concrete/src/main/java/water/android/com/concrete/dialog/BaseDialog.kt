package water.android.com.concrete.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import org.jetbrains.anko.internals.AnkoInternals
import water.android.com.concrete.R

/**
 * Created by EdgeDi
 * 2017/10/30 16:59
 */
abstract class BaseDialog(val context: Activity) : Dialog(context, R.style.BaseDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout())
    }

    override fun onContentChanged() {
        super.onContentChanged()
        initUI()
        setListener()
    }

    abstract fun layout(): Int

    abstract fun initUI()

    abstract fun setListener()

    protected fun ToastShow(result: String, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, result, time).show()
    }

}