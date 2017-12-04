package water.android.com.concrete.picture

import android.content.Context
import water.android.com.concrete.ConcreteAdapter
import water.android.com.concrete.holder.ViewHolder

/**
 * Created by EdgeDi
 * 2017/10/27 11:34
 */
class PicAdapter(context: Context, list: List<String>, layout: Int) : ConcreteAdapter<String>(list, context, layout) {

    override fun initialise(holder: ViewHolder?, item: String, position: Int) {

    }

}