package water.android.com.concrete

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by EdgeDi
 * 2017/9/25 16:16
 */
abstract class ConcreteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getStatus()) {
            setBarColor(getColor())
        } else {
            setImageColor()
        }
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

    protected open fun getColor() = Color.WHITE

    /**
     * true为颜色
     * false为图片
     */
    protected open fun getStatus() = true
}