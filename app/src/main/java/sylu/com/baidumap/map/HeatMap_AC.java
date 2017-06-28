package sylu.com.baidumap.map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/27.
 */

public class HeatMap_AC extends BaseActivity {
    @BindView(R.id.heat_mapview)
    MapView heatMapview;
    @BindView(R.id.heat_switch)
    Switch heatSwitch;
    private BaiduMap bMap;
    private HeatMap hMap;
    private boolean isDestory = false;
    private List<LatLng> data;
    @Override
    protected void initData() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bMap = heatMapview.getMap();
        addHeatMap();
        bMap.setMapStatus(MapStatusUpdateFactory.zoomTo(5.0f));
        heatSwitch.setChecked(true);
        heatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addHeatMap();
                }
                else{
                    heatSwitch.setText("打开热图");
                    hMap.removeHeatMap();
                }
            }
        });
    }

    private void addHeatMap() {
        final Handler han = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(!isDestory)
                bMap.addHeatMap(hMap);
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<LatLng> da = getLocationData();
                Log.d("Heat","data是否为空："+(da==null));
                hMap = new HeatMap.Builder()
                        .data(da)
                        .build();
                han.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.heat_map;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        heatMapview.onDestroy();
        isDestory = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        heatMapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        heatMapview.onResume();
    }

    public List<LatLng> getLocationData() {
        if(data!=null){
            Log.d("Heat","data已保存");
            return data;
        }
        else {
            Log.d("Heat","data重新加载");
            data = new ArrayList<>();
            InputStream in = getResources().openRawResource(R.raw.locations);
            String json = new Scanner(in).useDelimiter("\\A").next();
            JSONArray ja;
            try {
                ja = new JSONArray(json);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject job = ja.getJSONObject(i);
                    LatLng lat = new LatLng(job.getDouble("lat"), job.getDouble("lng"));
                    data.add(lat);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
