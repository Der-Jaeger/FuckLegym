package ldh.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @author ldh
 * 时间: 2022/3/18 12:53
 * 邮箱: 2637614077@qq.com
 */
object ServiceCreator {

    private const val BASE_URL = "https://cpes.legym.cn"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)

    inline fun<reified T> create(): T = create(T::class.java)
}