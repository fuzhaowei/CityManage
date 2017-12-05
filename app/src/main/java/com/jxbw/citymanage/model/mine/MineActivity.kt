package com.jxbw.citymanage.model.mine

import android.view.View
import com.jxbw.citymanage.R
import kotlinx.android.synthetic.main.public_title.*
import water.android.com.concrete.ConcreteActivity

/**
 * Created by EdgeDi
 * 2017/12/5 8:43
 */
class MineActivity : ConcreteActivity() {

    override fun layout() = R.layout.activity_mine

    override fun getColor() = resources.getColor(R.color.title_background)

    override fun initUI() {
        left_image.visibility = View.INVISIBLE
        center_title.text = "我的设置"
    }

    override fun setListener() {

    }

}