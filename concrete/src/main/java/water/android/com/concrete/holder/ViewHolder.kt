package water.android.com.concrete.holder

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by EdgeDi
 * 2017/9/25 17:47
 */
class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), Holder {

    private val views = SparseArray<View>()

    override fun bind(rid: Int) =
            (if (views[rid] === null) {
                views.append(rid, view.findViewById(rid))
                views[rid]
            } else {
                views[rid]
            })!!

    override fun setText(rid: Int, content: String): Holder {
        val text = bind(rid) as TextView?
        text?.text = content
        return this
    }

    override fun setTextColor(rid: Int, content: String, color: Int): Holder {
        val text = bind(rid) as TextView?
        text?.text = content
        text?.setTextColor(color)
        return this
    }

    override fun setImage(rid: Int, resources: Int): Holder {
        val image = bind(rid) as ImageView?
        image?.setImageResource(resources)
        return this
    }

    override fun setProject(rid: Int, content: String, color: Int): Holder {
        val view = bind(rid) as TextView
        view.text = content
        val graw = view.background as GradientDrawable
        graw.setColor(color)
        view.background = graw
        return this
    }

    override fun setStatus(rid: Int, status: Int): Holder {
        val view = bind(rid)
        view.visibility = status
        return this
    }

    override fun setOnClickListener(rid: Int, onClickListener: View.OnClickListener): Holder {
        bind(rid).setOnClickListener(onClickListener)
        return this
    }

    override fun setProjectDetail(rid: Int, level: String, content: String): Holder {
        val view = bind(rid) as TextView
        val drawable = view.background as GradientDrawable
        when (level) {
            "前期" -> {
                drawable.setColor(Color.parseColor("#FA9B84"))
            }
            "施工" -> {
                drawable.setColor(Color.parseColor("#1D83CB"))
            }
            else -> {
                drawable.setColor(Color.parseColor("#FCA14F"))
            }
        }
        view.background = drawable
        view.text = content
        return this
    }

    override fun setDrawableColor(rid: Int, color: Int): Holder {
        val view = bind(rid)
        val gradientDrawable = view.background as GradientDrawable
        gradientDrawable.setColor(color)
        view.background = gradientDrawable
        return this
    }

    override fun setDrawable(rid: Int, color: Int): Holder {
        val view = bind(rid)
        view.setBackgroundColor(color)
        return this
    }

}