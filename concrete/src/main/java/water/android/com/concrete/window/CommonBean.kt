package water.android.com.concrete.window

/**
 * Created by EdgeDi
 * 2017/10/19 15:01
 */
data class CommonBean(val result: String) : CommonString {

    override fun toResult() = result
}