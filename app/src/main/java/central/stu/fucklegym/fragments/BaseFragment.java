package central.stu.fucklegym.fragments;

import androidx.fragment.app.Fragment;

import fucklegym.top.entropy.User;

/**
 * 通用fragment基类
 *
 * @author Forever_DdB
 */
public class BaseFragment extends Fragment {
    //通用的User
    private User user;
    //显示在工具栏的标题
    private String title;

    //java bean
    public void setUser(User user) {
        this.user = user;
    }

    //java bean
    public void setTitle(String title) {
        this.title = title;
    }

    //java bean
    public User getUser() {
        return user;
    }

    //java bean
    public String getTitle() {
        return title;
    }


}
