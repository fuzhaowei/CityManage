package water.android.com.concrete

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

/**
 * Created by EdgeDi
 * 2017/10/17 14:06
 */
class ScrollGridView(context: Context, attr: AttributeSet?) : GridView(context, attr) {

    constructor(context: Context) : this(context = context, attr = null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}