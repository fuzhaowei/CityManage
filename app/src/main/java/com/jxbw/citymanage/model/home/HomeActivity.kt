package com.jxbw.citymanage.model.home

import com.jxbw.citymanage.R
import com.jxbw.citymanage.model.mine.MineActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.public_title.*
import org.jetbrains.anko.startActivity
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
        map_view.showZoomControls(false)
        map_view.
    }

    override fun setListener() {
        left_image.setOnClickListener {
            startActivity<MineActivity>()
        }
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