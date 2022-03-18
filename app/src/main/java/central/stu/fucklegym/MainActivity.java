package central.stu.fucklegym;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

import central.stu.fucklegym.fragments.BaseFragment;
import fucklegym.top.entropy.NetworkSupport;
import fucklegym.top.entropy.User;
import ldh.ui.run.RunningActivity;

//判断是否更新
class CheckUpdateThread extends Thread {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    private Handler handler;

    public CheckUpdateThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            JSONObject jsonObject = NetworkSupport.getForReturn("https://foreverddb.github.io/FuckLegym/msg.json", new HashMap<String, String>());
            Log.d("getUpdate", "showUpdateMsg: " + jsonObject.toJSONString());
            Message msg = handler.obtainMessage();
            msg.what = SUCCESS;
            msg.obj = jsonObject;
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
            Message msg = handler.obtainMessage();
            msg.what = FAIL;
            msg.obj = null;
            handler.sendEmptyMessage(FAIL);
        }
    }
}

public class MainActivity extends AppCompatActivity{
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAIL = 1;
    public static User staticUser;
    private boolean logined = false;
    private DrawerLayout drawerLayout;//滑动菜单
    private NavigationView navigationView;//滑动导航栏
    private BaseFragment currentFragment;//当前主页面的Fragment
    private Toolbar toolbar;//工具栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("mysha1", "onCreate: " + sHA1(getApplicationContext()));
        //获取顶部工具栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置滑动导航栏选中选项后关闭导航栏
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                //判断选项并替换Fragment
                switch (item.getItemId()){
                    case R.id.nav_run:
                        startActivity(new Intent(MainActivity.this, RunningActivity.class));
                        break;
                    case R.id.nav_activity:
                        startActivity(new Intent(MainActivity.this, SignUp.class));
                        break;
                    case R.id.nav_course:
                        startActivity(new Intent(MainActivity.this, CourseSignUpActivity.class));
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public void changeFragment(BaseFragment fragment){
        toolbar.setTitle(fragment.getTitle());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.the_activities_layout, fragment);
        transaction.commit();
    }
    /**
     * 设置菜单选项的每个按钮事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/Foreverddb/FuckLegym"));
                startActivity(intent);
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    //检查更新
    private void checkUpdate() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CheckUpdateThread.SUCCESS:
                        JSONObject jsonObject = (JSONObject) msg.obj;
                        String version = jsonObject.getString("current_version");
                        if (!version.equals(getVersionName())) {
                            StringBuffer s = new StringBuffer();
                            s.append("更新提醒：\n");
                            String[] msgs = jsonObject.getObject("msg", String[].class);
                            for (int i = 0; i < msgs.length; i++) {
                                s.append((i + 1) + ". " + msgs[i] + "\n");
                            }
                            alertDialogBuilder.setMessage(s);
                            alertDialogBuilder.setPositiveButton("去更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(jsonObject.getString("update_url")));
                                    startActivity(intent);
                                }
                            });
                            alertDialogBuilder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            final AlertDialog alertdialog1 = alertDialogBuilder.create();
                            alertdialog1.show();
                        }

                        break;
                    case CheckUpdateThread.FAIL:
                        Toast.makeText(MainActivity.this, "检查更新失败，请检查网络状态", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        new CheckUpdateThread(handler).start();
    }

    //获取当前版本号
    private String getVersionName() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sHA1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}