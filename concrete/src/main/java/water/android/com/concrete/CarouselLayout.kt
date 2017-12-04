package water.android.com.concrete

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.dip
import java.util.*

/**
 * Created by EdgeDi
 * 2017/10/17 8:42
 */
class CarouselLayout(context: Context, attr: AttributeSet?) : FrameLayout(context, attr) {

    private var url_list: List<String>? = null
    private var res_list: List<Int>? = null

    private val page = ViewPager(context)
    private val line = RadioGroup(context)

    private var status = true

    /**
     * 标识状态0为默认圆形，其他的待后续实现
     */
    private var line_status = 0

    /**
     * 0为url_list,1为res_list
     */
    private var list_status = -1

    private var adapter: PageAdapter? = null

    private var time = 3000

    constructor(context: Context) : this(context, null)

    init {
        if (attr !== null) {
            val nature = context.obtainStyledAttributes(attr, R.styleable.CarouselLayout)
            line_status = nature.getInt(R.styleable.CarouselLayout_line_status, 0)
            time = nature.getInt(R.styleable.CarouselLayout_time, 3000)
            nature.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val page_params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val line_params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
        page.layoutParams = page_params
        line_params.setMargins(0, 0, 0, dip(10f))
        line.layoutParams = line_params
        line.orientation = LinearLayout.HORIZONTAL
        line.gravity = Gravity.CENTER_HORIZONTAL
        addView(page)
        addView(line)
    }

    fun initUrl(url_list: List<String>): CarouselLayout {
        this.url_list = url_list
        init()
        return this
    }

    fun initRes(res_list: List<Int>): CarouselLayout {
        this.res_list = res_list
        init()
        return this
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        when (visibility) {
            View.VISIBLE -> status = true
            View.GONE -> status = false
        }
    }

    fun setTime(time: Int) {
        this.time = time
    }

    private val image_list = arrayListOf<ImageView>()

    private fun init() {
        if (res_list === null && url_list === null) {
            LogUtil(javaClass).LogE("list数据为空，请检查数据")
            return
        }
        list_status = if (res_list === null) 0 else 1
        if (image_list.size > 0) image_list.clear()
        when (list_status) {
            0 -> {
                initLine(url_list?.size)
                max_size = url_list?.size!!
                for (i in 0 until url_list?.size!!) {
                    val image = ImageView(context)
                    image.scaleType = ImageView.ScaleType.FIT_XY
                    ImageUtil.setImage(context, url_list?.get(i)!!, image, context.resources.getDrawable(R.drawable.white))
                    image_list.add(image)
                }
            }
            1 -> {
                initLine(res_list?.size)
                max_size = res_list?.size!!
                for (i in 0 until res_list?.size!!) {
                    val image = ImageView(context)
                    image.scaleType = ImageView.ScaleType.FIT_XY
                    image.setImageResource(res_list?.get(i)!!)
                    image_list.add(image)
                }
            }
        }
        initPage(image_list)
    }

    private fun initLine(size: Int?) {
        if (line.childCount === 0) {
            val params = LinearLayout.LayoutParams(dip(5f), dip(5f))
            params.setMargins(dip(2f), 0, dip(2f), 0)
            for (i in 0 until size!!) {
                val button = RadioButton(context)
                button.buttonDrawable = null
                if (line_status === 0) {
                    button.background = context.resources.getDrawable(R.drawable.round_check)
                } else {

                }
                button.layoutParams = params
                line.addView(button)
            }
            (line.getChildAt(0) as RadioButton).isChecked = true
        }
    }

    private fun initPage(list: List<View>) {
        if (adapter === null) {
            adapter = PageAdapter(list)
            page.adapter = adapter
        } else {
            adapter?.list = list
            adapter?.notifyDataSetChanged()
        }
        page.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                (line.getChildAt(position) as RadioButton).isChecked = true
                i = position
            }

        })
        if (timer !== null) {
            timer?.cancel()
        }
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (status) {
                    page.post({
                        page.currentItem = i
                    })
                    i++
                    if (i >= max_size) {
                        i = 0
                    }
                }
            }
        }, time.toLong(), time.toLong())
    }

    private var max_size = 0
    private var i = 1
    private var timer: Timer? = null

    private class PageAdapter(var list: List<View>) : PagerAdapter() {

        override fun isViewFromObject(view: View?, `object`: Any?) = view === `object`

        override fun getCount() = list.size

        override fun instantiateItem(container: ViewGroup?, position: Int): View {
            container?.addView(list[position])
            return list[position]
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(list[position])
        }
    }

}