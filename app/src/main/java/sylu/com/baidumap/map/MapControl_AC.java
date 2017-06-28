package sylu.com.baidumap.map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import butterknife.BindView;
import butterknife.OnClick;
import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/25.
 */

public class MapControl_AC extends BaseActivity {
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.zoombutton)
    Button zoombutton;
    @BindView(R.id.zoomlevel)
    EditText zoomlevel;
    @BindView(R.id.rotatebutton)
    Button rotatebutton;
    @BindView(R.id.rotateangle)
    EditText rotateangle;
    @BindView(R.id.overlookbutton)
    Button overlookbutton;
    @BindView(R.id.overlookangle)
    EditText overlookangle;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.bmapView)
    TextureMapView bmapView;
    @BindView(R.id.updatestatus)
    Button updatestatus;
    @BindView(R.id.savescreen)
    Button savescreen;
    private BaiduMap bMap;
    private LatLng currentLatlng;
    private String clicktype;
    private MapStatus.Builder mapSta;
    private BitmapDescriptor bd;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bMap = bmapView.getMap();
        bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        listener();
    }

    private void listener() {
        bMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                clicktype = "单击了Map-->";
                currentLatlng = latLng;
                showLatlng();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                clicktype = "单击了Poi-->";
                currentLatlng = mapPoi.getPosition();
                showLatlng();
                return false;
            }
        });
        bMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                clicktype = "长按了Map-->";
                currentLatlng = latLng;
                showLatlng();
            }
        });
        bMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            @Override
            public void onMapDoubleClick(LatLng latLng) {
                clicktype = "双击了Map-->";
                currentLatlng = latLng;
                showLatlng();
            }
        });
    }

    private void showLatlng() {
        String sta = "";
        sta = String.format(clicktype + "，经度：%f,纬度：%f", currentLatlng.longitude
                , currentLatlng.latitude);
        state.setText(sta);
        MarkerOptions mo = new MarkerOptions().icon(bd).position(currentLatlng);
        bMap.clear();
        bMap.addOverlay(mo);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.map_control;
    }


    @OnClick({R.id.zoombutton, R.id.rotatebutton, R.id.overlookbutton, R.id.updatestatus, R.id.savescreen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zoombutton:
                doZoom();
                break;
            case R.id.rotatebutton:
                doRotate();
                break;
            case R.id.overlookbutton:
                doOverlook();
                break;
            case R.id.updatestatus:
//                可在MapStatus对象中同时执行一系列操作
//                MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus())
//                        .rotate(rotateAngle).zoom(zoomLevel).overlook(overlookAngle).build();
                break;
            case R.id.savescreen:
                break;
        }
    }

    private void doOverlook() {
        try {
            MapStatus mSta = new MapStatus.Builder(bMap.getMapStatus())
                    .overlook(Float.parseFloat(overlookangle.getText().toString()))
                    .build();
            MapStatusUpdate up = MapStatusUpdateFactory.newMapStatus(mSta);
            bMap.animateMapStatus(up);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "角度錯誤", Toast.LENGTH_SHORT).show();

        } finally {
        }
    }

    private void doRotate() {
        try {
            MapStatus mSta = new MapStatus.Builder(bMap.getMapStatus())
                    .rotate(Float.parseFloat(rotateangle.getText().toString()))
                    .build();
            MapStatusUpdate up = MapStatusUpdateFactory.newMapStatus(mSta);
            bMap.animateMapStatus(up);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "角度錯誤", Toast.LENGTH_SHORT).show();

        } finally {
        }
    }

    private void doZoom() {
        try {
            MapStatusUpdate up = MapStatusUpdateFactory
                    .zoomTo(Float.parseFloat(zoomlevel.getText().toString()));
//        bMap.setMapStatus(up);
            bMap.animateMapStatus(up);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "級別有誤", Toast.LENGTH_SHORT).show();
        } finally {
        }

    }

    private void setMapstatus(LatLng ll) {
//        MapStatusUpdate iup = MapStatusUpdateFactory.newLatLng(ll);
//        bMap.setMapStatus(iup);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bmapView.onDestroy();
        Log.d(TAG, "mapControl==========onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
        Log.d(TAG, "mapControl==========onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapView.onResume();
        Log.d(TAG, "mapControl==========onResume");
    }
}
