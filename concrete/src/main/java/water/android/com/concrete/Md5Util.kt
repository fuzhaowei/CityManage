package water.android.com.concrete

/**
 * Created by EdgeDi
 * 2017/10/23 17:51
 */
object Md5Util {

    fun GetMD5Code(string: String) = string
//         val hash: ByteArray
//        try {
//            hash = MessageDigest.getInstance("MD5").digest(string.toByteArray(charset("UTF-8")))
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//            return null
//        } catch (e: UnsupportedEncodingException) {
//            e.printStackTrace()
//            return null
//        }
//
//        val hex = StringBuilder(hash.size * 2)
//        for (b in hash) {
//            if (b and 0xFF.toByte() < 0x10)
//                hex.append("0")
//            hex.append(Integer.toHexString((b and 0xFF.toByte()).toInt()))
//        }
//        return hex.toString()

}