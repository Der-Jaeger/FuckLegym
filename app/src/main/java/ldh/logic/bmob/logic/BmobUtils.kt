package ldh.logic.bmob.logic

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SQLQueryListener
import cn.bmob.v3.listener.SaveListener
import ldh.logic.bmob.model.BMOB_LEGYM_USER_TABLE
import ldh.logic.bmob.model.BmobUser
import ldh.logic.legym.network.model.login.LoginResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * @author ldh
 * 时间: 2022/3/19 19:52
 * 邮箱: 2637614077@qq.com
 */

/**
 * 获取一个新的[BmobQuery]对象
 */
private val bmobQuery: BmobQuery<BmobUser>
    get() = BmobQuery()


/**
 * 通过乐健的账号找到Bmob的账号
 *
 * 同步阻塞的方式
 *
 * 返回可能为空
 */
suspend fun getBmobDataByLegymId(legymId: String) = suspendCoroutine<BmobUser?> {
    bmobQuery.setSQL("SELECT * FROM $BMOB_LEGYM_USER_TABLE")
        .addWhereContains("userId", legymId)
        .doSQLQuery(object : SQLQueryListener<BmobUser>() {
            override fun done(p0: BmobQueryResult<BmobUser>?, p1: BmobException?) {
                it.resume(p0?.results?.lastOrNull())
            }
        })
}

/**
 * 用挂起函数来同步注册新用户
 */
suspend fun BmobUser.suspendSaveSync() = suspendCoroutine<String?> {
    save(object : SaveListener<String> () {
        override fun done(p0: String?, p1: BmobException?) {
            it.resume(p0)
        }
    })
}

/**
 * 基于账号和登录结果生成Bmob新用户
 */
fun LoginResult.generateBmobUser(legymId: String) = BmobUser(
    userId = legymId,
    integral = 20,
    isVip = false,
    schoolName = schoolName,
    organizationName = organizationName,
    year = year
)