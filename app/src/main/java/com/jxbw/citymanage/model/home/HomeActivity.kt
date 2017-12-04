package com.jxbw.citymanage.model.home

import com.jxbw.citymanage.R
import kotlinx.android.synthetic.main.activity_home.*
import water.android.com.concrete.ConcreteActivity
import water.android.com.concrete.ToastShow

/**
 * Created by EdgeDi
 * 2017/12/4 17:34
 */
class HomeActivity : ConcreteActivity() {

    override fun layout() = R.layout.activity_home

    override fun getColor() = resources.getColor(R.color.title_background)

    override fun initUI() {

    }

    override fun setListener() {

    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
    }

    private var finish = false

    override fun onBackPressed() {
        if (!finish) {
            ToastShow("再次点击退出程序")
            finish = true
            home_linear.postDelayed({ finish = false }, 2000)
            return
        }
        super.onBackPressed()
    }
}