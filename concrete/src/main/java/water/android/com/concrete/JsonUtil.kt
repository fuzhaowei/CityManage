package water.android.com.concrete

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by EdgeDi
 * 2017/10/12 16:08
 */
object JsonUtil {

    private val gson = GsonBuilder().create()
    private var JSON: JSONObject? = null

    fun fromJson(json: String, cla: Class<*>) =
            try {
                gson.fromJson(json, cla)!!
            } catch (run: Exception) {
                null
            }

    fun fromJson(json: String, typeToken: TypeToken<*>) =
            try {
                gson.fromJson<List<*>>(json, typeToken.type)!!
            } catch (run: Exception) {
                ArrayList<Any>()
            }

    fun getString(json: String, key: String): String? {
        JSON = JSONObject(json)
        return JSON?.getString(key) ?: ""
    }

    fun getInt(json: String, key: String): Int? {
        JSON = JSONObject(json)
        return JSON?.getInt(key) ?: 0
    }

    fun getDouble(json: String, key: String): Double? {
        JSON = JSONObject(json)
        return JSON?.getDouble(key) ?: 0.0
    }

    fun getLong(json: String, key: String): Long? {
        JSON = JSONObject(json)
        return JSON?.getLong(key) ?: 0L
    }

    fun getBoolean(json: String, key: String): Boolean {
        return try {
            JSON = JSONObject(json)
            JSON?.getBoolean(key) ?: false
        } catch (e: JSONException) {
            false
        }
    }

}   