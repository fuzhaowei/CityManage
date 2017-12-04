package water.android.com.concrete.picture

import android.app.ProgressDialog
import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_picture.*
import water.android.com.concrete.ConcreteActivity
import water.android.com.concrete.R
import java.io.File
import java.util.*

/**
 * Created by EdgeDi
 * 2017/10/27 11:22
 */
class PictureActivity : ConcreteActivity() {

    private var progress: ProgressDialog? = null
    private var imageview: ImageView? = null

    /**
     * 图片数量
     */
    private var pic_size = 0

    /**
     * 图片最多的文件夹
     */
    private var max_img: File? = null

    private val list = arrayListOf<String>()

    private var adapter: PicAdapter? = null

    /**
     * 临时辅助类，防止多次扫描
     */
    private val dirpaths = hashSetOf<String>()

    override fun layout() = R.layout.activity_picture

    override fun initUI() {
        getImgs()
    }

    override fun setListener() {

    }

    private fun getImgs() {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            ToastShow("无存储卡")
            return
        }
        progress = ProgressDialog.show(this, null, "正在加载...")
        Thread {
            kotlin.run {
                val img_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val contentResolver = contentResolver
                val query = contentResolver.query(img_uri,
                        null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        arrayOf("image/jpeg", "image/png"),
                        MediaStore.Images.Media.DATE_MODIFIED)
                while (query.moveToNext()) {
                    val path = query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA))
                    val parentFile = File(path).parentFile
                    val dirpath = parentFile.absolutePath
                    if (dirpaths.contains(dirpath)) {
                        continue
                    } else {
                        dirpaths.add(dirpath)
                    }
                    val pic_size = parentFile.list({ _, name -> name?.endsWith(".jpg") ?: false }).size
                    if (pic_size > this.pic_size) {
                        this.pic_size = pic_size
                        max_img = parentFile
                    }
                }
                query.close()
                dirpaths.clear()
                val asList = Arrays.asList(max_img?.list { dir, name ->
                    name?.endsWith(".jpg") ?: false
                })
                list.clear()
                asList.forEach { i ->
                    i?.forEach { j ->
                        list.add(j)
                    }
                }
                handler.obtainMessage(0x1, asList).sendToTarget()
            }
        }.start()
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                0x1 -> {
                    progress?.dismiss()
                    val list = msg.obj as List<String>
                    adapter = PicAdapter(this@PictureActivity, list, R.layout.pic_selete_item)
                    pic_grid.adapter = adapter
                }
            }
        }
    }

}