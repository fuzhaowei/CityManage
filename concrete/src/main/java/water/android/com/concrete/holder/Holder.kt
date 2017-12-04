package water.android.com.concrete.holder

import android.graphics.drawable.Drawable
import android.view.View

/**
 * Created by EdgeDi
 * 2017/9/25 17:41
 */
interface Holder {

    fun bind(rid: Int): View

    fun setText(rid: Int, content: String): Holder

    fun setImage(rid: Int, resources: Int): Holder

    fun setProject(rid: Int, content: String, color: Int): Holder

    fun setStatus(rid: Int, status: Int): Holder

    fun setOnClickListener(rid: Int, onClickListener: View.OnClickListener): Holder

    fun setTextColor(rid: Int, content: String, color: Int): Holder

    fun setProjectDetail(rid: Int, level: String, content: String): Holder

    fun setDrawableColor(rid: Int, color: Int): Holder

    fun setDrawable(rid: Int, color: Int): Holder
}