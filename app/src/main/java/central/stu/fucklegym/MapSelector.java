package central.stu.fucklegym;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import maputil.Poi;
import maputil.PoiAdapter;

public class MapSelector extends AppCompatActivity implements PoiSearch.OnPoiSearchListener {
    private AMap aMap = null;//地图容器
    private MapView mMapView = null;//地图视图控件
    private ArrayList<Polyline> polylines = new ArrayList<Polyline>();//绘制路线
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            city = aMapLocation.getCity();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 19, 30, 0));
            aMap.animateCamera(cameraUpdate);
        }
    };
    private String city;
    //定位参数
    private AMapLocationClientOption mLocationOption = null;
    //定位蓝标图像
    private Marker marker;
    //需要的定位权限
    private String[] access = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    //信息栏
    private TextView infoBar;
    //地址搜索结果栏
    private ListView lv;
    //搜索结果适配器
    private PoiAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_selector);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //获取信息栏view
        infoBar = (TextView) findViewById(R.id.info_bar);
        //获取搜索结果栏
        lv = (ListView) findViewById(R.id.select_poi_list);
        //首先获取定位权限
        Request();
        //设置已同意隐私权限
        AMapLocationClient.updatePrivacyShow(getApplicationContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getApplicationContext(), true);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.the_map);
        //在activity执行onCreate时执行mMapView.onCre0ate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        //定位到当前位置
        getLocation();
        //设置搜索框
        EditText searchBar = (EditText) findViewById(R.id.map_search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                PoiSearch.Query query = null;
                if(editable.toString().trim().length() > 0){
                    query = new PoiSearch.Query(editable.toString().trim(), "", city);
                    query.setPageSize(10);
                    query.setPageNum(0);
                    try {
                        PoiSearch poiSearch = new PoiSearch(MapSelector.this, query);
                        poiSearch.setOnPoiSearchListener(MapSelector.this);
                        poiSearch.searchPOIAsyn();
                    } catch (AMapException e) {
                        Log.e("Amap", "afterTextChanged: " + e.getErrorMessage());
                    }
                }else {
                    adapter.clear();
                }
            }
        });
        //设置定位按钮
        findViewById(R.id.to_my_position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        List<LatLng> latLngs = new ArrayList<LatLng>();
        //撤销按钮
        findViewById(R.id.return_last_operation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!latLngs.isEmpty()) {
                    latLngs.remove(latLngs.size() - 1);
                    if (!latLngs.isEmpty()) {
                        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLngs.get(latLngs.size() - 1), 19, 30, 0));
                        aMap.animateCamera(mCameraUpdate);
                    }
                }
                Log.d("current_local", "onMapClick: " + latLngs.toString());
                try {
                    polylines.get(polylines.size() - 1).remove();
                    polylines.remove(polylines.size() - 1);
                    marker.destroy();
                    marker = aMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size() - 1)).snippet("DefaultMarker"));
                    infoBar.setText("已选取的路线长度为 " + calculateAllDistance() + " 米");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MapSelector.this, "已经没有更多点了！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //点击地图事件
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latLngs.add(latLng);
                Log.d("current_local", "onMapClick: " + latLngs.toString());
                polylines.add(aMap.addPolyline(new PolylineOptions().
                        addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1))));
                CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 19, 30, 0));
                aMap.animateCamera(mCameraUpdate);
                if (marker != null) {
                    marker.destroy();
                }
                marker = aMap.addMarker(new MarkerOptions().position(latLng).snippet("DefaultMarker"));
                infoBar.setText("已选取的路线长度为 " + calculateAllDistance() + " 米");
            }
        });

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        UiSettings mUiSettings;//定义一个UiSettings对象
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);

//        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    //获取当前所用密钥sha1值
    private static String sHA1(Context context) {
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
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取当前所在位置
    private void getLocation() {
        try {
            //设置定位参数
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setLocationCacheEnable(false);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            mLocationClient.setLocationListener(mLocationListener);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //计算路线长度
    private float calculateAllDistance(){
        float distance = 0;
        ArrayList<LatLng> points = (ArrayList<LatLng>) polylines.get(polylines.size() - 1).getPoints();
        for (int i = 0; i < polylines.size() - 1; i ++){
            distance += AMapUtils.calculateLineDistance(points.get(i), points.get(i + 1));
        }
        return distance;
    }

    //获取定位权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Request() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(access[0]) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(access[1]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, access, 1);
            }
        }
    }

    //检验定位权限是否申请成功
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkSelfPermission(access[0]) == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(MapSelector.this, "本功能需要定位权限才能正常运行！", Toast.LENGTH_SHORT).show();
            Request();
        }
    }

    //搜索结果回调
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        ArrayList<Poi> poiArrayList = new ArrayList<>();
        try {
            ArrayList<PoiItem> poiItems = poiResult.getPois();
            for(PoiItem item : poiItems){
                poiArrayList.add(new Poi(item.getTitle(), item.getLatLonPoint()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new PoiAdapter(this, R.layout.poi_listview, poiArrayList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LatLng latLng = new LatLng(poiArrayList.get(i).getLatLonPoint().getLatitude(), poiArrayList.get(i).getLatLonPoint().getLongitude());
                infoBar.setText(poiArrayList.get(i).getTitle() + "\n" + poiArrayList.get(i).getLatLonPoint());
                marker = aMap.addMarker(new MarkerOptions().position(latLng).snippet(poiArrayList.get(i).getTitle()));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 19 ,30, 0));
                aMap.animateCamera(cameraUpdate);
                adapter.clear();
            }
        });
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

}