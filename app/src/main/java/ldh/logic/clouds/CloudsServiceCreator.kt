package ldh.logic.clouds

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @author ldh
 * 时间: 2022/3/19 8:49
 * 邮箱: 2637614077@qq.com
 */
object CloudsServiceCreator {

    private const val BASE_URL = "https://gitee.com/liang_dh/flcloulds/raw/master/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)

    inline fun<reified T> create(): T = create(T::class.java)

}