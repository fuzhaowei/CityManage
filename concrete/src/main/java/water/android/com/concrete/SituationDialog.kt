package water.android.com.concrete

import android.app.Dialog
import android.content.Context
import android.os.Bundle

/**
 * Created by EdgeDi
 * 2017/11/16 9:09
 */
class SituationDialog(context: Context) : Dialog(context, R.style.BaseDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_situation)
    }

    override fun onContentChanged() {
        super.onContentChanged()
        initUI()
        setListener()
    }

    private fun initUI() {

    }

    private fun setListener() {

    }

}