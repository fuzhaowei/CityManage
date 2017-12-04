package water.android.com.concrete

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutionException


/**
 * Created by EdgeDi
 * 2017/10/17 9:51
 */
object ImageUtil {

    fun setImage(context: Context, url: String, view: ImageView, drawable: Drawable) {
        Glide.with(context)
                .load(url)
                .error(drawable)
                .fitCenter()
                .into(view)
    }

    fun setUri(context: Context, uri: String, view: ImageView, drawable: Drawable) {
        Glide.with(context)
                .load(File(uri))
                .error(drawable)
                .fitCenter()
                .into(view)
    }

    fun setImage(context: Context, url: String, view: ImageView, bitmap: Bitmap) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .override(10, 10)
                .error(BitmapDrawable(bitmap))
                .into(view)
    }

    fun setImage(context: Context, url: String, view: ImageView, res: Int) {
        Glide.with(context)
                .load(url)
                .override(50, 50)
                .transform(GlideCircleTransform(context))
                .error(context.resources.getDrawable(res))
                .into(view)
    }

    fun setImage(context: Context, url: String, view: ImageView, res: Int, dp: Int) {
        Glide.with(context)
                .load(url)
                .transform(GlideRoundTransform(context, dp))
                .error(context.resources.getDrawable(res))
                .into(view)
    }

    fun getBitMap(context: Context, url: String): Bitmap? {
        return try {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .fitCenter()
                    .into(500, 500)
                    .get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            null
        } catch (e: ExecutionException) {
            e.printStackTrace()
            null
        }

    }

    class GlideCircleTransform(context: Context) : BitmapTransformation(context) {

        override fun getId() = javaClass.toString()

        override fun transform(pool: BitmapPool?, toTransform: Bitmap?, outWidth: Int, outHeight: Int) = toRoundBitmap(toTransform!!)

    }

    class GlideRoundTransform(context: Context, dp: Int) : BitmapTransformation(context) {

        private var radius = 0f

        constructor(context: Context) : this(context, 4)

        init {
            radius = Resources.getSystem().displayMetrics.density * dp
        }

        override fun transform(pool: BitmapPool?, toTransform: Bitmap?, outWidth: Int, outHeight: Int) = roundCrop(pool, toTransform)

        private fun roundCrop(pool: BitmapPool?, source: Bitmap?): Bitmap? {
            if (source == null) return null
            var result = pool?.get(source.width, source.height, Bitmap.Config.ARGB_8888)
            if (result == null) {
                result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
            }
            val canvas = Canvas(result)
            val paint = Paint()
            paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.isAntiAlias = true
            val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
            canvas.drawRoundRect(rectF, radius, radius, paint)
            return result
        }

        override fun getId() = javaClass.name + Math.round(radius)

    }

    /**
     * 获取图片绝对路径
     *
     * @param context
     * @param imageUri
     * @return
     */
    @TargetApi(19)
    fun getImageAbsolutePath(context: Activity?, imageUri: Uri?): String? {
        if (context == null || imageUri == null)
            return null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } // MediaStore (and general)
        else if ("content".equals(imageUri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(imageUri)) imageUri.lastPathSegment else getDataColumn(context, imageUri, null, null)
        } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
            return imageUri.path
        }// File
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri) = "com.android.externalstorage.documents" == uri.authority

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri) = "com.android.providers.downloads.documents" == uri.authority

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri) = "com.android.providers.media.documents" == uri.authority

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri) = "com.google.android.apps.photos.content" == uri.authority

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    fun scaleBitmap(origin: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        if (origin == null) {
            return null
        }
        val height = origin.height
        val width = origin.width
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)// 使用后乘
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (!origin.isRecycled) {
            origin.recycle()
        }
        return newBM
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    fun scaleBitmap(origin: Bitmap?, ratio: Float): Bitmap? {
        if (origin == null) {
            return null
        }
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.preScale(ratio, ratio)
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        origin.recycle()
        return newBM
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    fun cropBitmap(bitmap: Bitmap): Bitmap {
        val w = bitmap.width // 得到图片的宽，高
        val h = bitmap.height
        var cropWidth = if (w >= h) h else w// 裁切后所取的正方形区域边长
        cropWidth /= 2
        val cropHeight = (cropWidth / 1.2).toInt()
        return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false)
    }

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    fun rotateBitmap(origin: Bitmap?, alpha: Float): Bitmap? {
        if (origin == null) {
            return null
        }
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.setRotate(alpha)
        // 围绕原地进行旋转
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        origin.recycle()
        return newBM
    }

    /**
     * 偏移效果
     *
     * @param origin 原图
     * @return 偏移后的bitmap
     */
    fun skewBitmap(origin: Bitmap?): Bitmap? {
        if (origin == null) {
            return null
        }
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.postSkew(-0.6f, -0.3f)
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        origin.recycle()
        return newBM
    }

    /**
     * 压缩图片
     *
     * @param image
     * @param size    期望值
     * @param options 压缩率
     * @return
     */
    fun compressBitmap(image: Bitmap, size: Int, options: Int): Bitmap {
        var options = options
        val baos = ByteArrayOutputStream()
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().size / 1024 > size) {
            options -= 10// 每次都减少10
            baos.reset()// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        val isBm = ByteArrayInputStream(baos.toByteArray())
        // 把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    /**
     * 将图片变成圆形
     *
     * @param bitmap
     * @return
     */
    fun toRoundBitmap(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        val roundPx: Float
        val left: Float
        val top: Float
        val right: Float
        val bottom: Float
        val dst_left: Float
        val dst_top: Float
        val dst_right: Float
        val dst_bottom: Float
        if (width <= height) {
            roundPx = (width / 2).toFloat()
            top = 0f
            bottom = width.toFloat()
            left = 0f
            right = width.toFloat()
            height = width
            dst_left = 0f
            dst_top = 0f
            dst_right = width.toFloat()
            dst_bottom = width.toFloat()
        } else {
            roundPx = (height / 2).toFloat()
            val clip = ((width - height) / 2).toFloat()
            left = clip
            right = width - clip
            top = 0f
            bottom = height.toFloat()
            width = height
            dst_left = 0f
            dst_top = 0f
            dst_right = height.toFloat()
            dst_bottom = height.toFloat()
        }
        val output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val src = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        val dst = Rect(dst_left.toInt(), dst_top.toInt(), dst_right.toInt(), dst_bottom.toInt())
        val rectF = RectF(dst)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, src, dst, paint)
        return output
    }

    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                baos.flush()
                baos.close()

                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    fun base64ToBitmap(base64Data: String): Bitmap {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

}