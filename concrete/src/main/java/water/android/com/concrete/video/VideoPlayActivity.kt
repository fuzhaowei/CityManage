package water.android.com.concrete.video

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.MediaController
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_video_play.*
import water.android.com.concrete.ConcreteActivity
import water.android.com.concrete.R
import java.util.*


/**
 * Created by EdgeDi
 * 2017/11/7 13:47
 */
class VideoPlayActivity : ConcreteActivity() {

    private val length_height by lazy { windowManager.defaultDisplay.height / 2 }
    private val widght by lazy { windowManager.defaultDisplay.width / 2 }
    private var audio_manage: AudioManager? = null
    private var audio_max = 0
    private var play_status = false
    private val max = 100000

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun layout() = R.layout.activity_video_play

    override fun initUI() {
        audio_manage = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        audio_max = audio_manage?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0
        val url = intent.getStringExtra("url")
        if (url == null) {
            ToastShow("视频地址为空")
            finish()
            return
        }
        if (url.startsWith("http") && url.startsWith("https")) {
            ToastShow("视频格式错误")
            finish()
            return
        }
        val mc = MediaController(this)
        mc.visibility = View.GONE
        video_view.setMediaController(mc)
        video_view.setVideoURI(Uri.parse(url))
        video_view.start()
        status_button.setImageResource(R.drawable.stop)
        play_status = true
        video_title.text = intent.getStringExtra("title")
        Linear()
    }

    private var x = 0f
    private var y = 0f
    private var move_y = 0f
    private var start_y = 0f
    /**
     * false 左边
     * turn 右边
     */
    private var left_right = false

    override fun setListener() {
        status_button.setOnClickListener {
            play_status = if (play_status) {
                video_view.pause()
                status_button.setImageResource(R.drawable.start)
                false
            } else {
                video_view.start()
                status_button.setImageResource(R.drawable.stop)
                true
            }
        }
        video_view.setOnCompletionListener {
            ToastShow("视频播放完成")
            finish()
        }
        video_view.setOnInfoListener { mp, what, extra ->
            if (what == 3) {
                loading_linear.visibility = View.GONE
                video_length.max = video_view.duration
                getTime()
            }
            false
        }
        video_view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x
                    y = event.y
                    start_y = event.y
                    left_right = event.x > widght
                }
                MotionEvent.ACTION_MOVE -> {
                    if (Math.abs(x - event.x) > 100) {
                        Log.e("sss", (max / widght).toString())
                    } else if (Math.abs(y - event.y) > 100) {
                        if (ss.visibility == View.GONE)
                            ss.visibility = View.VISIBLE
                        move_y = start_y - event.y
                        start_y = event.y
                        if (left_right) {//右边
                            if (move_y > 0) {//向上滑

                            } else {//向下滑

                            }
                            val i = audio_max / 100f
                            val j = i * move_y
                            volume(j)
                        } else {//左边
                            if (move_y > 0) {//向上滑

                            } else {//向下滑

                            }
                            val i = length_height / 100f
                            val j = i * move_y
                            lighteness(j)
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    ss.visibility = View.GONE
                    start_y = 0f
                    move_y = 0f
                    val f1 = Math.abs(x - event.x)
                    val f2 = Math.abs(y - event.y)
                    if (f1 < 80 && f2 < 80) {
                        top_linear.visibility = View.VISIBLE
                        bottom_linear.visibility = View.VISIBLE
                        Linear()
                    }
                }
            }
            true
        }
        video_length.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    video_view.seekTo(progress)
                }
            }

        })
    }

    private fun Linear() {
        handler.postDelayed({
            bottom_linear.visibility = View.GONE
            top_linear.visibility = View.GONE
        }, 3000)
    }

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 0x1) {
                info.text = msg.obj.toString()
                video_length.progress = video_view.currentPosition
                video_length.secondaryProgress = video_view.bufferPercentage
                getTime()
            }
        }
    }

    private fun getTime() {
        handler.postDelayed({
            val all = stringForTime(video_view.currentPosition)
            val duration = stringForTime(video_view.duration)
            handler.obtainMessage(0x1, "$all/$duration").sendToTarget()
        }, 1000)
    }

    private fun stringForTime(timeMs: Int): String {
        val mFormatBuilder = StringBuilder()
        val mFormatter: Formatter
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000

        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600

        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    private fun volume(index: Float) {
        val before = audio_manage?.getStreamVolume(AudioManager.STREAM_MUSIC)
        val end = index % 1
        var adjust =
                if (end >= 0.5) {
                    before!! + 1 + index.toInt()
                } else {
                    before!! + index.toInt()
                }
        adjust =
                when {
                    adjust > 15 -> 15
                    adjust < 0 -> 0
                    else -> adjust
                }
        val i = (adjust.toDouble() / audio_max.toDouble()) * 100
        ss.text = "${i.toInt()}%"
        audio_manage?.setStreamVolume(AudioManager.STREAM_MUSIC, adjust, 0)
    }

    private fun lighteness(index: Float) {
        val i = index / 255f
        val lp = window.attributes
        var end = lp.screenBrightness + i
        end =
                if (end > 1f) {
                    0.8f
                } else {
                    0.3f
                }
        ss.text = "${(end * 100).toInt()}%"
        lp.screenBrightness = end
        window.attributes = lp
    }

    override fun onPause() {
        super.onPause()
        video_view.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        video_view.suspend()
    }

    fun back(view: View) = video_view.seekTo(video_view.currentPosition - 2000)

    fun go(view: View) = video_view.seekTo(video_view.currentPosition + 2000)

    fun finish(view: View) = finish()
}