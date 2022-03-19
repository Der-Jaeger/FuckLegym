package ldh.logic.interfaces;

import ldh.logic.legym.network.model.login.LoginResult;

/**
 * @author ldh
 * 时间: 2022/3/18 14:52
 * 邮箱: 2637614077@qq.com
 */
public interface LoginResultCallback {

    void onResult(LoginResult result);

}
