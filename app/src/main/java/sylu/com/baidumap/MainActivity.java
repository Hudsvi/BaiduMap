package sylu.com.baidumap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import sylu.com.baidumap.map.BaseMap_AC;
import sylu.com.baidumap.map.Geometry_AC;
import sylu.com.baidumap.map.HeatMap_AC;
import sylu.com.baidumap.map.LayerMap_AC;
import sylu.com.baidumap.map.Location_AC;
import sylu.com.baidumap.map.MapControl_AC;
import sylu.com.baidumap.map.MapViewFragment_AC;
import sylu.com.baidumap.map.OverlayMap_AC;
import sylu.com.baidumap.search.Geo_AC;
import sylu.com.baidumap.search.POISearch_AC;

public class MainActivity extends BaseActivity {


    @BindView(R.id.base_btn)
    Button baseBtn;
//    MapView baiduMapview;


    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }



    @Override
    protected int getLayoutId() {
        return R.layout.main;
    }


    @Override
    protected void onResume() {
        super.onResume();
//        Log.d("OnResume=====","onResum------>baiduMapview"+(baiduMapview==null));
//        baiduMapview.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        baiduMapview.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        baiduMapview.onPause();
    }

    @OnClick({R.id.base_btn,R.id.fragment_map,R.id.btn_layer,R.id.map_control,
    R.id.geometry_map,R.id.location,R.id.overlay_map,R.id.btn_heat,R.id.btn_geography,
    R.id.btn_poi})
    public void onViewClicked(View v) {
        switch(v.getId()){
            case R.id.base_btn:
                startAct(BaseMap_AC.class);
                break;
            case R.id.fragment_map:
                startAct(MapViewFragment_AC.class);
                break;
            case R.id.btn_layer:
                startAct(LayerMap_AC.class);
                break;
            case R.id.map_control:
                startAct(MapControl_AC.class);
                break;
            case R.id.geometry_map:
                startAct(Geometry_AC.class  );
                break;
            case R.id.location:
                startAct(Location_AC.class);
                break;
            case R.id.overlay_map:
                startAct(OverlayMap_AC.class);
                break;
            case R.id.btn_heat:
                startAct(HeatMap_AC.class);
                break;
            case R.id.btn_geography:
                startAct(Geo_AC.class);
                break;
            case R.id.btn_poi:
                startAct(POISearch_AC.class);
                break;
        }
    }

    private void startAct(Class baseMapClass) {
        startActivity(new Intent(MainActivity.this,baseMapClass));
    }
}
