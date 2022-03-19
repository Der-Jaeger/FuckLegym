package fucklegym.top.entropy;

import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.liangguo.easyingcontext.EasyingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import central.stu.fucklegym.R;

public class PathGenerator {
    //设置跑步范围经纬度坐标
    private static double[] latitude;
    private static double[] longitude;
    private static double base_lat, base_lon;

    public static HashMap<String, HashMap<String, double[]>> RunMaps = new HashMap<String, HashMap<String, double[]>>(){
        {
            put(EasyingContext.INSTANCE.getContext().getString(R.string.pref_key_running_map_uest_chengdu), new HashMap<String, double[]>(){{
                put("latitude", new double[]{30.74900133367409, 30.74898688255651, 30.74896815188424, 30.748940007385624, 30.74890075884211, 30.748858269547515, 30.74881181869255, 30.748767052700018, 30.74871888049938, 30.74867331788709, 30.748623451489145, 30.74857920474307, 30.74853030713002, 30.748481211584117, 30.748429527137464, 30.748383421657277, 30.748333985232744, 30.74828811599811, 30.748235951285466, 30.748191096023184, 30.748142175480325, 30.748097550703548, 30.748058617728123, 30.748018544501814, 30.747980211939076, 30.747947191065723, 30.74791292078947, 30.747890330417512, 30.74787416782794, 30.74786240621775, 30.747856523760166, 30.74785980010884, 30.74785690800689, 30.747865959569513, 30.747875314209224, 30.747908380679696, 30.747926722451464, 30.74797856162601, 30.7479974271559, 30.748031924278937, 30.748067004124913, 30.748103603148873, 30.748137991505164, 30.748181086323175, 30.748213615032288, 30.748256225717085, 30.74829499944973, 30.74832378615495, 30.7483531090519, 30.748389993183903, 30.748432167277432, 30.74846407370112, 30.74849832952399, 30.748540586821097, 30.748583705215964, 30.74862533577733, 30.748666868496855, 30.748708004494137, 30.74875759253553, 30.748795589174925, 30.74882992478308, 30.748871139113955, 30.748905943676657, 30.74894293072605, 30.748983362714043, 30.74902341708968, 30.749063701911755, 30.74909634844098, 30.749133504479673, 30.749167350515883, 30.749198730460648, 30.74922822967705, 30.749257753706267, 30.74928112423463, 30.749302023272534, 30.74931730877049, 30.749329927264625, 30.749337665026857, 30.74933932563312, 30.74933726330152, 30.749331236584325, 30.749321107949623, 30.749308419600716, 30.749293066016048, 30.749276217255073, 30.749254621112684, 30.74922907315558, 30.749195509767382, 30.749160259494236, 30.749126420245425, 30.7490910564646, 30.749056590717725, 30.749025114532795, 30.7490045709538});
                put("longitude", new double[]{103.9359940899701,103.93599145457326,103.93599066232734,103.93599055090571,103.93599157105098,103.935993222198,103.93599519375348,103.93598593505452,103.93598757593618,103.93598097200996,103.93598729128452,103.93598433401458,103.9359894511563,103.93598906833532,103.93599740117877,103.93598897392566,103.9359951128949,103.93599098343176,103.9359992962188,103.93599759118769,103.9360076673111,103.93601323546063,103.93603907058,103.936065116064,103.93609798404005,103.93613011095266,103.9361700520723,103.93621270872048,103.93625148866946,103.93630326259604,103.93635229189177,103.93640412683638,103.93645220475928,103.9365054628182,103.93655375189736,103.93660181169626,103.93664484170414,103.93668806360834,103.936719642683,103.93674682436205,103.93677564913912,103.93679649927348,103.93681764988912,103.93681962541316,103.93682109955142,103.93682041012326,103.93681629418852,103.93681084535568,103.93682315941174,103.93681997511848,103.93681491758907,103.93681503919755,103.93681392862815,103.93681103510718,103.93680859245802,103.936808153451,103.93680922723829,103.93681268541958,103.93680773841793,103.93680587662148,103.93680458572246,103.93680687173809,103.9368046992286,103.936803468556,103.93681127474932,103.93680713883496,103.93679637066526,103.93678943926372,103.93677373183826,103.93674544103804,103.93672067667796,103.93669605252474,103.936667831773,103.93663244760168,103.9365899703367,103.93654697195876,103.93650001629362,103.93645421263544,103.9364055335529,103.93635606297768,103.93631878460998,103.93628158627824,103.93623848715006,103.93619960567376,103.93616458122015,103.93613304295478,103.93610161476654,103.93606921451588,103.9360395692364,103.93601712710708,103.93599905284368,103.93598628824404,103.93597916398726,103.93597697914348});
                put("base", new double[]{30.75231973150331, 103.93098944486947});
            }});
            put(EasyingContext.INSTANCE.getContext().getString(R.string.pref_key_running_map_swpu_chengdu), new HashMap<String, double[]>(){{
                put("latitude", new double[]{
                        30.825302,30.825417,30.825445,30.825472,30.825509,30.825578,30.825633,30.825666,30.825716,30.825799,30.825878,30.825905,30.825951,30.826002,30.826053,30.826066,30.826117,30.826200,30.826269,30.826324,30.826361,30.826398,30.826467,30.826518,30.826564,30.826555,30.826550,30.826522,30.826467,30.826421,30.826370,30.826343,30.826288,30.826260,30.826232,30.826209,30.826177,30.826103,30.826094,30.826062,30.826002,30.825951,30.825910,30.825859,30.825822,30.825785,30.825730,30.825647,30.825560,30.825454,30.825426,30.825394,30.825357,30.825339,30.825325,30.825302,30.825265,30.825274,30.825284,30.825306
                });
                put("longitude", new double[]{
                        104.184846,104.184878,104.184889,104.184889,104.184910,104.184926,104.184932,104.184948,104.184958,104.185007,104.185034,104.185039,104.185055,104.185060,104.185082,104.185093,104.185109,104.185141,104.185157,104.185135,104.185103,104.185060,104.184996,104.184846,104.184781,104.184685,104.184578,104.184454,104.184406,104.184384,104.184374,104.184352,104.184325,104.184304,104.184299,104.184288,104.184277,104.184256,104.184261,104.184245,104.184234,104.184207,104.184197,104.184186,104.184186,104.184170,104.184148,104.184111,104.184095,104.184122,104.184159,104.184181,104.184224,104.184272,104.184309,104.184336,104.184433,104.184508,104.184599,104.184755
                });
                put("base", new double[]{30.825302, 104.184846});
            }});
            put(EasyingContext.INSTANCE.getContext().getString(R.string.pref_key_running_map_uest_shahe), new HashMap<String, double[]>(){{
                put("latitude", new double[]{30.676965, 30.676988, 30.67703, 30.677035, 30.677016, 30.676998, 30.676924, 30.67685, 30.676725, 30.676532, 30.676315, 30.676103, 30.67607, 30.676006, 30.675987, 30.675858, 30.675738, 30.675715, 30.675687, 30.675683, 30.675641, 30.675637, 30.675604, 30.675567, 30.675567, 30.675586, 30.675609, 30.675646, 30.67578, 30.675807, 30.675918, 30.675964, 30.676038, 30.676144, 30.676259, 30.676296, 30.676379, 30.676536, 30.676721, 30.676785, 30.676832, 30.676887});
                put("longitude", new double[]{104.097982, 104.098036, 104.098132, 104.098256, 104.098385, 104.098454, 104.098519, 104.098626, 104.098669, 104.098658, 104.098658, 104.098631, 104.098637, 104.098637, 104.098647, 104.098621, 104.098572, 104.098551, 104.098497, 104.098486, 104.09846, 104.098406, 104.098374, 104.098293, 104.098272, 104.098149, 104.098057, 104.097993, 104.097859, 104.097832, 104.097805, 104.097821, 104.097821, 104.097827, 104.097827, 104.097864, 104.097853, 104.097848, 104.097853, 104.097864, 104.097886, 104.097891});
                put("base", new double[]{30.676965, 104.097982});
            }});
//"xx大学（xx校区）": {
//  "latitude": [],
//  "longitude": []
//}
        }
    };

    /**
     * 获取本地储存的地图
     * @param local_maps
     */
    public static void getLocalMaps(SharedPreferences local_maps){
        String[] maps = new String[]{};
        Map<String, ?> all = local_maps.getAll();
        for(String str: all.keySet()){
            double[] attr = new double[]{};
            JSONArray latitude = JSON.parseObject((String) all.get(str)).getJSONArray("latitude");
            JSONArray longitude = JSON.parseObject((String) all.get(str)).getJSONArray("longitude");
            double[] latitude_double = latitude.toJavaObject(double[].class);
            double[] longitude_double = longitude.toJavaObject(double[].class);
            double[] base = new double[]{latitude_double[0], longitude_double[0]};
            HashMap<String, double[]> theMap = new HashMap<>();
            theMap.put("latitude", latitude_double);
            theMap.put("longitude", longitude_double);
            theMap.put("base", base);
            RunMaps.put(str, theMap);
        }
    }

    /**
     * 将地图数据加载到类中
     * @param map
     */
    private static void setAttr(String map){
        HashMap<String, double[]> currentMap = RunMaps.get(map);
        latitude = currentMap.get("latitude");
        longitude = currentMap.get("longitude");
        base_lat = currentMap.get("base")[0];
        base_lon = currentMap.get("base")[1];
    }

    /**
     * 获取纬度经度数组长度最小值，防止两数组长度不一样而出错
     * @param latitude
     * @param longitude
     * @return
     */
    public static int getMin(double[] latitude, double[] longitude){
        return latitude.length <= longitude.length ? latitude.length : longitude.length;
    }

    /**
     * 计算路线一圈的长度，返回以米为单位的整数
     * @param latitude
     * @param longitude
     * @return
     */
    private static int calculateAllDistance(double[] latitude, double[] longitude){
        float distance = 0;
        ArrayList<LatLng> points = new ArrayList<>();
        for(int i = 0; i <  getMin(latitude, longitude);i ++ ){
            points.add(new LatLng(latitude[i], longitude[i]));
        }
        for (int i = 0; i < points.size() - 1; i ++){
            distance += AMapUtils.calculateLineDistance(points.get(i), points.get(i + 1));
        }
        return (int)distance;
    }

    public static ArrayList<Pair<Double,Double>> genRegularRoutine(String map, double totalMile){

        int totalMeter = (int)(totalMile * 1000);
        int offset = 6;//经纬度随机偏移量
        setAttr(map);

        int cycleMeter = calculateAllDistance(latitude, longitude);//路线一圈的长度

        ArrayList<Pair<Double,Double>> points = new ArrayList<>();
        Random rad = new Random(System.currentTimeMillis());
        for(int j = 0;j <= totalMeter/cycleMeter;j ++){
            Log.d("pathG", "genRegularRoutine: length:" + latitude.length);
            Log.d("pathG", "genRegularRoutine: j:" + j);
            if(totalMeter/cycleMeter - j - 1 >= 0) {
                for (int i = 0; i < getMin(latitude, longitude); i++) {
                    Log.d("pathG", "genRegularRoutine: i:" + i);
                    points.add(new Pair(latitude[i] + rad.nextInt(offset) * 1e-5, longitude[i] + rad.nextInt(offset) * 1e-5));
                }
            }else {
                int lastMeter = totalMeter - j * cycleMeter;
                double rate = ((double) lastMeter)/((double)cycleMeter);
                Log.d("run_rate", "genRegularRoutine: " + rate);
                for (int i = 0; i < getMin(latitude, longitude)*rate; i++) {
                    points.add(new Pair(latitude[i] + rad.nextInt(offset) * 1e-5, longitude[i] + rad.nextInt(offset) * 1e-5));
                }
            }
        }
        if(totalMeter < 0){
            points.add(new Pair<>(latitude[0], longitude[0]));
        }

        return points;
    }

}
