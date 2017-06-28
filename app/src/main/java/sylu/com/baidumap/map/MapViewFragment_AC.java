package sylu.com.baidumap.map;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;

import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.MapViewFragment;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/24.
 */

public class MapViewFragment_AC extends BaseActivity {
    MapViewFragment mapfrag;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        LatLng center = new LatLng(30.001, 108.0004);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(center).overlook(-30).zoom(15);
        BaiduMapOptions op = new BaiduMapOptions()
                .mapStatus(builder.build())
                .compassEnabled(false)
                .zoomControlsEnabled(false);
        mapfrag=MapViewFragment.getInstance(op);
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran=fragmentManager.beginTransaction();
        tran.add(R.id.fragment_mapview,mapfrag,"mapviewfragment").commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmentmapview;
    }
}
