package central.stu.fucklegym;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import fucklegym.top.entropy.NetworkSupport;
import fucklegym.top.entropy.User;
import ldh.logic.OnlineData;
import ldh.ui.run.RunningActivity;

class LoginCheck extends Thread {
    String username;
    String password;
    Handler handler;

    public LoginCheck(String username, String password, Handler handler) {
        this.username = username;
        this.password = password;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            User user = new User(username, password);
            user.login();
            handler.sendEmptyMessage(MainActivity.LOGIN_SUCCESS);
            OnlineData.INSTANCE.getUser().postValue(user);
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(MainActivity.LOGIN_FAIL);
        }
    }
}

class Jump extends Thread {
    private Activity cont;

    public Jump(Activity con) {
        this.cont = con;
    }

    @Override
    public void run() {
        EditText username = (EditText) cont.findViewById(R.id.editText_username);
        EditText password = (EditText) cont.findViewById(R.id.editText_password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        Intent intent = new Intent(cont, RunningActivity.class);
        intent.putExtra("username", user);
        intent.putExtra("password", pass);
        cont.startActivity(intent);
//        cont.finish();
    }
}

class SignJump extends Thread {
    private Activity cont;

    public SignJump(Activity con) {
        this.cont = con;
    }

    @Override
    public void run() {
        EditText username = (EditText) cont.findViewById(R.id.editText_username);
        EditText password = (EditText) cont.findViewById(R.id.editText_password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        Intent intent = new Intent(cont, SignUp.class);
        intent.putExtra("username", user);
        intent.putExtra("password", pass);
        cont.startActivity(intent);
//        cont.finish();
    }

    void save(String username, String password) {

    }
}

class CourseSign extends Thread {
    private Activity cont;

    public CourseSign(Activity con) {
        this.cont = con;
    }

    @Override
    public void run() {
        EditText username = (EditText) cont.findViewById(R.id.editText_username);
        EditText password = (EditText) cont.findViewById(R.id.editText_password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        Intent intent = new Intent(cont, CourseSignUp.class);
        intent.putExtra("username", user);
        intent.putExtra("password", pass);
        cont.startActivity(intent);
//        cont.finish();
    }
}

//获取更新信息
class UpdateMsgThread extends Thread {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    private Handler handler;

    public UpdateMsgThread(Handler handler) {
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAIL = 1;
    private boolean logined = false;
    private DrawerLayout drawerLayout;//滑动菜单
    private NavigationView navigationView;//滑动导航栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取顶部工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                checkLogin(item.getItemId());
                drawerLayout.closeDrawers();
                return true;
            }
        });

        //检查更新
        checkUpdate();
        Button but = (Button) findViewById(R.id.button_freeRun);
//        findViewById(R.id.button_running).setOnClickListener(v -> {
//            startActivity(new Intent(this, RunningActivity.class));
//        });
        but.setOnClickListener(this);
        ((Button) findViewById(R.id.button_signup)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_course_sign)).setOnClickListener(this);
        //获取文本框的账号密码
        EditText username = (EditText) findViewById(R.id.editText_username);
        EditText password = (EditText) findViewById(R.id.editText_password);
        ((Button) findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                MainActivity.this.saveUser(user, pass);
            }
        });

        //判断是否更新
        SharedPreferences currentVersion = getSharedPreferences("update", MODE_PRIVATE);
        String version = currentVersion.getString("current_version", "");
        if (!getVersionName().equals(version)) {
            showUpdateMsg();
        }
        //获取保存的账号密码
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        String usn = userInfo.getString("username", "");
        String pwd = userInfo.getString("password", "");
        username.setText(usn);
        password.setText(pwd);
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

    public void checkLogin(int id) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case LOGIN_SUCCESS:
                        Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                        switch (id) {
                            case R.id.nav_run:
                                jumpFreeRun();
                                break;
                            case R.id.nav_activity:
                                jumpSignUp();
                                break;
                            case R.id.nav_course:
                                jumpCourseSignUp();
                                break;
                        }
                        logined = true;
                        break;
                    case LOGIN_FAIL:
                        logined = false;
                        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("登陆失败")
                                .setMessage("乐健账户登陆检查失败，请重新检查您的乐健账号和密码！")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .create();
                        alertDialog.show();
                        break;
                }
            }
        };
        EditText username = (EditText) findViewById(R.id.editText_username);
        EditText password = (EditText) findViewById(R.id.editText_password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        new LoginCheck(user, pass, handler).start();
    }

    @Override
    public void onClick(View view) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case LOGIN_SUCCESS:
                        Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                        if (view.getId() == R.id.button_freeRun) {
                            jumpFreeRun();
                        } else if (view.getId() == R.id.button_signup) {
                            jumpSignUp();
                        } else if (view.getId() == R.id.button_course_sign) {
                            jumpCourseSignUp();
                        }
                        break;
                    case LOGIN_FAIL:
                        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("登陆失败")
                                .setMessage("乐健账户登陆检查失败，请重新检查您的乐健账号和密码！")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .create();
                        alertDialog.show();
                        break;
                }
            }
        };
        EditText username = (EditText) findViewById(R.id.editText_username);
        EditText password = (EditText) findViewById(R.id.editText_password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        new LoginCheck(user, pass, handler).start();
    }

    private void jumpFreeRun() {
        Jump jmp = new Jump(this);
        jmp.start();
    }

    private void jumpSignUp() {
        SignJump jmp = new SignJump(this);
        jmp.start();
    }

    private void jumpCourseSignUp() {
        CourseSign jmp = new CourseSign(this);
        jmp.start();
    }

    //    private void jumpWeb(String url){
//        Intent intent = new Intent(MainActivity.this, WebViewStarter.class);
//        intent.putExtra("url", url);
//        startActivity(intent);
//    }
    //保存账号密码
    void saveUser(String username, String password) {
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor userEdit = userInfo.edit();
        userEdit.putString("username", username);
        userEdit.putString("password", password);
        userEdit.apply();
        Toast.makeText(MainActivity.this, "账号密码保存成功！", Toast.LENGTH_SHORT).show();
    }

    //显示更新信息
    void showUpdateMsg() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        new UpdateThread().start();
        Handler handlerMsg = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UpdateMsgThread.SUCCESS:
                        StringBuffer s = new StringBuffer();
                        s.append("更新日志：\n");
                        JSONObject jsonObject = (JSONObject) msg.obj;
                        String[] msgs = jsonObject.getObject("msg", String[].class);
                        for (int i = 0; i < msgs.length; i++) {
                            s.append((i + 1) + ". " + msgs[i] + "\n");
                        }
                        alertDialogBuilder.setMessage(s);
                        alertDialogBuilder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        final AlertDialog alertdialog1 = alertDialogBuilder.create();
                        alertdialog1.show();
                        SharedPreferences update = getSharedPreferences("update", MODE_PRIVATE);
                        update.edit().putString("current_version", getVersionName()).apply();
                        break;
                    case UpdateMsgThread.FAIL:
                        break;
                }
            }
        };
        new UpdateMsgThread(handlerMsg).start();//获取更新信息
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
}