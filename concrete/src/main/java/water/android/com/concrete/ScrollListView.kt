package water.android.com.concrete

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView

/**
 * Created by EdgeDi
 * 2017/10/17 14:01
 */
class ScrollListView(context: Context, attr: AttributeSet?) : ListView(context, attr) {

    constructor(context: Context) : this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }

}