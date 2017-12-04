package water.android.com.concrete

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

/**
 * Created by EdgeDi
 * 2017/9/19 17:25
 */
interface HttpService {

    @GET("{path}")
    fun GET(@Path("path") path: String, @QueryMap map: HashMap<String, String>): Call<ResponseBody>

    @POST("{path}")
    @FormUrlEncoded
    fun POST(@Path("path") path: String, @FieldMap map: HashMap<String, String>): Call<ResponseBody>

    @POST("{path}")
    @FormUrlEncoded
    fun Head(@Path("path") path: String, @FieldMap map: HashMap<String, String>, @HeaderMap head: HashMap<String, String>): Call<ResponseBody>

    @POST("{path}")
    fun JSON(@Path("path") path: String, @Body body: RequestBody): Call<ResponseBody>

    @Multipart
    @POST("{path}")
    fun UPLOAD(@Path("path") path: String, @PartMap map: HashMap<String, RequestBody>): Call<ResponseBody>

    @GET("{path}")
    fun LOAD(@Path("path") path: String, @QueryMap map: HashMap<String, String>): Call<ResponseBody>
}