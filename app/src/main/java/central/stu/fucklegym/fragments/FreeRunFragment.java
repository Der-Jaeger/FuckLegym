package central.stu.fucklegym.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import central.stu.fucklegym.R;
import fucklegym.top.entropy.User;

/**
 * 刷跑步的Fragment
 *
 * @author Forever_DdB
 */
public class FreeRunFragment extends BaseFragment {
    /**
     * 通用构造方法
     * @param title 工具栏显示的标题
     * @param user 通用的user
     */
    public FreeRunFragment(String title, User user) {
        this.setUser(user);
        this.setTitle(title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_running, container, false);
        return view;
    }
}
