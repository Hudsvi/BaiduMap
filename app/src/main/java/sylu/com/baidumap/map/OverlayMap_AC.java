package sylu.com.baidumap.map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/27.
 */

public class OverlayMap_AC extends BaseActivity {
    @BindView(R.id.overlay_mapView)
    MapView overlayMapView;
    @BindView(R.id.overlay_seekBar)
    SeekBar overlaySeekBar;
    @BindView(R.id.overlay_text)
    TextView overlayText;
    @BindView(R.id.overlay_clear)
    Button overlayClear;
    @BindView(R.id.overlay_reset)
    Button overlayReset;
    BaiduMap bMap;
    Marker ma, mb, m;
    private BitmapDescriptor ad = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    private BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
    private BitmapDescriptor cd = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bMap = overlayMapView.getMap();
        overlaySeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        initOverlay();
    }

    private void initOverlay() {
        //-------------边界覆盖物--------------
        LatLng lat1 = new LatLng(30.0111, 108.01111);
        LatLng lat2 = new LatLng(30.0621, 108.06211);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(lat1)
                .include(lat2)
                .build();
        OverlayOptions oop1 = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(ad)
                .transparency(0.6f);
        bMap.addOverlay(oop1);
        MapStatusUpdate up1 = MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        bMap.setMapStatus(up1);
        //----------------------------------------
        //------------带有动画和可移动的Marker
        LatLng lat3=new LatLng(29.999,108.00000);
        MarkerOptions mop1 = new MarkerOptions()
                .anchor(0.4f, 0.4f)//官方默认的是0.5f,1.0f(也就是图标中下方)
                .position(lat3)
                .icon(bd)
                //.rotate(45.0f)旋转
                .zIndex(8)
                .title("Marker1")
                .animateType(MarkerOptions.MarkerAnimateType.drop);
        mb=(Marker)(bMap.addOverlay(mop1));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.overlay_map;
    }


    @OnClick({R.id.overlay_clear, R.id.overlay_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.overlay_clear:
                bMap.clear();
                ma=null;
                mb=null;
                break;
            case R.id.overlay_reset:
                bMap.clear();
                ma=null;
                mb=null;
                initOverlay();
                break;
        }
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            float alpha=(float)seekBar.getProgress()/100;
            if (ma != null) {
                ma.setAlpha(alpha);
            }
            if (mb != null){
                mb.setAlpha(alpha);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overlayMapView.onDestroy();
        ad.recycle();
        bd.recycle();
        cd.recycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overlayMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overlayMapView.onResume();
    }
}
