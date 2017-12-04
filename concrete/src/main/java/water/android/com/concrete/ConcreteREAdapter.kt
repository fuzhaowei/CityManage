package water.android.com.concrete

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import water.android.com.concrete.holder.ViewHolder

/**
 * Created by EdgeDi
 * 2017/9/25 17:52
 */
abstract class ConcreteREAdapter<T>(var list: List<T>, val context: Context, val layout: Int) : RecyclerView.Adapter<ViewHolder>() {

    private var head_view: View? = null
    private var onItemListener: OnItemListener<T>? = null
    private val inflater = LayoutInflater.from(context)

    fun setOnItemListener(onItemListener: OnItemListener<T>) {
        this.onItemListener = onItemListener
    }

    fun addHeadView(view: View) {
        head_view = view
    }

    override fun getItemCount(): Int {
        if (head_view != null) {
            return list.size + 1
        }
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.view?.setOnClickListener {
            onItemListener?.item(getItem(position), position, holder.view)
        }
        val item = getItem(position)
        if (item != null) {
            initialise(holder, item, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (head_view != null) {
            return ViewHolder(head_view!!)
        }
        return ViewHolder(inflater.inflate(layout, parent, false))
    }

    abstract fun initialise(holder: ViewHolder?, item: T, position: Int)

    open fun getItem(position: Int): T? {
        if (head_view != null) {
            return if (position == 0) {
                null
            } else {
                list[position - 1]
            }
        }
        return list[position]
    }

    interface OnItemListener<T> {
        fun item(item: T?, position: Int, view: View?)
    }
}