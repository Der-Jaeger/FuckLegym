package central.stu.fucklegym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.liangguo.androidkit.app.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fucklegym.top.entropy.NetworkSupport;

class GetOnlineMaps extends Thread{
    private Handler handler;
    private JSONObject maps;
    Message message;
    public GetOnlineMaps(Handler handler){
        this.handler = handler;
        message = handler.obtainMessage();
    }

    @Override
    public void run() {
        try {
            maps = NetworkSupport.getForReturn("https://foreverddb.github.io/FuckLegym/maps.json", new HashMap<String, String>());
            message.what = OnlineMaps.SUCCESS;
            message.obj = maps;
            handler.sendMessage(message);
        }catch (IOException e){
            e.printStackTrace();
            handler.sendEmptyMessage(OnlineMaps.FAIL);
        }
    }
}
class ImportMaps extends Thread{
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> selectedMaps;
    private JSONObject maps;
    public ImportMaps(Handler handler, ArrayList<String> selectedMaps, SharedPreferences sharedPreferences, JSONObject maps){
        this.handler = handler;
        this.selectedMaps = selectedMaps;
        this.sharedPreferences = sharedPreferences;
        this.maps = maps;
    }

    @Override
    public void run() {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            JSONObject storage = new JSONObject();
            for (String str: selectedMaps){
                Log.d("import_maps", "run: " + maps.getJSONObject(str).toString());
                editor.putString(str, maps.getJSONObject(str).toString());
            }
            editor.apply();
            handler.sendEmptyMessage(OnlineMaps.IMPORTSUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            handler.sendEmptyMessage(OnlineMaps.FAIL);
        }
    }
}
public class OnlineMaps extends AppCompatActivity {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int IMPORTSUCCESS = 2;
    public JSONObject maps;
    private ArrayList<String> selectedMaps = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_maps);

        //自定义地图按钮
        findViewById(R.id.custom_edit_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnlineMaps.this, MapSelector.class));
            }
        });
        //增加云端地图按钮
        findViewById(R.id.add_maps_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/Foreverddb/FuckLegym#%E5%A6%82%E4%BD%95%E6%96%B0%E5%A2%9E%E8%B7%91%E6%AD%A5%E5%9C%B0%E5%9B%BE"));
                startActivity(intent);
            }
        });

        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case SUCCESS:
                        maps = (JSONObject) msg.obj;
                        TextView textView = (TextView) findViewById(R.id.online_loading);
                        textView.setText("");
                        LinearLayout layout = (LinearLayout) findViewById(R.id.online_maps);
                        for(String str: maps.keySet()){
                            CheckBox checkBox = (CheckBox) View.inflate(OnlineMaps.this, R.layout.checkbox, null);
                            checkBox.setText(str);
                            layout.addView(checkBox);
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(b){
                                        selectedMaps.add(str);
                                    }else {
                                        selectedMaps.remove(str);
                                    }
                                    Log.d("acac", "onClick: " + selectedMaps);
                                }
                            });
                        }
                        break;
                    case FAIL:
                        Toast.makeText(OnlineMaps.this, "加载错误...", Toast.LENGTH_SHORT).show();
                        break;
                    case IMPORTSUCCESS:
//                        Toast.makeText(OnlineMaps.this, "导入成功！请返回跑步界面并刷新。", Toast.LENGTH_SHORT).show();
                        ToastUtil.INSTANCE.success(getString(R.string.import_success), ToastUtil.INSTANCE.getDEFAULT_DURATION());
                        finish();
                        break;
                }
            }
        };
        Button importBtn = (Button) findViewById(R.id.import_maps_button);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImportMaps(handler, selectedMaps, getSharedPreferences("local_maps", MODE_PRIVATE), maps).start();
            }
        });
        new GetOnlineMaps(handler).start();
    }
}