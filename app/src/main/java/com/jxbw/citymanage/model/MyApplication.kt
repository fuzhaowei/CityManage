package com.jxbw.citymanage.model

import android.app.Application
import com.baidu.mapapi.SDKInitializer

/**
 * Created by EdgeDi
 * 2017/12/4 16:25
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SDKInitializer.initialize(this)
    }

}