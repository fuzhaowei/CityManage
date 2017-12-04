package water.android.com.concrete

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by EdgeDi
 * 2017/10/17 15:35
 */
class TestView(context: Context, attr: AttributeSet?) : View(context, attr) {

    constructor(context: Context) : this(context, null)

    private val paint: Paint = Paint()
    private val path = Path()
    private val measure: PathMeasure
    private val position = FloatArray(2)
    private val tan = FloatArray(2)

    init {
        paint.strokeWidth = 5f
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        path.moveTo(200f, 300f)
        path.quadTo(250f, 150f, 350f, 350f)
        path.quadTo(450f, 150f, 500f, 300f)
        path.quadTo(510f, 350f, 350f, 500f)
        path.quadTo(190f, 350f, 200f, 300f)
        measure = PathMeasure(path, false)
        measure.getPosTan(10f, position, tan)
    }

    private var length = 0f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawPath(path, paint)
        length += 0.005f
        if (length <= 1f) {
            measure.getPosTan(measure.length * length, position, tan)
            canvas.drawCircle(position[0], position[1], 10f, paint)
            invalidate()
        }
        canvas.drawCircle(position[0], position[1], 10f, paint)
    }

}