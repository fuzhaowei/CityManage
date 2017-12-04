package water.android.com.concrete

import android.os.Environment
import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSink
import okio.Okio
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 网络请求工具类
 * Created by EdgeDi
 * 2017/9/19 17:28
 */
object HttpUtil {

    /**
     * 上传文件TYPE，二进制流
     * 大部分上传文件为二进制流，但具体还需询问后台
     */
    val BINARY_SYSTEM = MediaType.parse("application/octet-stream")!!

    /**
     *  域名
     *  需要http://||https://开头，否则会报错
     */
    private var base_url = "http://192.168.19.121:8080/jhldxm_server/service/AccountService/"

    /**
     * jsonTYPE
     */
    private val json = MediaType.parse("application/json; charset=utf-8")

    /**
     * 存放get请求和post请求的参数
     */
    private val get_map = hashMapOf<String, String>()

    /**
     * 存放头部
     */
    private val head_map = hashMapOf<String, String>()

    /**
     * 存放上传文件
     */
    private val upload_mep = hashMapOf<String, RequestBody>()

    private lateinit var call: Call<ResponseBody>

    private var retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(base_url)
                .build()
    }

    fun putHead(key: String, value: String): HttpUtil {
        head_map.put(key, value)
        return this
    }

    /**
     * 将参数添加进键值队，在Ask之前调用
     * @key
     * @value
     */
    fun putKeyCode(key: String, value: String): HttpUtil {
        get_map.put(key, value)
        return this
    }

    /**
     * 更改域名
     * @url 域名
     */
    fun setBase_Url(url: String): HttpUtil {
        retrofit = Retrofit.Builder()
                .baseUrl(url)
                .build()
        return this
    }

    /**
     * 将要上传的文件和key添加进键值队并进行进度监听
     * @key key的形式：Filedata\";filename=\"icon.png  Filddata为上传key,icon.png为服务端接收到文件的名字
     * @file 要上传的文件
     * @type 服务端要求的文件类型，默认可使用已定义好的TYPE
     * @onLengthListener 单个文件下载进度回调
     */
    fun putKeyUpload(key: String, file: File, type: MediaType, onLengthListener: OnLengthListener?): HttpUtil {
        val value = object : RequestBody() {
            override fun contentType(): MediaType? = type

            override fun contentLength(): Long = file.length()

            override fun writeTo(sink: BufferedSink?) {
                if (onLengthListener !== null) {
                    onLengthListener.Count(contentLength())
                    val source = Okio.source(file)
                    val buffer = Buffer()
                    var length = 0L
                    var count = source.read(buffer, 2048)
                    while (count !== -1L) {
                        sink?.write(buffer, count)
                        length += count
                        count = source.read(buffer, 2048)
                        onLengthListener.Length(length)
                    }
                }
            }
        }
        upload_mep.put(key, value)
        return this
    }

    /**
     * 添加头部请求
     */
    private fun head(url: String) = retrofit.create(HttpService::class.java)
            .Head(url, get_map, head_map)

    /**
     * get请求
     */
    private fun get(url: String) = retrofit.create(HttpService::class.java)
            .GET(url, get_map)

    /**
     * post请求
     */
    private fun post(url: String) = retrofit.create(HttpService::class.java)
            .POST(url, get_map)

    /**
     * json请求
     */
    private fun json(url: String, json: JSONObject) =
            retrofit.create(HttpService::class.java)
                    .JSON(url, RequestBody.create(HttpUtil.json, json.toString()))

    /**
     * 上传文件
     */
    private fun upload(url: String) = retrofit.create(HttpService::class.java)
            .UPLOAD(url, upload_mep)

    /**
     * 下载文件
     */
    private fun load(url: String) = retrofit.create(HttpService::class.java)
            .LOAD(url, get_map)

    /**
     * 外部调用get
     * @url 接口
     * @onDataListener 请求数据回调
     */
    fun AskGet(url: String, onDataListener: OnDataListener) {
        call = get(url)
        Asynchronous(call, onDataListener)
    }

    /**
     * 外部调用post
     * 传参同上
     */
    fun AskPost(url: String, onDataListener: OnDataListener) {
        call = post(url)
        Asynchronous(call, onDataListener)
    }

    fun AskHead(url: String, onDataListener: OnDataListener) {
        call = head(url)
        Asynchronous(call, onDataListener)
    }

    /**
     * 外部调用json
     * @url 接口
     * @json JSONObject对象
     * @onDataListener 请求数据回调
     */
    fun AskJson(url: String, json: JSONObject, onDataListener: OnDataListener) {
        call = json(url, json)
        Asynchronous(call, onDataListener)
    }

    /**
     * 外部调用上传
     * 传参同get
     */
    fun AskUpload(url: String, onDataListener: OnDataListener) {
        call = upload(url)
        Asynchronous(call, onDataListener)
    }

    /**
     * 外部调用下载
     * @url 接口
     * @onLoadingListener 下载结果回调
     */
    fun AskLoad(url: String, name: String, onLoadingListener: OnLoadingListener) {
        call = load(url)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                onLoadingListener.onError(t.toString())
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                var input: InputStream? = null
                var fos: FileOutputStream? = null
                try {
                    input = response?.body()?.byteStream()
                    val buf = ByteArray(2048)
                    val SDPath = File(Environment.getExternalStorageDirectory().absolutePath + "/gallery")
                    if (!SDPath.exists()) {
                        SDPath.mkdir()
                    }
                    val total = response?.body()?.contentLength()
                    val file = File(SDPath, name)
                    fos = FileOutputStream(file)
                    var sum = 0L
                    var len = input?.read(buf)
                    while (len !== -1) {
                        fos.write(buf, 0, len!!)
                        sum += len
                        val progress = (sum * 1.0f / total!! * 1000).toInt()
                        onLoadingListener.onProgress(progress)
                        len = input?.read(buf)
                    }
                    fos.flush()
                    handler.post({
                        onLoadingListener.onSuccess(file)
                    })
                } catch (e: Exception) {
                    onLoadingListener.onError(e.toString())
                } finally {
                    try {
                        input?.close()
                        fos?.close()
                    } catch (io: IOException) {
                        onLoadingListener.onError(io.toString())
                    }
                }
            }

        })
        if (get_map.size > 0)
            get_map.clear()
    }

    /**
     * 在页面destroy时调用，结束还在请求的call
     */
    fun Cancel() {
        call.cancel()
    }

    /**
     * 发起请求
     */
    private fun Asynchronous(call: Call<ResponseBody>, onDataListener: OnDataListener) {
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                handler.post({
                    val string = response?.body()?.string()
                    if (string == null) {
                        onDataListener.Error("请求数据为空")
                    } else {
                        onDataListener.Success(string)
                    }
                })
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                handler.post({
                    onDataListener.Error(t.toString())
                })
            }

        })
        if (get_map.size > 0)
            get_map.clear()
        if (upload_mep.size > 0)
            upload_mep.clear()
        if (head_map.size > 0)
            head_map.clear()
    }

    private val handler = object : Handler(Looper.getMainLooper()) {}

    /**
     * 请求数据回调
     */
    interface OnDataListener {

        fun Error(e: String?)

        fun Success(content: String?)

    }

    /**
     * 上传回调
     */
    interface OnLengthListener {

        fun Length(length: Long)

        fun Count(count: Long)

    }

    /**
     * 下载回调
     */
    interface OnLoadingListener {

        fun onSuccess(file: File)

        fun onError(e: String)

        fun onProgress(progress: Int)

    }

}