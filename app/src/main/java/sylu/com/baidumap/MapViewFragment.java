package sylu.com.baidumap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapView;

/**
 * Created by Hudsvi on 2017/6/24.
 */

public class MapViewFragment extends Fragment {
    private final String TAG=this.getClass().getName();
    private MapView bMap;
    private BaiduMapOptions op;
    public MapViewFragment() {
        super();
    }
    private  MapViewFragment(BaiduMapOptions op){
        this.op=op;
    }
public static MapViewFragment getInstance(){
    return new MapViewFragment();
}
public static MapViewFragment getInstance(BaiduMapOptions op){
    return new MapViewFragment(op);
}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.bMap=new MapView(this.getContext(),this.op);

        return this.bMap;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"frag=====Created");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        bMap.onResume();
        Log.d(TAG,"frag=====onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        bMap.onPause();
        Log.d(TAG,"frag=====onPause");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bMap.onDestroy();
        Log.d(TAG,"frag=====onDestoryed");

    }
    public BaiduMap getBaiduMap(){
        return bMap==null? null:bMap.getMap();
    }
    public MapView getMapView(){
        return bMap;
    }
}
