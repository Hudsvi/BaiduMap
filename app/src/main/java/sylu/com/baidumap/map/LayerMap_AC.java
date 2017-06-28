package sylu.com.baidumap.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/24.
 */

public class LayerMap_AC extends BaseActivity {
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.layer_bmapView)
    MapView layerBmapView;
    private BaiduMap bMap;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bMap = layerBmapView.getMap();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.layer_map;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        layerBmapView.onDestroy();
        Log.d(TAG, "layerMap==========onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        layerBmapView.onPause();
        Log.d(TAG, "layerMap==========onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        layerBmapView.onResume();
        Log.d(TAG, "layerMap==========onResume");
    }

    public void setMapMode(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.normal:
                bMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.satellite:
                bMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
        }
    }
    public void setTraffic(View v){
bMap.setTrafficEnabled(((CheckBox)v).isChecked());
    }

    public void setBaiduHeatMap(View view) {
        bMap.setBaiduHeatMapEnabled(((CheckBox)view).isChecked());
    }
}
