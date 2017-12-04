package com.jxbw.citymanage.model.login

import android.view.View
import com.jxbw.citymanage.R
import com.jxbw.citymanage.model.home.HomeActivity
import org.jetbrains.anko.startActivity
import water.android.com.concrete.ConcreteActivity

/**
 * Created by EdgeDi
 * 2017/12/4 16:25
 */
class LoginActivity : ConcreteActivity() {

    override fun layout() = R.layout.activity_login

    override fun getStatus() = false

    override fun initUI() {

    }

    override fun setListener() {

    }

    fun login(view: View) {
        startActivity<HomeActivity>()
        finish()
    }
}