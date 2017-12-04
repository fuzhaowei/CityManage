package water.android.com.concrete

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import water.android.com.concrete.holder.ViewHolder

/**
 * Created by EdgeDi
 * 2017/9/25 17:53
 */
abstract class ConcreteAdapter<T>(var list: List<T>, val context: Context, val layout: Int) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val holder: ViewHolder
        if (p1 == null) {
            val view = inflater.inflate(layout, p2, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = p1.tag as ViewHolder
        }
        initialise(holder, getItem(p0), p0)
        return holder.view
    }

    override fun getItem(p0: Int) = list[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getCount() = list.size

    abstract fun initialise(holder: ViewHolder?, item: T, position: Int)

}